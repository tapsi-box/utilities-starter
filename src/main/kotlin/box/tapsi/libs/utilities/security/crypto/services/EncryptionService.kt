package box.tapsi.libs.utilities.security.crypto.services

interface EncryptionService {
  fun encrypt(plainText: String, salt: String): String
  fun decrypt(cipherText: String, salt: String): String
  fun generateSalt(): String
}
