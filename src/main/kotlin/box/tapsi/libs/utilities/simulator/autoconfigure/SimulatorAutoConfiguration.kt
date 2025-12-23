package box.tapsi.libs.utilities.simulator.autoconfigure

import box.tapsi.libs.utilities.simulator.actuator.SimulatorEndpoint
import box.tapsi.libs.utilities.simulator.data.DataSimulator
import org.slf4j.Logger
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean

/**
 * Auto-configuration for the simulator library.
 *
 * This configuration:
 * - Creates a [DataSimulator] bean if not already present
 * - Registers the actuator endpoint if actuator is available
 * - Only activates when simulator is enabled via property
 *
 * Properties:
 * - `tapsi.utilities.simulator.enabled=true` - Enable simulator (default: false, must be explicitly enabled)
 * - `management.endpoints.web.exposure.include=simulator` - Expose simulator endpoint via HTTP
 *   (required for actuator endpoints)
 */
@AutoConfiguration
@ConditionalOnProperty(
  prefix = "tapsi.utilities.simulator",
  name = ["enabled"],
  havingValue = "true",
  matchIfMissing = false,
)
class SimulatorAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(DataSimulator::class)
  fun dataSimulator(
    applicationContext: org.springframework.context.ApplicationContext,
    logger: Logger,
  ): DataSimulator = box.tapsi.libs.utilities.simulator.data.DataSimulatorImpl(applicationContext, logger)

  @Bean
  @ConditionalOnClass(name = ["org.springframework.boot.actuate.endpoint.annotation.Endpoint"])
  fun simulatorEndpoint(dataSimulator: DataSimulator): SimulatorEndpoint = SimulatorEndpoint(dataSimulator)
}
