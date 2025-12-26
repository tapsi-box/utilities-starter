package box.tapsi.libs.utilities.simulator.data

import box.tapsi.libs.utilities.simulator.data.clearable.Clearable
import box.tapsi.libs.utilities.simulator.data.seedable.Seedable
import org.slf4j.Logger
import org.springframework.context.ApplicationContext
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Default implementation of [DataSimulator].
 * Automatically discovers and invokes [Seedable] and [Clearable] components.
 *
 * Note: Developers must implement [Seedable] and [Clearable] interfaces
 * for their components to participate in the simulator workflow.
 * The library does not automatically clear repositories or perform any
 * technology-specific operations.
 */
class DataSimulatorImpl(
  private val applicationContext: ApplicationContext,
  private val logger: Logger,
) : DataSimulator {

  override fun seedData(): Mono<Void> = Flux.fromIterable(
    applicationContext.getBeanNamesForType(Seedable::class.java).toList(),
  )
    .map { applicationContext.getBean(it) as Seedable }
    .flatMap { seedable ->
      seedable.seed().thenReturn(seedable)
    }
    .doOnError { logger.error("Error seeding data", it) }
    .then()

  override fun clearData(): Mono<Void> = Flux.fromIterable(
    applicationContext.getBeanNamesForType(Clearable::class.java).toList(),
  )
    .map { applicationContext.getBean(it) as Clearable }
    .flatMap { clearable ->
      clearable.clear().thenReturn(clearable)
    }
    .doOnError { logger.error("Error clearing data", it) }
    .then()
}
