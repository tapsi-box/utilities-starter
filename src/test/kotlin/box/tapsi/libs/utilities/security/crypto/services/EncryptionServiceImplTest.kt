package box.tapsi.libs.utilities.security.crypto.services

import box.tapsi.libs.utilities.security.SecurityProperties
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.security.crypto.keygen.KeyGenerators
import org.springframework.security.crypto.keygen.StringKeyGenerator

class EncryptionServiceImplTest {
  private lateinit var serviceImpl: EncryptionServiceImpl

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private lateinit var securityProperties: SecurityProperties

  private lateinit var stringKeyGenerator: StringKeyGenerator

  @BeforeEach
  fun init() {
    MockitoAnnotations.openMocks(this)
    stringKeyGenerator = KeyGenerators.string()
    serviceImpl = EncryptionServiceImpl(securityProperties, stringKeyGenerator)
  }

  @Test
  fun `should encrypt and decrypt text`() {
    // given
    val plainText = "plainText"
    val salt = serviceImpl.generateSalt()
    val key = "key"

    // when
    Mockito.`when`(securityProperties.crypto.key).thenReturn(key)

    // verify
    val encryptedText = serviceImpl.encrypt(plainText, salt)
    val decryptedText = serviceImpl.decrypt(encryptedText, salt)
    assert(plainText == decryptedText)
  }

  @Test
  fun `should generate salt`() {
    // given
    val salt = serviceImpl.generateSalt()

    // when

    // verify
    assert(salt.isNotEmpty())
  }
}
