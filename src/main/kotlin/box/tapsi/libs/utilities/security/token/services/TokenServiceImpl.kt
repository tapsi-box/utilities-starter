package box.tapsi.libs.utilities.security.token.services

import box.tapsi.libs.utilities.security.SecurityProperties
import box.tapsi.libs.utilities.security.token.TokenException
import box.tapsi.libs.utilities.time.TimeOperator
import box.tapsi.libs.utilities.time.toDate
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.jackson.io.JacksonSerializer
import io.jsonwebtoken.security.Keys
import io.micrometer.core.annotation.Timed
import org.slf4j.Logger
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.Base64
import java.util.concurrent.TimeUnit
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import javax.crypto.SecretKey

@Timed
open class TokenServiceImpl(
  private val timeOperator: TimeOperator,
  private val logger: Logger,
  securityProperties: SecurityProperties,
  private val objectMapper: ObjectMapper,
) : TokenService {
  private val secretKey: SecretKey =
    Keys.hmacShaKeyFor(Base64.getDecoder().decode(securityProperties.token.jwt.secretKey))
  private val jwtParser = Jwts.parser().verifyWith(secretKey).build()

  override fun createJwt(
    expiryDurationInSeconds: Long,
    subject: String?,
    key: String,
    valueObject: Any,
    compress: Boolean,
  ): String {
    val builder = Jwts.builder()
      .json(JacksonSerializer(objectMapper))
      .issuedAt(timeOperator.getCurrentTime().toDate())
      .expiration(
        timeOperator.addToCurrentTime(
          offset = expiryDurationInSeconds,
          timeUnit = TimeUnit.SECONDS,
        ).toDate(),
      )
      .signWith(secretKey)
    if (compress) {
      val json = objectMapper.writeValueAsBytes(valueObject)
      val compressed = ByteArrayOutputStream().also { out ->
        GZIPOutputStream(out).use { gz -> gz.write(json) }
      }.toByteArray()
      builder
        .claim(COMPRESSED_PAYLOAD_CLAIM, Base64.getEncoder().encodeToString(compressed))
        .claim(COMPRESSION_VERSION_CLAIM, COMPRESSION_VERSION)
    } else {
      builder.claim(key, valueObject)
    }
    subject?.let { builder.subject(it) }
    return builder.compact()
  }

  override fun <T> parseJwt(token: String, key: String, clazz: Class<T>): T {
    try {
      val claims = jwtParser.parseSignedClaims(token).payload
      val version = claims[COMPRESSION_VERSION_CLAIM] as? Int
      if (version != null && version == COMPRESSION_VERSION) {
        val encoded = claims[COMPRESSED_PAYLOAD_CLAIM] as String
        val compressed = Base64.getDecoder().decode(encoded)
        val json = GZIPInputStream(ByteArrayInputStream(compressed)).use { it.readBytes() }
        return objectMapper.readValue(json, clazz)
      }
      return objectMapper.convertValue(claims[key], clazz)
    } catch (e: ExpiredJwtException) {
      logger.error("Expired token: $token", e)
      throw TokenException.InvalidTokenException.createExpiredTokenException(token)
    } catch (e: UnsupportedJwtException) {
      logger.error("Invalid token: $token", e)
      throw TokenException.InvalidTokenException.createCorruptedTokenException(token)
    } catch (e: JwtException) {
      logger.error("Invalid token: $token", e)
      throw TokenException.InvalidTokenException.createCorruptedTokenException(token)
    } catch (e: IllegalArgumentException) {
      logger.error("Invalid token: $token", e)
      throw TokenException.InvalidTokenException.createCorruptedTokenException(token)
    }
  }

  private companion object {
    const val COMPRESSED_PAYLOAD_CLAIM = "cp"
    const val COMPRESSION_VERSION_CLAIM = "cv"
    const val COMPRESSION_VERSION = 1
  }
}
