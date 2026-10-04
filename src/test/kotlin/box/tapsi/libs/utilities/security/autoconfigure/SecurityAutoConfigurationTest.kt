package box.tapsi.libs.utilities.security.autoconfigure

import box.tapsi.libs.utilities.autoconfigure.UtilitiesAutoConfiguration
import box.tapsi.libs.utilities.logging.autoconfiguration.LoggingAutoConfiguration
import box.tapsi.libs.utilities.security.token.services.TokenService
import box.tapsi.libs.utilities.time.autoconfigure.TimeAutoConfiguration
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SecurityAutoConfigurationTest {
  private val contextRunner = ApplicationContextRunner()
    .withConfiguration(
      AutoConfigurations.of(
        UtilitiesAutoConfiguration::class.java,
        LoggingAutoConfiguration::class.java,
        TimeAutoConfiguration::class.java,
        SecurityAutoConfiguration::class.java,
      ),
    )
    .withPropertyValues(
      "box.libs.utilities.security.token.jwt.secret-key=" +
        "c2VjcmV0LWtleS1mb3Itand0LXRva2VuLWdlbmVyYXRpb24td2l0aC0yNTYtYml0cw==",
      "box.libs.utilities.security.token.compression.enabled=true",
      "box.libs.utilities.security.token.compression.threshold-bytes=16",
    )

  @Test
  fun `should create token service with the Spring Boot Jackson 3 mapper`() {
    contextRunner
      .withConfiguration(AutoConfigurations.of(JacksonAutoConfiguration::class.java))
      .run { context ->
        // given
        val tokenService = context.getBean(TokenService::class.java)
        val payload = TestPayload(userId = "u".repeat(100), amount = 42)

        // when
        val token = tokenService.createJwt(60L, "TEST_SUBJECT", "payload", payload)
        val parsed = tokenService.parseJwt(token, "payload", TestPayload::class.java)

        // verify
        assertEquals(payload, parsed)
      }
  }

  @Test
  fun `should not create token service without a Jackson mapper`() {
    contextRunner.run { context ->
      // verify
      assertTrue(context.startupFailure == null)
      assertFalse(context.containsBean("tokenService"))
    }
  }

  data class TestPayload(val userId: String, val amount: Int)
}
