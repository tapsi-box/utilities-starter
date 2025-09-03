package box.tapsi.libs.utilities.security.autoconfigure

import box.tapsi.libs.utilities.autoconfigure.UtilitiesAutoConfiguration
import box.tapsi.libs.utilities.security.SecurityProperties
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties

@AutoConfiguration(after = [UtilitiesAutoConfiguration::class])
@EnableConfigurationProperties(SecurityProperties::class)
class SecurityAutoConfiguration
