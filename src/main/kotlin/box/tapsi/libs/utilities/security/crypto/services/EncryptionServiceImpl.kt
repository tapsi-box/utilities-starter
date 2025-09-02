package box.tapsi.libs.utilities.security.crypto.services

import box.tapsi.libs.utilities.security.SecurityProperties
import org.springframework.security.crypto.encrypt.Encryptors
import org.springframework.security.crypto.encrypt.TextEncryptor
import org.springframework.security.crypto.keygen.StringKeyGenerator
import org.springframework.stereotype.Service

@Service
class EncryptionServiceImpl(
  private val securityProperties: SecurityProperties,
  private val stringKeyGenerator: StringKeyGenerator,
) : EncryptionService {
  override fun encrypt(plainText: String, salt: String): String = createEncryptors(salt).encrypt(plainText)

  override fun decrypt(cipherText: String, salt: String): String = createEncryptors(salt).decrypt(cipherText)

  override fun generateSalt(): String = stringKeyGenerator.generateKey()

  private fun createEncryptors(salt: String): TextEncryptor = Encryptors.text(
    securityProperties.crypto.key,
    salt,
  )
}
