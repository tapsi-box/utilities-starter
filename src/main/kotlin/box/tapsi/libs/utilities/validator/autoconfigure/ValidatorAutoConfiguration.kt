package box.tapsi.libs.utilities.validator.autoconfigure

import box.tapsi.libs.utilities.validator.factories.ValidatorFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Import

@AutoConfiguration
@Import(ValidatorFactory::class)
class ValidatorAutoConfiguration
