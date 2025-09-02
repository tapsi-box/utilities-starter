package box.tapsi.libs.utilities.security.hash.services

import box.tapsi.libs.utilities.security.hash.HashAlgorithm
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.security.MessageDigest

class HashServiceImplTest {
  private lateinit var serviceImpl: HashServiceImpl

  @BeforeEach
  fun init() {
    serviceImpl = HashServiceImpl()
  }

  @Test
  fun `should hash input with any hash algorithm`() {
    // given
    val input = "testInput"
    val hashAlgorithm = HashAlgorithm.entries.random()
    val expectedHash = MessageDigest
      .getInstance(hashAlgorithm.value)
      .digest(input.toByteArray())
      .joinToString("") { "%02x".format(it) }

    // when

    // verify
    val actualHash = serviceImpl.hash(input, hashAlgorithm)
    assertEquals(actualHash, expectedHash)
  }
}
