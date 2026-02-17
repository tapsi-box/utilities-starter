package box.tapsi.libs.utilities.context.providers

import box.tapsi.libs.utilities.logging.LoggerReactorContextKeys
import io.grpc.Context
import io.grpc.Metadata
import java.util.UUID

class TraceIdContextDataProvider : AbstractContextDataProvider<String>() {
  private val contextKey = Context.key<String>(LoggerReactorContextKeys.TRACE_ID_KEY)

  override fun getDataFromHeaders(headers: Metadata?): String? = contextKey.get() ?: UUID.randomUUID().toString()

  override fun getGrpcContextKey(): Context.Key<String> = contextKey

  override fun getReactorContextKey(): String = LoggerReactorContextKeys.TRACE_ID_KEY

  override fun getProviderName(): String = this::class.simpleName!!
}
