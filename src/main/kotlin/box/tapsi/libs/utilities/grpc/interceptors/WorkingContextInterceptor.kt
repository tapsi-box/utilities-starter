package box.tapsi.libs.utilities.grpc.interceptors

import box.tapsi.libs.utilities.context.WorkingContextException
import box.tapsi.libs.utilities.context.services.WorkingContextService
import io.grpc.Context
import io.grpc.Contexts
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import io.grpc.Status

class WorkingContextInterceptor(
  private val workingContextService: WorkingContextService,
) : ServerInterceptor {
  override fun <ReqT, RespT> interceptCall(
    call: ServerCall<ReqT, RespT>?,
    headers: Metadata?,
    next: ServerCallHandler<ReqT, RespT>?,
  ): ServerCall.Listener<ReqT> = try {
    val ctx = workingContextService.populateGrpcContext(Context.current(), headers)
    Contexts.interceptCall(ctx, call, headers, next)
  } catch (err: WorkingContextException) {
    call!!.close(Status.NOT_FOUND.withCause(err), headers)
    object : ServerCall.Listener<ReqT>() {}
  }
}
