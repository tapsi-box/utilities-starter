package box.tapsi.libs.utilities.simulator.data.clearable

import reactor.core.publisher.Mono

/**
 * Interface for components that can clear test data.
 * Implement this interface to participate in the simulator's clear workflow.
 */
interface Clearable {
  /**
   * Clears test data for this component.
   *
   * @return Mono that completes when clearing is done
   */
  fun clear(): Mono<Void>
}
