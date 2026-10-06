package box.tapsi.libs.utilities.security.token.services

import box.tapsi.libs.utilities.security.SecurityProperties
import box.tapsi.libs.utilities.security.token.TokenException
import box.tapsi.libs.utilities.time.TimeOperator
import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import org.slf4j.Logger
import tools.jackson.module.kotlin.jacksonObjectMapper
import java.time.Instant
import java.util.Base64
import java.util.concurrent.TimeUnit
import java.util.zip.Deflater
import javax.crypto.Mac
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
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

    whenever(timeOperator.addToCurrentTime(offset = 60L, timeUnit = TimeUnit.SECONDS))
      .thenReturn(now.plusSeconds(60))

    tokenService = buildTokenService()
  }

  @Test
  fun `should create and parse token with small payload`() {
    // given
    val payload = TestPayload(userId = "user-1", amount = 42000)

    // when
    val token = tokenService.createJwt(60L, testSubject, testKey, payload)
    val parsed = tokenService.parseJwt(token, testKey, TestPayload::class.java)

    // verify
    assertEquals(payload, parsed)
  }

  @Test
  fun `should create and parse token with large payload`() {
    // given
    val largePayload = TestPayload(userId = "u".repeat(500), amount = 99999)

    // when
    val token = tokenService.createJwt(60L, testSubject, testKey, largePayload)
    val parsed = tokenService.parseJwt(token, testKey, TestPayload::class.java)

    // verify
    assertEquals(largePayload, parsed)
  }

  @Test
  fun `compressed token should be smaller than uncompressed for large payloads`() {
    // given
    val largePayload = TestPayload(userId = "u".repeat(500), amount = 99999)
    val serviceWithNoCompression = buildTokenService(compressionEnabled = false)

    // when
    val uncompressed = serviceWithNoCompression.createJwt(60L, testSubject, testKey, largePayload)
    val compressed = tokenService.createJwt(60L, testSubject, testKey, largePayload)

    // verify
    assertTrue(compressed.length < uncompressed.length, "Compressed token should be shorter than uncompressed")
  }

  @Test
  fun `should not compress when compression is disabled`() {
    // given
    val serviceWithNoCompression = buildTokenService(compressionEnabled = false)
    val largePayload = TestPayload(userId = "u".repeat(500), amount = 99999)

    // when
    val token = serviceWithNoCompression.createJwt(60L, testSubject, testKey, largePayload)
    val parsed = serviceWithNoCompression.parseJwt(token, testKey, TestPayload::class.java)

    // verify
    assertEquals(largePayload, parsed)
  }

  @Test
  fun `should reject token with duplicate claim names`() {
    // given
    val header = encodeSegment("""{"alg":"HS256"}""")
    val first = """"$testKey":{"userId":"a","amount":1}"""
    val second = """"$testKey":{"userId":"b","amount":2}"""
    val claims = encodeSegment("{$first,$second}")
    val unsignedToken = "$header.$claims"
    val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(securityProperties.token.jwt.secretKey))
    val signature = Mac.getInstance("HmacSHA256")
      .apply { init(secretKey) }
      .doFinal(unsignedToken.toByteArray())
    val token = "$unsignedToken.${Base64.getUrlEncoder().withoutPadding().encodeToString(signature)}"

    // verify
    assertFailsWith<TokenException.InvalidTokenException> {
      tokenService.parseJwt(token, testKey, TestPayload::class.java)
    }
  }

  private fun encodeSegment(json: String): String {
    val encoder = Base64.getUrlEncoder().withoutPadding()
    return encoder.encodeToString(json.toByteArray())
  }

  private fun buildTokenService(
    compressionEnabled: Boolean = true,
    thresholdBytes: Int = 512,
    compressionLevel: Int = Deflater.DEFAULT_COMPRESSION,
  ): TokenServiceImpl {
    whenever(securityProperties.token.compression.enabled).thenReturn(compressionEnabled)
    whenever(securityProperties.token.compression.thresholdBytes).thenReturn(thresholdBytes)
    whenever(securityProperties.token.compression.level).thenReturn(compressionLevel)
    return TokenServiceImpl(timeOperator, logger, securityProperties, objectMapper)
  }

  data class TestPayload(val userId: String, val amount: Int)
}
