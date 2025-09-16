package box.tapsi.libs.utilities.validator

import reactor.core.publisher.Mono

object ValidatorTestHelper {
  data object ValidationType
  data object AnotherValidationType
  object ValidationTypeValidValidator : Validator<ValidationType> {
    override fun validate(input: ValidationType): Mono<Void> = Mono.empty()
  }

  object ValidationTypeInvalidValidator : Validator<ValidationType> {
    override fun validate(input: ValidationType): Mono<Void> = Mono.error(RuntimeException())
  }

  object AnotherValidationTypeValidValidator : Validator<AnotherValidationType> {
    override fun validate(input: AnotherValidationType): Mono<Void> = Mono.empty()
  }

  object AnotherValidationTypeInvalidValidator : Validator<AnotherValidationType> {
    override fun validate(input: AnotherValidationType): Mono<Void> = Mono.error(RuntimeException())
  }

  object ValidationTypesValidValidator :
    Validator<List<ValidationType>> {
    override fun validate(input: List<ValidationType>): Mono<Void> = Mono.empty()
  }

  object ValidationTypesInvalidValidator :
    Validator<List<ValidationType>> {
    override fun validate(input: List<ValidationType>): Mono<Void> = Mono.error(RuntimeException())
  }

  object AnotherValidationTypeRatingValidator : Validator<String> {
    override fun validate(input: String): Mono<Void> = Mono.empty()
  }

  object AnotherValidationTypeRatingInvalidValidator : Validator<String> {
    override fun validate(input: String): Mono<Void> = Mono.error(RuntimeException())
  }
}
