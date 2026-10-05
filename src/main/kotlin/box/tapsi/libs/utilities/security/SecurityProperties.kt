package box.tapsi.libs.utilities.security

import org.springframework.boot.context.properties.ConfigurationProperties
import java.util.zip.Deflater

/**
 * Configuration properties for security-related features.
 *
 * Bound to the prefix `box.libs.utilities.security` in Spring Boot application properties.
 *
 * Example YAML:
 * ```yaml
 * box:
 *   libs:
 *     utilities:
 *       security:
 *         token:
 *           jwt:
 *             secret-key: "<base64-encoded-256-bit-key>"
 *           compression:
 *             enabled: true
 *             threshold-bytes: 512
 *             level: 6
 *         crypto:
 *           key: "<encryption-key>"
 * ```
 */
@ConfigurationProperties("box.libs.utilities.security")
data class SecurityProperties(val crypto: Crypto = Crypto(), val token: Token = Token()) {
  /**
   * Properties governing JWT token creation and parsing.
   *
   * @property jwt JWT signing configuration.
   * @property compression Payload compression settings.
   */
  data class Token(val jwt: Jwt = Jwt(), val compression: Compression = Compression()) {
    /**
     * JWT signing configuration.
     *
     * @property secretKey Base64-encoded HMAC-SHA key used to sign and verify tokens.
     *   Must decode to at least 256 bits (32 bytes). **Must be overridden via external
     *   configuration — the default value `"CHANGE_ME"` will cause startup failures.**
     */
    data class Jwt(val secretKey: String = "CHANGE_ME")

    /**
     * Configuration for automatic payload compression within JWT claims.
     *
     * When enabled, payloads whose serialized size exceeds [thresholdBytes] are
     * DEFLATE-compressed and Base64-encoded before being stored in the JWT claim.
     * A meta-byte is prepended to the compressed data so that [TokenService.parseJwt]
     * can transparently detect and decompress it — callers never need to know whether
     * a token was compressed. Payloads that fall below the threshold, or where compression
     * yields no size reduction, are stored uncompressed, preserving backward compatibility.
     *
     * @property enabled Whether automatic compression is active. When `false`, all payloads
     *   are stored as-is regardless of size. Defaults to `false`.
     * @property thresholdBytes Minimum serialized payload size in bytes before compression
     *   is attempted. Payloads smaller than this value are stored uncompressed to avoid
     *   the overhead of compressing data too short to benefit from it. Defaults to `512`.
     * @property level DEFLATE compression level passed to [java.util.zip.Deflater].
     *   `-1` (default) uses [java.util.zip.Deflater.DEFAULT_COMPRESSION]; `0` disables
     *   compression at the algorithm level; `1`–`9` trade speed for output size, where
     *   `1` is fastest and `9` produces the smallest output. Defaults to `-1`.
     */
    data class Compression(
      val enabled: Boolean = false,
      val thresholdBytes: Int = 512,
      val level: Int = Deflater.DEFAULT_COMPRESSION,
    )
  }

  /**
   * Symmetric encryption key configuration.
   *
   * @property key The encryption key used by the crypto service. **Must be overridden via
   *   external configuration — the default value `"CHANGE_ME"` is not secure.**
   */
  data class Crypto(val key: String = "CHANGE_ME")
}
