package box.tapsi.libs.utilities.format.formatters

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component(StringLowercaseFormatter.BEAN_NAME)
class StringLowercaseFormatter : PropertyFormatter<String> {
  override fun format(property: String): Mono<String> = property.lowercase().toMono()

  companion object {
    const val BEAN_NAME = "stringLowercaseFormatter"
  }
}
