package box.tapsi.libs.utilities.simulator.actuator

import box.tapsi.libs.utilities.simulator.SimulatorResponse
import box.tapsi.libs.utilities.simulator.data.DataSimulator
import org.springframework.boot.actuate.endpoint.annotation.Endpoint
import org.springframework.boot.actuate.endpoint.annotation.Selector
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

/**
 * Spring Boot Actuator endpoint for simulator operations.
 * Provides HTTP endpoints for seeding and clearing test data.
 *
 * Endpoints:
 * - POST /simulator/seed - Seeds test data
 * - POST /simulator/clear - Clears test data
 */
@Component
@Endpoint(id = "simulator")
class SimulatorEndpoint(
  private val dataSimulator: DataSimulator,
) {

  /**
   * Handles simulator operations.
   * POST /simulator/{action} where action is "seed" or "clear"
   */
  @WriteOperation
  fun operation(@Selector action: String): Mono<SimulatorResponse> = when (action) {
    "seed" -> seed()
    "clear" -> clear()
    else -> Mono.just(
      SimulatorResponse(
        success = false,
        message = "Unknown action: $action. Supported actions: seed, clear",
      ),
    )
  }

  private fun seed(): Mono<SimulatorResponse> = dataSimulator.seedData()
    .then(Mono.just(SimulatorResponse(success = true, message = "Data seeded successfully")))
    .onErrorResume { error ->
      Mono.just(SimulatorResponse(success = false, message = "Error seeding data: ${error.message}"))
    }

  private fun clear(): Mono<SimulatorResponse> = dataSimulator.clearData()
    .then(Mono.just(SimulatorResponse(success = true, message = "Data cleared successfully")))
    .onErrorResume { error ->
      Mono.just(SimulatorResponse(success = false, message = "Error clearing data: ${error.message}"))
    }
}
