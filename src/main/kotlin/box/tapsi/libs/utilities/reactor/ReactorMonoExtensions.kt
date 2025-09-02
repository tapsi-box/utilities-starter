package box.tapsi.libs.utilities.reactor

import reactor.core.publisher.Mono
import kotlin.reflect.KClass

fun <T, E : Throwable> Mono<T>.doOnErrorExcept(
  excludedExceptionType: KClass<E>,
  onError: (Throwable) -> Unit,
): Mono<T> = doOnError({ !excludedExceptionType.isInstance(it) }) { onError(it) }
