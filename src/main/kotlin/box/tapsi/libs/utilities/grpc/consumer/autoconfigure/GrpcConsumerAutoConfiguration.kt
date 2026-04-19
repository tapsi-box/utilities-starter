package box.tapsi.libs.utilities.grpc.consumer.autoconfigure

import box.tapsi.libs.utilities.autoconfigure.UtilitiesAutoConfiguration
import box.tapsi.libs.utilities.grpc.consumer.GrpcConsumerProperties
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Import

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
  matchIfMissing = false,
)
@Import(GrpcChannelRegistrar::class)
class GrpcConsumerAutoConfiguration
