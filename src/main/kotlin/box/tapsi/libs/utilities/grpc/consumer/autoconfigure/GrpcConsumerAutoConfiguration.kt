package box.tapsi.libs.utilities.grpc.consumer.autoconfigure

import box.tapsi.libs.utilities.autoconfigure.UtilitiesAutoConfiguration
import box.tapsi.libs.utilities.grpc.consumer.GrpcConsumerProperties
import io.grpc.ClientInterceptor
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment

@AutoConfiguration(
  after = [
    UtilitiesAutoConfiguration::class,
  ],
)
@EnableConfigurationProperties(
  GrpcConsumerProperties::class,
)
@ConditionalOnProperty(
  prefix = "box.libs.utilities.grpc.consumer",
  name = ["enabled"],
  havingValue = "true",
  matchIfMissing = true,
)
class GrpcConsumerAutoConfiguration {
  @Bean
  fun grpcChannelRegistrar(
    environment: Environment,
    interceptors: List<ClientInterceptor>,
  ): GrpcChannelRegistrar {
    val props = Binder.get(environment).bind<GrpcConsumerProperties>(
      "box.libs.utilities.grpc.consumer",
      GrpcConsumerProperties::class.java,
    ).orElseThrow {
      IllegalStateException("Failed to bind GrpcConsumerProperties from environment")
    }

    return GrpcChannelRegistrar(props, interceptors)
  }
}
