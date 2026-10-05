package box.tapsi.libs.utilities.security.token.services

/**
 * Service for creating and parsing signed JSON Web Tokens (JWTs).
 *
 * Tokens are signed with HMAC-SHA and optionally compress their payload using DEFLATE
 * when the configured size threshold is exceeded (see
 * [box.tapsi.libs.utilities.security.SecurityProperties.Token.Compression]).
 * Compression is fully transparent to callers: [parseJwt] automatically detects and
 * decompresses the payload regardless of how the token was originally created.
 */
interface TokenService {

  /**
   * Creates a signed JWT embedding [valueObject] as a named claim.
   *
   * If compression is enabled and the serialized [valueObject] exceeds the configured
   * threshold, the payload is DEFLATE-compressed before being stored in the claim.
   * When compression yields no size reduction the payload is stored uncompressed.
   *
   * @param expiryDurationInSeconds Lifetime of the token in seconds from the current time.
   * @param subject Optional JWT `sub` claim. Pass `null` to omit it.
   * @param key The claim name under which [valueObject] is stored in the JWT payload.
   * @param valueObject The value to embed. Must be serializable by the configured Jackson
   *   [tools.jackson.databind.ObjectMapper].
   * @return A compact, URL-safe signed JWT string.
   */
  fun createJwt(expiryDurationInSeconds: Long, subject: String?, key: String, valueObject: Any): String

  /**
   * Parses and verifies a signed JWT, returning the value stored under [key] deserialized
   * as [clazz].
   *
   * Automatically detects whether the stored claim was compressed and decompresses it
   * transparently before deserialization, so callers do not need to know how the token
   * was originally created.
   *
   * @param token The compact JWT string to parse.
   * @param key The claim name to extract from the JWT payload.
   * @param clazz The target type to deserialize the claim value into.
   * @return The deserialized claim value.
   * @throws box.tapsi.libs.utilities.security.token.TokenException.InvalidTokenException
   *   if the token is expired, has an invalid signature, is malformed, or the claim cannot
   *   be deserialized into [clazz].
   */
  fun <T> parseJwt(token: String, key: String, clazz: Class<T>): T
}
