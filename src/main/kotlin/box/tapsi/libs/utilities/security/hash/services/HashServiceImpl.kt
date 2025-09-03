package box.tapsi.libs.utilities.security.hash.services

import box.tapsi.libs.utilities.security.hash.HashAlgorithm
import org.springframework.stereotype.Service
import java.security.MessageDigest

@Service
class HashServiceImpl : HashService {
  override fun hash(input: String, algorithm: HashAlgorithm): String = MessageDigest.getInstance(algorithm.value)
    .digest(input.toByteArray())
    .toHexString()

  private fun ByteArray.toHexString(): String = joinToString("") { "%02x".format(it) }
}
