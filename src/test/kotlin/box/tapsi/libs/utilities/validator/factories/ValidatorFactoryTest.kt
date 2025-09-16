package box.tapsi.libs.utilities.validator.factories

import box.tapsi.libs.utilities.FixtureTestHelper
import box.tapsi.libs.utilities.validator.CompositeValidator
import box.tapsi.libs.utilities.validator.Validator
import box.tapsi.libs.utilities.validator.ValidatorTestHelper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.spy
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import org.slf4j.Logger
import org.springframework.context.ApplicationContext
import reactor.kotlin.test.test

class ValidatorFactoryTest {
  @InjectMocks
  private lateinit var factory: ValidatorFactory

  @Mock
  private lateinit var logger: Logger

  @Mock
  private lateinit var applicationContext: ApplicationContext

  private val fixture = FixtureTestHelper.getDefaultFixture()

  @BeforeEach
  fun init() {
    MockitoAnnotations.openMocks(this)
  }

  @Test
  fun `should ignore validator if the type is not match`() {
    // given
    val beanNames = fixture<Array<String>> {
      repeatCount { 4 }
    }
    val beans = listOf(
      spy(ValidatorTestHelper.ValidationTypeValidValidator),
      spy(ValidatorTestHelper.ValidationTypeInvalidValidator),
      spy(ValidatorTestHelper.AnotherValidationTypeValidValidator),
      spy(ValidatorTestHelper.AnotherValidationTypeInvalidValidator),
    )

    val deliveryRequest = fixture<ValidatorTestHelper.AnotherValidationType>()

    // when
    whenever(applicationContext.getBeansOfType(Validator::class.java)).thenReturn(
      beanNames.zip(beans).toMap(),
    )

    val validator = factory.getValidator<ValidatorTestHelper.AnotherValidationType>(*beanNames)

    validator.validate(deliveryRequest)
      .test()
      .verifyError()

    // verify
    assert(validator is CompositeValidator<ValidatorTestHelper.AnotherValidationType>)
  }

  @Test
  fun `should throw error when there is no registered bean of type ValidationTypeValidator`() {
    // given
    val beanNames = fixture<Array<String>> {
      repeatCount { 4 }
    }

    // when
    whenever(applicationContext.getBeansOfType(Validator::class.java)).thenReturn(emptyMap())

    // verify
    assertThrows<IllegalStateException> {
      factory.getValidator<ValidatorTestHelper.AnotherValidationType>(*beanNames)
    }

    verifyNoInteractions(logger)
  }
}
