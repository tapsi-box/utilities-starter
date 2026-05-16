package box.tapsi.libs.utilities.security.token.services

import box.tapsi.libs.utilities.security.SecurityProperties
import box.tapsi.libs.utilities.time.TimeOperator
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import org.slf4j.Logger
import java.time.Instant
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TokenServiceImplTest {
  private lateinit var tokenService: TokenServiceImpl

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private lateinit var securityProperties: SecurityProperties

  @Mock
  private lateinit var timeOperator: TimeOperator

  @Mock
  private lateinit var logger: Logger

  private val objectMapper = jacksonObjectMapper()
  private val testKey = "previewResponseToken"
  private val testSubject = "TEST_SUBJECT"
  private val now = Instant.now()

  @BeforeEach
  fun init() {
    MockitoAnnotations.openMocks(this)
    whenever(securityProperties.token.jwt.secretKey)
      .thenReturn("c2VjcmV0LWtleS1mb3Itand0LXRva2VuLWdlbmVyYXRpb24td2l0aC0yNTYtYml0cw==")
    whenever(timeOperator.getCurrentTime()).thenReturn(now)
    whenever(timeOperator.addToCurrentTime(offset = 60L, timeUnit = TimeUnit.SECONDS)).thenReturn(now.plusSeconds(60))
    tokenService = TokenServiceImpl(
      timeOperator,
      logger,
      securityProperties,
      objectMapper,
    )
  }

  @Test
  fun `should create and parse uncompressed token`() {
    // given
    val payload = TestPayload(userId = "user-1", amount = 42000)

    // when
    val token = tokenService.createJwt(60L, testSubject, testKey, payload, compress = false)
    val parsed = tokenService.parseJwt(token, testKey, TestPayload::class.java)

    // verify
    assertEquals(payload, parsed)
  }

  @Test
  fun `should create and parse compressed token`() {
    // given
    val payload = TestPayload(userId = "user-1", amount = 42000)

    // when
    val token = tokenService.createJwt(60L, testSubject, testKey, payload, compress = true)
    val parsed = tokenService.parseJwt(token, testKey, TestPayload::class.java)

    // verify
    assertEquals(payload, parsed)
  }

  @Test
  fun `compressed token should be smaller than uncompressed for large payloads`() {
    // given
    val largePayload = TestPayload(userId = "u".repeat(500), amount = 99999)

    // when
    val uncompressed = tokenService.createJwt(60L, testSubject, testKey, largePayload, compress = false)
    val compressed = tokenService.createJwt(60L, testSubject, testKey, largePayload, compress = true)

    // verify
    assertTrue(compressed.length < uncompressed.length, "Compressed token should be shorter than uncompressed")
  }

  @Test
  fun `should parse old uncompressed token (backward compatibility)`() {
    // given
    val payload = TestPayload(userId = "user-old", amount = 12345)

    // when
    val oldToken = tokenService.createJwt(60L, testSubject, testKey, payload, compress = false)
    val parsed = tokenService.parseJwt(oldToken, testKey, TestPayload::class.java)

    // verify
    assertEquals(payload, parsed)
  }

  data class TestPayload(val userId: String, val amount: Int)
}
