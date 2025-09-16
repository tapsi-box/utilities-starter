package box.tapsi.libs.utilities.validator.factories

import box.tapsi.libs.utilities.validator.CompositeValidator
import box.tapsi.libs.utilities.validator.Validator
import org.slf4j.Logger
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

/**
 * Factory class for managing and retrieving validator beans from the application context.
 * This class scans the application context for beans of type [Validator] and provides
 * functionality to fetch specific validators based on their bean names. If multiple bean
 * names are requested, a [CompositeValidator] is returned to aggregate their behavior.
 *
 * @constructor Initializes the factory with a logger and the application context to fetch the validators.
 *
 * @param logger Used to log warnings when no matching validators are found.
 * @param applicationContext Spring's application context used to fetch validator beans.
 */
@Component
class ValidatorFactory(
  private val logger: Logger,
  private val applicationContext: ApplicationContext,
) {
  private val validators: MutableMap<String, Validator<*>> by lazy {
    applicationContext.getBeansOfType(Validator::class.java)
      .also { beans ->
        check(beans.isNotEmpty()) { "No validator beans found" }
      }
  }

  fun <TInput : Any> getValidator(vararg beanName: String): Validator<TInput> = beanName
    .mapNotNull { validators[it] }
    .filterIsInstance<Validator<TInput>>()
    .let { validators ->
      if (validators.isEmpty()) {
        logger.warn("No validator found for bean names: ${beanName.joinToString()}")
      }
      return@let CompositeValidator(validators)
    }
}
