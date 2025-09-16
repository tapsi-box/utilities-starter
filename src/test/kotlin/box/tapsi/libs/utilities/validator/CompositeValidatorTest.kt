package box.tapsi.libs.utilities.validator

import box.tapsi.libs.utilities.FixtureTestHelper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.spy
import org.mockito.kotlin.verifyNoInteractions
import reactor.kotlin.test.test

class CompositeValidatorTest {

  private val fixture = FixtureTestHelper.getDefaultFixture()

  @Test
  fun `should return empty mono when all validators are valid`() {
    // given
    val validValidator = spy(ValidatorTestHelper.ValidationTypeValidValidator)
    val validators = listOf(validValidator, validValidator)
    val compositeValidator = CompositeValidator(validators)
    val order = fixture<ValidatorTestHelper.ValidationType>()

    // when
    compositeValidator.validate(order)
      .test()
      .verifyComplete()

    // verify
    ValidatorTestHelper.ValidationTypeValidValidator.validate(order)
  }

  @Test
  fun `should return mono with error when one validator is invalid`() {
    // given
    val validValidator = spy(ValidatorTestHelper.ValidationTypeValidValidator)
    val invalidValidator = spy(ValidatorTestHelper.ValidationTypeInvalidValidator)
    val validators = listOf(validValidator, invalidValidator)
    val compositeValidator = CompositeValidator(validators)
    val order = fixture<ValidatorTestHelper.ValidationType>()

    // when
    compositeValidator.validate(order)
      .test()
      .verifyError()

    // verify
    ValidatorTestHelper.ValidationTypeValidValidator.validate(order)
    ValidatorTestHelper.ValidationTypeInvalidValidator.validate(order)
  }

  @Test
  fun `should ignore all other validator when one validator is invalid`() {
    // given
    val validValidator = spy(ValidatorTestHelper.ValidationTypeValidValidator)
    val invalidValidator = spy(ValidatorTestHelper.ValidationTypeInvalidValidator)
    val validators = listOf(invalidValidator, validValidator)
    val compositeValidator = CompositeValidator(validators)
    val order = fixture<ValidatorTestHelper.ValidationType>()

    // when
    compositeValidator.validate(order)
      .test()
      .verifyError()

    // verify
    verifyNoInteractions(validValidator)
    ValidatorTestHelper.ValidationTypeInvalidValidator.validate(order)
  }

  @Test
  fun `should return empty if the validator list is empty`() {
    // given
    val validators = emptyList<Validator<ValidatorTestHelper.ValidationType>>()
    val compositeValidator = CompositeValidator(validators)
    val order = fixture<ValidatorTestHelper.ValidationType>()

    // when

    // verify
    compositeValidator.validate(order)
      .test()
      .verifyComplete()
  }
}
