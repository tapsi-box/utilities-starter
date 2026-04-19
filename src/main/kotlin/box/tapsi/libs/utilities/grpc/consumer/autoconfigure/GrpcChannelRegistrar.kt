package box.tapsi.libs.utilities.grpc.consumer.autoconfigure

import box.tapsi.libs.utilities.grpc.consumer.GrpcChannelHelper
import box.tapsi.libs.utilities.grpc.consumer.GrpcConsumerProperties
import io.grpc.Channel
import io.grpc.ClientInterceptor
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.BeanFactoryAware
import org.springframework.beans.factory.getBeanProvider
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.context.EnvironmentAware
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.env.Environment
import org.springframework.core.type.AnnotationMetadata

class GrpcChannelRegistrar :
  ImportBeanDefinitionRegistrar,
  EnvironmentAware,
  BeanFactoryAware {
  private lateinit var environment: Environment
  private lateinit var beanFactory: BeanFactory

  override fun setEnvironment(environment: Environment) {
    this.environment = environment
  }

  override fun setBeanFactory(beanFactory: BeanFactory) {
    this.beanFactory = beanFactory
  }

  override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
    val props = Binder.get(environment)
      .bind(
        "box.libs.utilities.grpc.consumer",
        GrpcConsumerProperties::class.java,
      ).orElseThrow {
        IllegalStateException("Failed to bind GrpcConsumerProperties from environment")
      }

    props.channels.forEach { (name, _) ->
      val cfg = GrpcChannelHelper.getGrpcChannelOrMaybeDefault(consumerProperties = props, channelName = name)
      if (registry.containsBeanDefinition(name)) return@forEach

      val bd = BeanDefinitionBuilder
        .genericBeanDefinition(Channel::class.java) {
          val interceptorList = beanFactory.getBeanProvider<ClientInterceptor>()
            .orderedStream()
            .toList()

          createManagedChannel(cfg, interceptorList)
        }.setDestroyMethodName("shutdown")
        .beanDefinition

      registry.registerBeanDefinition(name, bd)
    }
  }

  private fun createManagedChannel(
    cfg: GrpcConsumerProperties.GrpcChannel,
    interceptors: List<ClientInterceptor>,
  ): ManagedChannel = ManagedChannelBuilder.forAddress(cfg.host, cfg.port)
    .enableRetry()
    .maxRetryAttempts(cfg.retryAttempts)
    .intercept(interceptors)
    .apply {
      if (cfg.plaintext) {
        usePlaintext()
      }
    }.build()
}
