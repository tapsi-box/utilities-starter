package box.tapsi.libs.utilities.logging

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.context.Context
import java.util.UUID

fun <T> Mono<T>.addTraceIdToReactorContext(): Mono<T> = this.contextWrite {
  putTraceIdInContext(it)
}

fun <T> Flux<T>.addTraceIdToReactorContext(): Flux<T> = this.contextWrite {
  putTraceIdInContext(it)
}

private fun putTraceIdInContext(ctx: Context): Context {
  if (!ctx.hasKey(LoggerReactorContextKeys.TRACE_ID_KEY)) {
    return ctx.put(LoggerReactorContextKeys.TRACE_ID_KEY, UUID.randomUUID().toString())
  }
  return ctx
}
