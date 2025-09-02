package box.tapsi.libs.utilities.format.generators

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations

class RandomCodeGeneratorTest {
  @InjectMocks
  private lateinit var generator: RandomCodeGenerator

  @BeforeEach
  fun init() {
    MockitoAnnotations.openMocks(this)
  }

  @Test
  fun `should generate code with default prefix when prefix is null`() {
    // given

    // when
    val result = generator.generate(length = 6, prefix = null)

    // verify
    assertTrue(result.startsWith("TP30-"))
    assertEquals(11, result.length) // "TP30-" (5 chars) + 6 digits
    assertTrue(result.substring(5).matches(Regex("^\\d{6}$")))
  }

  @Test
  fun `should generate code with custom prefix`() {
    // given

    // when
    val result = generator.generate(length = 4, prefix = "TEST")

    // verify
    assertTrue(result.startsWith("TEST-"))
    assertEquals(9, result.length) // "TEST-" (5 chars) + 4 digits
    assertTrue(result.substring(5).matches(Regex("^\\d{4}$")))
  }

  @Test
  fun `should remove extra hyphen from prefix`() {
    // given

    // when
    val result = generator.generate(length = 3, prefix = "ABC-")

    // verify
    assertTrue(result.startsWith("ABC-"))
    assertFalse(result.startsWith("ABC--"))
    assertEquals(7, result.length) // "ABC-" (4 chars) + 3 digits
    assertTrue(result.substring(4).matches(Regex("^\\d{3}$")))
  }

  @Test
  fun `should generate code with correct length`() {
    // given

    // when
    val result = generator.generate(length = 8, prefix = "X")

    // verify
    assertTrue(result.startsWith("X-"))
    assertEquals(10, result.length) // "X-" (2 chars) + 8 digits
    assertTrue(result.substring(2).matches(Regex("^\\d{8}$")))
  }

  @Test
  fun `generated numbers should be padded with leading zeros`() {
    // given

    // when
    val result = generator.generate(length = 5, prefix = "TEST")

    // verify
    assertTrue(result.startsWith("TEST-"))
    assertEquals(10, result.length) // "TEST-" (5 chars) + 5 digits
    assertTrue(result.substring(5).matches(Regex("^\\d{5}$")))
  }
}
