package box.tapsi.libs.utilities.grpc.consumer.interceptors

import box.tapsi.libs.utilities.grpc.consumer.GrpcConsumerProperties
import io.grpc.CallOptions
import io.grpc.Channel
import io.grpc.ClientCall
import io.grpc.ClientInterceptor
import io.grpc.MethodDescriptor
import java.util.concurrent.TimeUnit

class ClientDeadlineInterceptor(
  private val client: GrpcConsumerProperties.GrpcClient,
) : ClientInterceptor {
  override fun <ReqT : Any, RespT : Any> interceptCall(
    methodDescriptor: MethodDescriptor<ReqT, RespT>,
    callOptions: CallOptions,
    channel: Channel,
  ): ClientCall<ReqT, RespT> = channel.newCall(
    methodDescriptor,
    callOptions.withDeadlineAfter(client.deadline.toMillis(), TimeUnit.MILLISECONDS),
  )
}
