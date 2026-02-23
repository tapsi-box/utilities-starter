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
  if (!ctx.hasKey("trace_id")) {
    return ctx.put("trace_id", UUID.randomUUID().toString())
  }
  return ctx
}
