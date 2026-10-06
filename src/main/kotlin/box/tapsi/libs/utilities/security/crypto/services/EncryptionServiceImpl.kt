package box.tapsi.libs.utilities.security.crypto.services

import box.tapsi.libs.utilities.security.SecurityProperties
import org.springframework.security.crypto.codec.Hex
import org.springframework.security.crypto.encrypt.AesCbcBytesEncryptor
import org.springframework.security.crypto.encrypt.AesGcmBytesEncryptor
import org.springframework.security.crypto.keygen.StringKeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * Default implementation of [EncryptionService].
 *
 * Encrypts with 256-bit AES-GCM ([AesGcmBytesEncryptor]) and hex-encodes the result. The AES key is
 * derived from [SecurityProperties.Crypto.key] and the hex-encoded salt with PBKDF2-HMAC-SHA256
 * (1,024 iterations).
 *
 * The iteration count is low on purpose: the service derives a key for each call, and the
 * Spring Security default (600,000 iterations) costs about 200 ms of CPU for each call. A low
 * count is safe only when the configured key is a long random secret, not a human password.
 *
 * Versions before 1.0.0 encrypted with AES-CBC (the deprecated `Encryptors.text`). When
 * [SecurityProperties.Crypto.legacyCbcDecryptionEnabled] is `true`, [decrypt] reads values in
 * that format if AES-GCM decryption fails.
 */
class EncryptionServiceImpl(
  private val securityProperties: SecurityProperties,
  private val stringKeyGenerator: StringKeyGenerator,
) : EncryptionService {
  override fun encrypt(plainText: String, salt: String): String {
    val encrypted = createGcmEncryptor(salt).encrypt(plainText.toByteArray(Charsets.UTF_8))
    return String(Hex.encode(encrypted))
  }

  override fun decrypt(cipherText: String, salt: String): String {
    val encrypted = Hex.decode(cipherText)
    val decrypted = try {
      createGcmEncryptor(salt).decrypt(encrypted)
    } catch (e: IllegalStateException) {
      decryptLegacyOrThrow(encrypted, salt, e)
    } catch (e: IllegalArgumentException) {
      decryptLegacyOrThrow(encrypted, salt, e)
    }
    return String(decrypted, Charsets.UTF_8)
  }

  override fun generateSalt(): String = stringKeyGenerator.generateKey()

  private fun createGcmEncryptor(salt: String): AesGcmBytesEncryptor =
    AesGcmBytesEncryptor.withSecretKey(deriveKey(GCM_KEY_ALGORITHM, salt, GCM_KEY_ITERATIONS)).build()

  private fun decryptLegacyOrThrow(encrypted: ByteArray, salt: String, cause: RuntimeException): ByteArray {
    if (!securityProperties.crypto.legacyCbcDecryptionEnabled) throw cause
    // Same key derivation and format as the deprecated Encryptors.text: AES-CBC with a 16-byte IV prefix.
    val legacyKey = deriveKey(LEGACY_KEY_ALGORITHM, salt, LEGACY_KEY_ITERATIONS)
    return AesCbcBytesEncryptor.withSecretKey(legacyKey).build().decrypt(encrypted)
  }

  private fun deriveKey(algorithm: String, salt: String, iterations: Int): SecretKey {
    val password = securityProperties.crypto.key.toCharArray()
    val spec = PBEKeySpec(password, Hex.decode(salt), iterations, KEY_LENGTH_BITS)
    val derived = SecretKeyFactory.getInstance(algorithm).generateSecret(spec)
    return SecretKeySpec(derived.encoded, "AES")
  }

  private companion object {
    const val KEY_LENGTH_BITS = 256
    const val GCM_KEY_ALGORITHM = "PBKDF2WithHmacSHA256"
    const val GCM_KEY_ITERATIONS = 1024
    const val LEGACY_KEY_ALGORITHM = "PBKDF2WithHmacSHA1"
    const val LEGACY_KEY_ITERATIONS = 1024
  }
}
