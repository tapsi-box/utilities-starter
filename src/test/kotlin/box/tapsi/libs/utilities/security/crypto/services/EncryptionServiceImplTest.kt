package box.tapsi.libs.utilities.security.crypto.services

import box.tapsi.libs.utilities.security.SecurityProperties
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.security.crypto.encrypt.Encryptors
import org.springframework.security.crypto.keygen.KeyGenerators
import org.springframework.security.crypto.keygen.StringKeyGenerator
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class EncryptionServiceImplTest {
  private lateinit var serviceImpl: EncryptionServiceImpl

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private lateinit var securityProperties: SecurityProperties

  private lateinit var stringKeyGenerator: StringKeyGenerator

  private val key = "key"

  @BeforeEach
  fun init() {
    MockitoAnnotations.openMocks(this)
    stringKeyGenerator = KeyGenerators.string()
    Mockito.`when`(securityProperties.crypto.key).thenReturn(key)
    Mockito.`when`(securityProperties.crypto.legacyCbcDecryptionEnabled).thenReturn(true)
    serviceImpl = EncryptionServiceImpl(securityProperties, stringKeyGenerator)
  }

  @Test
  fun `should encrypt and decrypt text`() {
    // given
    val plainText = "plainText"
    val salt = serviceImpl.generateSalt()

    // when
    val encryptedText = serviceImpl.encrypt(plainText, salt)
    val decryptedText = serviceImpl.decrypt(encryptedText, salt)

    // verify
    assertEquals(plainText, decryptedText)
  }

  @Test
  fun `should reject tampered ciphertext`() {
    // given
    Mockito.`when`(securityProperties.crypto.legacyCbcDecryptionEnabled).thenReturn(false)
    val salt = serviceImpl.generateSalt()
    val encryptedText = serviceImpl.encrypt("plainText", salt)
    val lastChar = encryptedText.last()
    val tamperedText = encryptedText.dropLast(1) + (if (lastChar == '0') '1' else '0')

    // verify
    assertFailsWith<IllegalStateException> {
      serviceImpl.decrypt(tamperedText, salt)
    }
  }

  @Test
  fun `should not decrypt with a different salt`() {
    // given
    Mockito.`when`(securityProperties.crypto.legacyCbcDecryptionEnabled).thenReturn(false)
    val encryptedText = serviceImpl.encrypt("plainText", serviceImpl.generateSalt())

    // verify
    assertFailsWith<IllegalStateException> {
      serviceImpl.decrypt(encryptedText, serviceImpl.generateSalt())
    }
  }

  @Test
  fun `should encrypt non-ASCII text`() {
    // given
    val plainText = "سلام ۱۲۳"
    val salt = serviceImpl.generateSalt()

    // when
    val decryptedText = serviceImpl.decrypt(serviceImpl.encrypt(plainText, salt), salt)

    // verify
    assertEquals(plainText, decryptedText)
  }

  @Test
  fun `should decrypt text encrypted in the legacy AES-CBC format`() {
    // given
    val plainText = "plainText"
    val salt = serviceImpl.generateSalt()
    val legacyEncryptedText = encryptLegacy(plainText, salt)

    // when
    val decryptedText = serviceImpl.decrypt(legacyEncryptedText, salt)

    // verify
    assertEquals(plainText, decryptedText)
  }

  @Test
  fun `should not decrypt legacy AES-CBC text when legacy decryption is disabled`() {
    // given
    Mockito.`when`(securityProperties.crypto.legacyCbcDecryptionEnabled).thenReturn(false)
    val salt = serviceImpl.generateSalt()
    val legacyEncryptedText = encryptLegacy("plainText", salt)

    // verify
    assertFailsWith<IllegalStateException> {
      serviceImpl.decrypt(legacyEncryptedText, salt)
    }
  }

  @Test
  fun `should generate salt`() {
    // given
    val salt = serviceImpl.generateSalt()

    // when

    // verify
    assert(salt.isNotEmpty())
  }

  @Suppress("DEPRECATION")
  private fun encryptLegacy(plainText: String, salt: String): String = Encryptors.text(key, salt).encrypt(plainText)
}
