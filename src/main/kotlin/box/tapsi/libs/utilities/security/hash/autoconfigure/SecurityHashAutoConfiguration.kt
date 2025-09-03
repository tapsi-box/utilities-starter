package box.tapsi.libs.utilities.security.hash.autoconfigure

import box.tapsi.libs.utilities.security.autoconfigure.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.ComponentScan

@AutoConfiguration(after = [SecurityAutoConfiguration::class])
@ComponentScan("box.tapsi.libs.utilities.security.hash.services")
class SecurityHashAutoConfiguration
