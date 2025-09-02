package box.tapsi.libs.utilities.logging.autoconfiguration

import box.tapsi.libs.utilities.autoconfigure.UtilitiesAutoConfiguration
import box.tapsi.libs.utilities.logging.MdcContextLifter
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InjectionPoint
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import reactor.core.publisher.Hooks
import reactor.core.publisher.Operators

@AutoConfiguration(after = [UtilitiesAutoConfiguration::class])
class LoggingAutoConfiguration {
  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  fun logger(injectionPoint: InjectionPoint): Logger = LoggerFactory.getLogger(
    injectionPoint.methodParameter?.containingClass // constructor
      ?: injectionPoint.field?.declaringClass,
  ) // or field injection

  @PostConstruct
  fun contextOperatorHook() {
    Hooks.onEachOperator(MDC_CONTEXT_REACTOR_KEY, Operators.lift { _, subscriber -> MdcContextLifter(subscriber) })
  }

  @PreDestroy
  fun cleanupHook() {
    Hooks.resetOnEachOperator(MDC_CONTEXT_REACTOR_KEY)
  }

  companion object {
    val MDC_CONTEXT_REACTOR_KEY: String = LoggingAutoConfiguration::class.simpleName!!
  }
}
