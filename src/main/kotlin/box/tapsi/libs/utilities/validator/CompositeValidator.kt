package box.tapsi.libs.utilities.validator

import reactor.core.publisher.Mono

/**
 * A composite implementation of the [Validator] interface that aggregates multiple validators.
 * This class validates an input by sequentially delegating the validation to each validator
 * in the provided list. Each validator is invoked in order, and validation continues as long
 * as previous validations succeed.
 *
 * @param TInput The type of input that this validator processes.
 * @param validators A list of [Validator] instances to apply sequentially for input validation.
 */
class CompositeValidator<TInput : Any>(
  private val validators: List<Validator<TInput>>,
) : Validator<TInput> {
  override fun validate(input: TInput): Mono<Void> = validators.fold(Mono.empty()) { acc, validator ->
    acc.then(Mono.defer { validator.validate(input) })
  }
}
