package box.tapsi.libs.utilities.context.providers

import io.grpc.Context
import io.grpc.Metadata
import java.util.UUID

class TraceIdContextDataProvider : AbstractContextDataProvider<String>() {
  private val contextKey = Context.key<String>("trace_id")

  override fun getDataFromHeaders(headers: Metadata?): String = contextKey.get() ?: UUID.randomUUID().toString()

  override fun getGrpcContextKey(): Context.Key<String> = contextKey

  override fun getReactorContextKey(): String = "trace_id"

  override fun getProviderName(): String = "trace-id"
}
