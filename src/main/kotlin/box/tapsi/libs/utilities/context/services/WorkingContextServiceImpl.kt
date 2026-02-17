package box.tapsi.libs.utilities.context.services

import box.tapsi.libs.utilities.context.ContextProperties
import box.tapsi.libs.utilities.context.WorkingContextException
import box.tapsi.libs.utilities.context.factories.ContextDataProviderFactory
import box.tapsi.libs.utilities.user.User
import io.grpc.Metadata
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.util.context.ContextView
import io.grpc.Context as GrpcContext
import reactor.util.context.Context as ReactorContext

class WorkingContextServiceImpl(
  private val contextDataProviderFactory: ContextDataProviderFactory,
  private val properties: ContextProperties,
) : WorkingContextService {
  private val logger = LoggerFactory.getLogger(this::class.java)

  @Suppress("detekt.TooGenericExceptionCaught")
  override fun populateGrpcContext(grpcContext: GrpcContext, headers: Metadata?): GrpcContext {
    var ctx = grpcContext
    return try {
      contextDataProviderFactory.getAllContextDataProviders().forEach { provider ->
        ctx = provider.populateGrpcContext(ctx, headers)
      }
      ctx
    } catch (err: Exception) {
      logger.error("Error populating gRPC context", err)
      ctx
    }
  }

  override fun getCurrentUser(): Mono<User> = Mono.deferContextual { ctx ->
    return@deferContextual getData<User>(properties.userProviderName, ctx)
      .switchIfEmpty { Mono.error(WorkingContextException.UserNotFoundInContextException(reactorContext = ctx)) }
  }

  override fun populateReactorContext(reactorContext: ReactorContext, grpcContext: GrpcContext): ReactorContext {
    var ctx = reactorContext
    contextDataProviderFactory.getAllContextDataProviders().forEach { provider ->
      ctx = provider.populateReactorContext(ctx, grpcContext)
    }
    return ctx
  }

  override fun getCurrentUser(grpcContext: GrpcContext): User = getData(properties.userProviderName, grpcContext)
    ?: throw WorkingContextException.UserNotFoundInContextException(
      grpcContext,
    )

  private fun <T : Any> getData(providerName: String, context: ContextView): Mono<T> = Mono.defer {
    val ctxProvider = contextDataProviderFactory.getProvider<T>(providerName)
    return@defer Mono.fromCallable { ctxProvider.getData(context) }
  }

  private fun <T : Any> getData(providerName: String, grpcContext: GrpcContext): T? {
    val ctxProvider = contextDataProviderFactory.getProvider<T>(providerName)
    return ctxProvider.getData(grpcContext)
  }
}
