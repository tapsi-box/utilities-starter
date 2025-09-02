package box.tapsi.libs.utilities.format.formatters.factories

import box.tapsi.libs.utilities.format.formatters.CompositePropertyFormatter
import box.tapsi.libs.utilities.format.formatters.PropertyFormatter
import org.slf4j.Logger
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class PropertyFormatterFactory(
  private val applicationContext: ApplicationContext,
  private val logger: Logger,
) {
  private val formatters: Map<String, PropertyFormatter<*>> by lazy {
    applicationContext.getBeansOfType(PropertyFormatter::class.java)
      .also { beans ->
        check(beans.isNotEmpty()) { "No property formatter beans found" }
      }
  }

  fun <TInput : Any> getFormatter(vararg beanName: String): PropertyFormatter<TInput> = beanName
    .mapNotNull { formatters[it] }
    .filterIsInstance<PropertyFormatter<TInput>>()
    .let { formatters ->
      if (formatters.isEmpty()) {
        logger.warn("No property formatter found for bean names: ${beanName.joinToString()}")
      }
      return CompositePropertyFormatter(formatters)
    }
}
