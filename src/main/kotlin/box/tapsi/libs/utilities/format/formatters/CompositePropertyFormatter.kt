package box.tapsi.libs.utilities.format.formatters

import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

/**
 * A composite formatter that chains multiple formatters together.
 *
 * This class implements the [PropertyFormatter] interface and provides a way to
 * combine multiple formatters into a single formatting chain.
 * The formatters are applied in sequence, with each formatter receiving the output of the previous one.
 *
 * The composite pattern allows for:
 * - Flexible composition of formatting operations
 * - Sequential application of multiple transformations
 * - Reactive processing of the entire chain
 *
 * @param T The type of property to be formatted
 * @property formatters The list of formatters to be applied in sequence
 */
class CompositePropertyFormatter<T : Any>(
  private val formatters: List<PropertyFormatter<T>>,
) : PropertyFormatter<T> {
  /**
   * Applies all formatters in sequence to the input property.
   *
   * The formatting process:
   * 1. Starts with the input property
   * 2. Applies each formatter in sequence
   * 3. Returns the final formatted result
   *
   * @param property The property to be formatted through the chain
   * @return A Mono containing the property after all formatters have been applied
   */
  override fun format(property: T): Mono<T> = formatters.fold(property.toMono()) { acc, formatter ->
    acc.flatMap { formattedProperty -> formatter.format(formattedProperty) }
  }
}
