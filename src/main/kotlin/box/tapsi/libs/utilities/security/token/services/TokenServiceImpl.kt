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
import java.util.Base64
import java.util.concurrent.TimeUnit
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
  ): String {
    val builder = Jwts.builder()
      .json(JacksonSerializer(objectMapper))
      .claim(key, valueObject)
      .issuedAt(timeOperator.getCurrentTime().toDate())
      .expiration(
        timeOperator.addToCurrentTime(
          offset = expiryDurationInSeconds,
          timeUnit = TimeUnit.SECONDS,
        ).toDate(),
      )
      .signWith(secretKey)
    subject?.let {
      builder.subject(it)
    }
    return builder.compact()
  }

  override fun <T> parseJwt(token: String, key: String, clazz: Class<T>): T = run {
    try {
      val claims = jwtParser
        .parseSignedClaims(token).payload
      return@run objectMapper.convertValue(claims[key], clazz)
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
}
