package box.tapsi.libs.utilities.validator

import reactor.core.publisher.Mono
import java.util.function.Function

/**
 * Represents a contract for validating an input of type [TInput].
 * Implementations of this interface define logic to validate specific types of input.
 *
 * This interface extends [Function] to allow functional-style usage, where its implementation can be
 * passed as a function object. The [apply] method delegates to [validate].
 *
 * @param TInput The type of input that the validator can process. It extends [Any].
 */
fun interface Validator<TInput : Any> : Function<TInput, Mono<Void>> {
  override fun apply(input: TInput): Mono<Void> = validate(input)

  fun validate(input: TInput): Mono<Void>
}
