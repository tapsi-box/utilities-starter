package box.tapsi.libs.utilities.simulator.data

import reactor.core.publisher.Mono

/**
 * Interface for data simulation operations.
 * Provides methods to seed and clear test data.
 */
interface DataSimulator {
  /**
   * Seeds test data by invoking all registered [Seedable] components.
   *
   * @return Mono that completes when seeding is done
   */
  fun seedData(): Mono<Void>

  /**
   * Clears test data by invoking all registered [Clearable] components.
   *
   * @return Mono that completes when clearing is done
   */
  fun clearData(): Mono<Void>
}
