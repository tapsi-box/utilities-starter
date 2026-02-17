package box.tapsi.libs.utilities.context.services

import box.tapsi.libs.utilities.user.User
import io.grpc.Metadata
import reactor.core.publisher.Mono
import io.grpc.Context as GrpcContext
import reactor.util.context.Context as ReactorContext

interface WorkingContextService {
  fun populateGrpcContext(grpcContext: GrpcContext, headers: Metadata?): GrpcContext
  fun populateReactorContext(reactorContext: ReactorContext, grpcContext: GrpcContext): ReactorContext
  fun getCurrentUser(): Mono<User>
  fun getCurrentUser(grpcContext: GrpcContext): User
}
