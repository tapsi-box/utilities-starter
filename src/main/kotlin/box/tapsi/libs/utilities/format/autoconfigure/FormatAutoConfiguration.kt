package box.tapsi.libs.utilities.format.autoconfigure

import box.tapsi.libs.utilities.autoconfigure.UtilitiesAutoConfiguration
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.ComponentScan

@AutoConfiguration(after = [UtilitiesAutoConfiguration::class])
@ComponentScan("box.tapsi.libs.utilities.format")
class FormatAutoConfiguration
