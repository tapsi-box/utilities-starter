package box.tapsi.libs.utilities.format.formatters

import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

class StringTrimFormatter : PropertyFormatter<String> {
  override fun format(property: String): Mono<String> = property.trim().toMono()

  companion object {
    const val BEAN_NAME = "stringTrimFormatter"
  }
}
