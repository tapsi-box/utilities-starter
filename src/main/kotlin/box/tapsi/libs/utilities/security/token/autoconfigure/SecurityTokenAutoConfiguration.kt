package box.tapsi.libs.utilities.security.token.autoconfigure

import box.tapsi.libs.utilities.security.SecurityProperties
import box.tapsi.libs.utilities.security.autoconfigure.SecurityAutoConfiguration
import box.tapsi.libs.utilities.security.token.services.TokenService
import box.tapsi.libs.utilities.security.token.services.TokenServiceImpl
import box.tapsi.libs.utilities.time.TimeOperator
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.context.annotation.Bean

@AutoConfiguration(after = [JacksonAutoConfiguration::class, SecurityAutoConfiguration::class])
@ConditionalOnClass(ObjectMapper::class)
class SecurityTokenAutoConfiguration {
  @ConditionalOnBean(ObjectMapper::class)
  @Bean
  fun tokenService(
    timeOperator: TimeOperator,
    logger: Logger,
    securityProperties: SecurityProperties,
    objectMapper: ObjectMapper,
  ): TokenService = TokenServiceImpl(
    timeOperator = timeOperator,
    logger = logger,
    securityProperties = securityProperties,
    objectMapper = objectMapper,
  )
}
