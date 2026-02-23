package box.tapsi.libs.utilities.logging.autoconfiguration

import box.tapsi.libs.utilities.autoconfigure.UtilitiesAutoConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InjectionPoint
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope

@AutoConfiguration(after = [UtilitiesAutoConfiguration::class])
class LoggingAutoConfiguration {
  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  fun logger(injectionPoint: InjectionPoint): Logger = LoggerFactory.getLogger(
    injectionPoint.methodParameter?.containingClass // constructor
      ?: injectionPoint.field?.declaringClass,
  ) // or field injection
}
