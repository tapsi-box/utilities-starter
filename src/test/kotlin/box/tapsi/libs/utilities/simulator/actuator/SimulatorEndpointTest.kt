package box.tapsi.libs.utilities.simulator.actuator

import box.tapsi.libs.utilities.simulator.data.DataSimulator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import reactor.core.publisher.Mono
import reactor.kotlin.test.test

class SimulatorEndpointTest {
  private lateinit var simulatorEndpoint: SimulatorEndpoint

  @Mock
  private lateinit var dataSimulator: DataSimulator

  @BeforeEach
  fun init() {
    MockitoAnnotations.openMocks(this)
    simulatorEndpoint = SimulatorEndpoint(dataSimulator)
  }

  @Test
  fun `should return success response when seed data succeeds`() {
    // given
    whenever(dataSimulator.seedData()).thenReturn(Mono.empty())

    // when
    simulatorEndpoint.operation("seed")
      .test()
      .expectNextMatches { response ->
        response.success && response.message == "Data seeded successfully"
      }
      .verifyComplete()
  }

  @Test
  fun `should return error response when seed data fails`() {
    // given
    val errorMessage = "Seed operation failed"
    whenever(dataSimulator.seedData()).thenReturn(Mono.error(RuntimeException(errorMessage)))

    // when
    simulatorEndpoint.operation("seed")
      .test()
      .expectNextMatches { response ->
        !response.success &&
          response.message.contains("Error seeding data") &&
          response.message.contains(errorMessage)
      }
      .verifyComplete()
  }

  @Test
  fun `should handle null error message when seed fails`() {
    // given
    whenever(dataSimulator.seedData()).thenReturn(Mono.error(RuntimeException()))

    // when
    simulatorEndpoint.operation("seed")
      .test()
      .expectNextMatches { response ->
        !response.success && response.message.contains("Error seeding data")
      }
      .verifyComplete()
  }

  @Test
  fun `should return success response when clear data succeeds`() {
    // given
    whenever(dataSimulator.clearData()).thenReturn(Mono.empty())

    // when
    simulatorEndpoint.operation("clear")
      .test()
      .expectNextMatches { response ->
        response.success && response.message == "Data cleared successfully"
      }
      .verifyComplete()
  }

  @Test
  fun `should return error response when clear data fails`() {
    // given
    val errorMessage = "Clear operation failed"
    whenever(dataSimulator.clearData()).thenReturn(Mono.error(RuntimeException(errorMessage)))

    // when
    simulatorEndpoint.operation("clear")
      .test()
      .expectNextMatches { response ->
        !response.success &&
          response.message.contains("Error clearing data") &&
          response.message.contains(errorMessage)
      }
      .verifyComplete()
  }

  @Test
  fun `should handle null error message when clear fails`() {
    // given
    whenever(dataSimulator.clearData()).thenReturn(Mono.error(RuntimeException()))

    // when
    simulatorEndpoint.operation("clear")
      .test()
      .expectNextMatches { response ->
        !response.success && response.message.contains("Error clearing data")
      }
      .verifyComplete()
  }

  @Test
  fun `should return error response for unknown action`() {
    // given
    // when
    simulatorEndpoint.operation("unknown")
      .test()
      .expectNextMatches { response ->
        !response.success &&
          response.message.contains("Unknown action") &&
          response.message.contains("unknown")
      }
      .verifyComplete()
  }
}
