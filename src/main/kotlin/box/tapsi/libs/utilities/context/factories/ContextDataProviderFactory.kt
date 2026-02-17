package box.tapsi.libs.utilities.context.factories

import box.tapsi.libs.utilities.context.providers.ContextDataProvider
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext

class ContextDataProviderFactory(
  private val applicationContext: ApplicationContext,
) {
  private val logger = LoggerFactory.getLogger(this::class.java)
  private val contextDataProviders = mutableMapOf<String, ContextDataProvider<*>>()
  private var initialized = false

  fun <T : Any> getProvider(providerName: String): ContextDataProvider<T> {
    if (!initialized) {
      initialize()
    }
    return requireNotNull(contextDataProviders[providerName] as ContextDataProvider<T>?) {
      "No context data provider found with name $providerName"
    }
  }

  fun getAllContextDataProviders(): List<ContextDataProvider<*>> {
    if (!initialized) {
      initialize()
    }
    return contextDataProviders.values.toList()
  }

  private fun initialize() {
    synchronized(this) {
      if (!initialized) {
        logger.debug("Initializing context data providers")
        applicationContext.getBeansOfType(ContextDataProvider::class.java).values.forEach { contextProvider ->
          contextDataProviders[contextProvider.getProviderName()] = contextProvider
          logger.debug("Context data provider {} registered", contextProvider::class)
        }
        logger.debug("Context data providers initialized")
        initialized = true
      }
    }
  }
}
