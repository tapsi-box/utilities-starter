package box.tapsi.libs.utilities.context.providers

import io.grpc.Metadata
import reactor.util.context.ContextView
import io.grpc.Context as GrpcContext
import reactor.util.context.Context as ReactorContext

interface ContextDataProvider<T : Any> {
  fun populateGrpcContext(grpcContext: GrpcContext, headers: Metadata?): GrpcContext
  fun populateReactorContext(reactorContext: ReactorContext, grpcContext: GrpcContext): ReactorContext
  fun getGrpcContextKey(): GrpcContext.Key<T>
  fun getReactorContextKey(): String
  fun getData(ctx: ContextView): T?
  fun getData(grpcContext: GrpcContext): T?
  fun getProviderName(): String
}
