package box.tapsi.libs.utilities.format.autoconfigure

import box.tapsi.libs.utilities.format.formatters.StringLowercaseFormatter
import box.tapsi.libs.utilities.format.formatters.StringTrimFormatter
import box.tapsi.libs.utilities.format.formatters.factories.PropertyFormatterFactory
import box.tapsi.libs.utilities.format.generators.RandomCodeGenerator
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

@AutoConfiguration
@Import(
  PropertyFormatterFactory::class,
)
class FormatAutoConfiguration {

  @Bean(StringLowercaseFormatter.BEAN_NAME)
  fun stringLowercaseFormatter(): StringLowercaseFormatter = StringLowercaseFormatter()

  @Bean(StringTrimFormatter.BEAN_NAME)
  fun stringTrimFormatter(): StringTrimFormatter = StringTrimFormatter()

  @Bean(RandomCodeGenerator.BEAN_NAME)
  fun randomCodeGenerator(): RandomCodeGenerator = RandomCodeGenerator()
}
