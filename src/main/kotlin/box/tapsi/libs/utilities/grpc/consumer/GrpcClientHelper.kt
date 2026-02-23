package box.tapsi.libs.utilities.grpc.consumer

import org.slf4j.LoggerFactory

object GrpcClientHelper {
  private val logger = LoggerFactory.getLogger(this::class.java)

  @Throws(IllegalArgumentException::class)
  fun getGrpcClientOrMaybeDefault(
    grpcConnection: GrpcConsumerProperties,
    clientName: String,
  ): GrpcConsumerProperties.GrpcClient = requireNotNull(
    grpcConnection.clients[clientName] ?: getDefaultClient(clientName, grpcConnection),
  ) {
    "Grpc client $clientName wanted but not found in appConfig"
  }

  private fun getDefaultClient(
    clientName: String,
    grpcConnection: GrpcConsumerProperties,
  ): GrpcConsumerProperties.GrpcClient? = GrpcConsumerProperties.GrpcClient()
    .takeIf { grpcConnection.provideDefaultChannels }?.also {
      logger.warn("Default client $clientName created")
    }
}
