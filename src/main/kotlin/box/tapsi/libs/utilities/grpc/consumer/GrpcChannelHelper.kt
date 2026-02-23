package box.tapsi.libs.utilities.grpc.consumer

import org.slf4j.LoggerFactory

object GrpcChannelHelper {
  private const val DEFAULT_GRPC_PORT = 50051
  private val logger = LoggerFactory.getLogger(this::class.java)

  @Throws(IllegalArgumentException::class)
  fun getGrpcChannelOrMaybeDefault(
    consumerProperties: GrpcConsumerProperties,
    channelName: String,
  ): GrpcConsumerProperties.GrpcChannel = requireNotNull(
    consumerProperties.channels[channelName]
      ?: getDefaultChannel(channelName, consumerProperties),
  ) {
    "Grpc channel $channelName wanted but not found in appConfig"
  }

  private fun getDefaultChannel(
    channelName: String,
    grpcConnection: GrpcConsumerProperties,
  ): GrpcConsumerProperties.GrpcChannel? = GrpcConsumerProperties
    .GrpcChannel(host = channelName, port = DEFAULT_GRPC_PORT)
    .takeIf { grpcConnection.provideDefaultChannels }?.also {
      logger.warn("Default channel $channelName created with default port $DEFAULT_GRPC_PORT")
    }
}
