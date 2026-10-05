package box.tapsi.libs.utilities.security.token.services

import box.tapsi.libs.utilities.security.SecurityProperties
import box.tapsi.libs.utilities.security.token.TokenException
import box.tapsi.libs.utilities.time.TimeOperator
import box.tapsi.libs.utilities.time.toDate
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import io.micrometer.core.annotation.Timed
import org.slf4j.Logger
import tools.jackson.databind.ObjectMapper
import java.io.ByteArrayOutputStream
import java.util.Base64
import java.util.concurrent.TimeUnit
import java.util.zip.Deflater
import java.util.zip.Inflater
import javax.crypto.SecretKey

/**
 * Default implementation of [TokenService].
 *
 * Signs tokens with HMAC-SHA using the secret key from
 * [box.tapsi.libs.utilities.security.SecurityProperties.Token.Jwt], and optionally applies
 * DEFLATE compression to large payloads as configured by
 * [box.tapsi.libs.utilities.security.SecurityProperties.Token.Compression].
 *
 * Compression is fully transparent: [parseJwt] inspects a meta-byte prepended to the claim
 * value to determine whether to inflate the payload, so callers never need to track how a
 * token was originally created.
 *
 * Annotated with [@Timed][io.micrometer.core.annotation.Timed] to expose latency metrics
 * for [createJwt] and [parseJwt] via Micrometer.
 */
@Timed
open class TokenServiceImpl(
  private val timeOperator: TimeOperator,
  private val logger: Logger,
  securityProperties: SecurityProperties,
  private val objectMapper: ObjectMapper,
) : TokenService {
  private val secretKey: SecretKey =
    Keys.hmacShaKeyFor(Base64.getDecoder().decode(securityProperties.token.jwt.secretKey))
  private val jwtSerializer = JacksonJwtSerializer(objectMapper)
  private val jwtParser = Jwts.parser()
    .json(JacksonJwtDeserializer(objectMapper))
    .verifyWith(secretKey)
    .build()
  private val compressionEnabled: Boolean = securityProperties.token.compression.enabled
  private val compressionThreshold: Int = securityProperties.token.compression.thresholdBytes
  private val compressionLevel: Int = securityProperties.token.compression.level

  override fun createJwt(expiryDurationInSeconds: Long, subject: String?, key: String, valueObject: Any): String {
    val claimValue = compress(valueObject)
    val builder = Jwts.builder()
      .json(jwtSerializer)
      .claim(key, claimValue)
      .issuedAt(timeOperator.getCurrentTime().toDate())
      .expiration(
        timeOperator.addToCurrentTime(
          offset = expiryDurationInSeconds,
          timeUnit = TimeUnit.SECONDS,
        ).toDate(),
      )
      .signWith(secretKey)
    subject?.let { builder.subject(it) }
    return builder.compact()
  }

  override fun <T> parseJwt(token: String, key: String, clazz: Class<T>): T = try {
    val claims = jwtParser.parseSignedClaims(token).payload
    val rawClaim = claims[key]
    val decompressed = decompress(rawClaim)
    objectMapper.convertValue(decompressed, clazz)
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

  // META byte layout: bit 4 → isCompressed flag; bits 0–3 reserved for future key versioning

  @Suppress("ReturnCount")
  private fun compress(value: Any): Any {
    if (!compressionEnabled) return value

    val bytes = objectMapper.writeValueAsBytes(value)
    if (bytes.size <= compressionThreshold) return value

    val deflated = deflate(bytes)

    if (deflated.size >= bytes.size) return value

    val packed = byteArrayOf(COMPRESSION_FLAG.toByte()) + deflated

    return Base64.getEncoder().encodeToString(packed)
  }

  @Suppress("ReturnCount")
  private fun decompress(claimValue: Any?): Any? {
    if (claimValue !is String) return claimValue
    val decoded = try {
      Base64.getDecoder().decode(claimValue)
    } catch (_: IllegalArgumentException) {
      return claimValue
    }
    if (decoded.isEmpty()) return claimValue
    val isCompressed = (decoded[0].toInt() and COMPRESSION_FLAG) != 0
    if (!isCompressed) return claimValue
    val inflated = inflate(decoded.copyOfRange(1, decoded.size))
    return objectMapper.readValue(inflated, Any::class.java)
  }

  private fun deflate(data: ByteArray): ByteArray {
    val deflater = Deflater(compressionLevel).also {
      it.setInput(data)
      it.finish()
    }

    return ByteArrayOutputStream().use { baos ->
      val buf = ByteArray(IO_BUFFER_SIZE)
      while (!deflater.finished()) baos.write(buf, 0, deflater.deflate(buf))
      deflater.end()
      baos.toByteArray()
    }
  }

  private fun inflate(data: ByteArray): ByteArray {
    val inflater = Inflater().also { it.setInput(data) }
    return ByteArrayOutputStream().use { baos ->
      val buf = ByteArray(IO_BUFFER_SIZE)
      while (!inflater.finished()) baos.write(buf, 0, inflater.inflate(buf))
      inflater.end()
      baos.toByteArray()
    }
  }

  private companion object {
    const val COMPRESSION_FLAG = 0x10
    const val IO_BUFFER_SIZE = 4096
  }
}
