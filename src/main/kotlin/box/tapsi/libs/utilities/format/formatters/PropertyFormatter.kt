package box.tapsi.libs.utilities.format.formatters

import reactor.core.publisher.Mono

/**
 * Interface for formatting properties in a reactive manner.
 *
 * This interface defines a contract for property formatting operations that can be composed
 * together to create a chain of formatting transformations.
 * Each formatter is responsible for a single, specific formatting operation.
 *
 * The interface is designed to be:
 * - Generic: Can handle any type of property
 * - Reactive: Returns a Mono to support asynchronous processing
 * - Composable: Multiple formatters can be chained together
 *
 * @param T The type of property to be formatted
 */
interface PropertyFormatter<T : Any> {
  /**
   * Formats the given property.
   *
   * @param property The property to be formatted
   * @return A Mono containing the formatted property
   */
  fun format(property: T): Mono<T>
}
