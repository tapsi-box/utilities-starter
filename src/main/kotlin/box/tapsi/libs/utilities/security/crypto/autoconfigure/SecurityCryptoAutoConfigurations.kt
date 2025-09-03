package box.tapsi.libs.utilities.security.crypto.autoconfigure

import box.tapsi.libs.utilities.security.autoconfigure.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.security.crypto.keygen.KeyGenerators
import org.springframework.security.crypto.keygen.StringKeyGenerator

@AutoConfiguration(after = [SecurityAutoConfiguration::class])
@ComponentScan("box.tapsi.libs.utilities.security.crypto.services")
class SecurityCryptoAutoConfigurations {
  @Bean
  fun getStringKeyGenerator(): StringKeyGenerator = KeyGenerators.string()
}
