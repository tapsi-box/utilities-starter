package box.tapsi.libs.utilities.grpc.consumer.autoconfigure

import box.tapsi.libs.utilities.grpc.consumer.GrpcChannelHelper
import box.tapsi.libs.utilities.grpc.consumer.GrpcConsumerProperties
import io.grpc.Channel
import io.grpc.ClientInterceptor
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import java.util.concurrent.ConcurrentHashMap

class GrpcChannelRegistrar(
  private val props: GrpcConsumerProperties,
  private val interceptors: List<ClientInterceptor>,
) : BeanDefinitionRegistryPostProcessor,
  DisposableBean {
  private val createdChannels = ConcurrentHashMap<String, ManagedChannel>()

  override fun postProcessBeanDefinitionRegistry(registry: BeanDefinitionRegistry) {
    props.clients.forEach { (name, _) ->
      val cfg = GrpcChannelHelper.getGrpcChannelOrMaybeDefault(consumerProperties = props, channelName = name)
      if (registry.containsBeanDefinition(name)) return@forEach

      val bd = BeanDefinitionBuilder
        .genericBeanDefinition(Channel::class.java) {
          createManagedChannel(name, cfg)
        }.setDestroyMethodName("shutdown")
        .beanDefinition

      registry.registerBeanDefinition(name, bd)
    }
  }

  override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
    // NO-OP
  }

  override fun destroy() {
    createdChannels.values.forEach { ch ->
      try {
        ch.shutdown()
      } catch (_: Exception) {
        // NO-OP
      }
    }
  }

  private fun createManagedChannel(
    name: String,
    cfg: GrpcConsumerProperties.GrpcChannel,
  ): ManagedChannel = createdChannels.computeIfAbsent(name) {
    ManagedChannelBuilder.forAddress(cfg.host, cfg.port)
      .enableRetry()
      .maxRetryAttempts(cfg.retryAttempts)
      .intercept(interceptors)
      .apply {
        if (cfg.plaintext) {
          usePlaintext()
        }
      }.build()
  }
}
