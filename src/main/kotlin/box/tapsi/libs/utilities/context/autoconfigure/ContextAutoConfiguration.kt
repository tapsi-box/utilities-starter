package box.tapsi.libs.utilities.context.autoconfigure

import box.tapsi.libs.utilities.autoconfigure.UtilitiesAutoConfiguration
import box.tapsi.libs.utilities.context.ContextProperties
import box.tapsi.libs.utilities.context.factories.ContextDataProviderFactory
import box.tapsi.libs.utilities.context.providers.TraceIdContextDataProvider
import box.tapsi.libs.utilities.context.providers.UserContextDataProvider
import box.tapsi.libs.utilities.context.services.WorkingContextService
import box.tapsi.libs.utilities.context.services.WorkingContextServiceImpl
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

@AutoConfiguration(
  after = [UtilitiesAutoConfiguration::class],
)
@EnableConfigurationProperties(
  ContextProperties::class,
)
class ContextAutoConfiguration {
  @ConditionalOnMissingBean
  @Bean
  fun workingContextService(
    contextDataProviderFactory: ContextDataProviderFactory,
    contextProperties: ContextProperties,
  ): WorkingContextService = WorkingContextServiceImpl(
    contextDataProviderFactory,
    contextProperties,
  )

  @Bean
  fun contextDataProviderFactory(
    applicationContext: org.springframework.context.ApplicationContext,
  ): ContextDataProviderFactory = ContextDataProviderFactory(applicationContext)

  @ConditionalOnProperty(
    value = ["box.libs.utilities.context.data-providers.user.enabled"],
    havingValue = "true",
    matchIfMissing = false,
  )
  @ConditionalOnMissingBean
  @Bean
  fun registerUserContextDataProvider(
    contextProperties: ContextProperties,
  ): UserContextDataProvider = UserContextDataProvider(contextProperties)

  @ConditionalOnProperty(
    value = ["box.libs.utilities.context.data-providers.trace-id.enabled"],
    havingValue = "true",
    matchIfMissing = false,
  )
  @ConditionalOnMissingBean
  @Bean
  fun registerTraceIdContextDataProvider(): TraceIdContextDataProvider = TraceIdContextDataProvider()
}
