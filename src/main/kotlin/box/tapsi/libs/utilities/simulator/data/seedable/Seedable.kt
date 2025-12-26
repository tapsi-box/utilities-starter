package box.tapsi.libs.utilities.simulator.data.seedable

import reactor.core.publisher.Mono

/**
 * Interface for components that can seed test data.
 * Implement this interface to participate in the simulator's seed workflow.
 */
interface Seedable {
  /**
   * Seeds test data for this component.
   *
   * @return Mono that completes when seeding is done
   */
  fun seed(): Mono<Void>
}
