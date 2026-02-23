package box.tapsi.libs.utilities.grpc.consumer

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@Suppress("detekt.MagicNumber")
@ConfigurationProperties(prefix = "box.libs.utilities.grpc.consumer")
data class GrpcConsumerProperties(
  val enabled: Boolean = true,
  val channels: Map<String, GrpcChannel> = emptyMap(),
  val clients: Map<String, GrpcClient> = emptyMap(),
  val provideDefaultChannels: Boolean = true,
  val configureChannels: Boolean = true,
) {
  data class GrpcChannel(
    val host: String,
    val port: Int,
    val retryAttempts: Int = 1000,
    val plaintext: Boolean = true,
  )

  data class GrpcClient(
    val enabled: Boolean = true,
    val deadline: Duration = Duration.ofSeconds(10),
  )
}
