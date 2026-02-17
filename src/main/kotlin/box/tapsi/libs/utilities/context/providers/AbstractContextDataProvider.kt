package box.tapsi.libs.utilities.context.providers

import io.grpc.Context
import io.grpc.Metadata
import reactor.util.context.ContextView
import io.grpc.Context as GrpcContext
import reactor.util.context.Context as ReactorContext

abstract class AbstractContextDataProvider<T : Any> : ContextDataProvider<T> {
  abstract fun getDataFromHeaders(headers: Metadata?): T?

  override fun populateGrpcContext(grpcContext: GrpcContext, headers: Metadata?): GrpcContext {
    val data = getDataFromHeaders(headers) ?: return grpcContext
    return grpcContext.withValue(getGrpcContextKey(), data)
  }

  override fun populateReactorContext(reactorContext: ReactorContext, grpcContext: GrpcContext): ReactorContext {
    val value = getData(grpcContext) ?: return reactorContext
    return reactorContext.put(getReactorContextKey(), value)
  }

  override fun getData(ctx: ContextView): T? = ctx.getOrDefault<T>(getReactorContextKey(), null)

  override fun getData(grpcContext: Context): T? = getGrpcContextKey().get(grpcContext)
}
