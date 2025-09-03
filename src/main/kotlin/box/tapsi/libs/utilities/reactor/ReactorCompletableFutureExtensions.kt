package box.tapsi.libs.utilities.reactor

import reactor.core.publisher.Mono
import reactor.core.publisher.SignalType
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future

fun <T> createCompletableFutureFromFuture(future: Future<T>): CompletableFuture<T> = object : CompletableFuture<T>() {
  override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
    val result = future.cancel(mayInterruptIfRunning)
    super.cancel(mayInterruptIfRunning)
    return result
  }
}

fun <T> CompletableFuture<T>.toMonoWithCancellation(): Mono<T> = Mono.fromFuture { this }
  .doFinally { signalType -> if (signalType == SignalType.CANCEL) this.cancel(true) }
