package box.tapsi.libs.utilities.security.autoconfigure

import box.tapsi.libs.utilities.security.SecurityProperties
import box.tapsi.libs.utilities.security.crypto.services.EncryptionService
import box.tapsi.libs.utilities.security.crypto.services.EncryptionServiceImpl
import box.tapsi.libs.utilities.security.hash.services.HashService
import box.tapsi.libs.utilities.security.hash.services.HashServiceImpl
import box.tapsi.libs.utilities.security.token.services.TokenService
import box.tapsi.libs.utilities.security.token.services.TokenServiceImpl
import box.tapsi.libs.utilities.time.TimeOperator
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.keygen.KeyGenerators
import org.springframework.security.crypto.keygen.StringKeyGenerator

@AutoConfiguration(after = [JacksonAutoConfiguration::class])
@EnableConfigurationProperties(SecurityProperties::class)
class SecurityAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  fun stringKeyGenerator(): StringKeyGenerator = KeyGenerators.string()

  @Bean
  @ConditionalOnMissingBean
  fun encryptionService(
    securityProperties: SecurityProperties,
    stringKeyGenerator: StringKeyGenerator,
  ): EncryptionService = EncryptionServiceImpl(securityProperties, stringKeyGenerator)

  @Bean
  @ConditionalOnMissingBean
  fun hashService(): HashService = HashServiceImpl()

  @Bean
  @ConditionalOnClass(ObjectMapper::class)
  @ConditionalOnBean(ObjectMapper::class)
  @ConditionalOnMissingBean
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
