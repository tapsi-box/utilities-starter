package box.tapsi.libs.utilities.time.autoconfigure

import box.tapsi.libs.utilities.time.TimeOperatorImpl
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Import

@AutoConfiguration
@Import(TimeOperatorImpl::class)
class TimeAutoConfiguration
