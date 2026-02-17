package box.tapsi.libs.utilities.context

import box.tapsi.libs.utilities.ErrorCodeString
import box.tapsi.libs.utilities.TapsiException
import io.grpc.Context as GrpcContext
import reactor.util.context.ContextView as ReactorContext

sealed class WorkingContextException(message: String) : TapsiException(message) {
  data class UserNotFoundInContextException(
    val grpcContext: GrpcContext? = null,
    val reactorContext: ReactorContext? = null,
  ) : WorkingContextException("User not found in context") {
    override fun getErrorCodeString(): ErrorCodeString = UserNotFoundErrorCodeString

    companion object {
      val UserNotFoundErrorCodeString = object : ErrorCodeString {
        override val codeString: String
          get() = "USER_NOT_FOUND_IN_CONTEXT"
      }
    }
  }
}
