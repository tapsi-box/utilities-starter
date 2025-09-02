@file:Suppress("detekt.TooManyFunctions")

package box.tapsi.libs.utilities.reactor

import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.context.ContextView
import java.util.function.Function
import kotlin.reflect.KClass

fun <TObject : Any, T> monoDeferWithObject(
  clazz: KClass<TObject>,
  monoFactory: Function<in TObject, out Mono<out T>>,
): Mono<T> = Mono.deferContextual { ctx ->
  val target = ctx.getOrThrow(clazz)
  return@deferContextual monoFactory.apply(target)
}

fun <TObject : Any, T> fluxDeferWithObject(
  clazz: KClass<TObject>,
  fluxFactory: Function<in TObject, out Publisher<T>>,
): Flux<T> = Flux.deferContextual { ctx ->
  val target = ctx.getOrThrow(clazz)
  fluxFactory.apply(target)
}

fun <TObject : Any, T> Mono<T>.withContextualObject(obj: TObject): Mono<T> = this.contextWrite {
  it.put(obj.getContextKey(), obj)
}

fun <TObject : Any, T> Flux<T>.withContextualObject(obj: TObject): Flux<T> = this.contextWrite {
  it.put(obj.getContextKey(), obj)
}

private fun <TObject : Any> TObject.getContextKey(): String = this::class.getContextKey()

private fun <TObject : Any> KClass<TObject>.getContextKey(): String = this.simpleName!!.trim().lowercase()

fun <TObject : Any> ContextView.getOrDefault(
  clazz: KClass<TObject>,
): TObject? = this.getOrDefault<TObject>(
  clazz.getContextKey(),
  null,
)

fun <TObject : Any> ContextView.getOrThrow(
  clazz: KClass<TObject>,
): TObject = this.getOrDefault<TObject>(clazz.getContextKey(), null)
  ?: error("No object of type ${clazz.simpleName} found in context")
