package box.tapsi.libs.utilities

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CommonExtensionsKtTest {
  private val fixture = FixtureTestHelper.getDefaultFixture()

  @Test
  fun `toOrdinal should return the correct ordinal number`() {
    // given
    val numbers = mapOf(
      1L to "1st",
      2L to "2nd",
      3L to "3rd",
      4L to "4th",
      5L to "5th",
      6L to "6th",
      7L to "7th",
      8L to "8th",
      9L to "9th",
      10L to "10th",
      11L to "11th",
      12L to "12th",
      13L to "13th",
      14L to "14th",
      15L to "15th",
      16L to "16th",
      17L to "17th",
      18L to "18th",
      19L to "19th",
      20L to "20th",
      21L to "21st",
      22L to "22nd",
      23L to "23rd",
      24L to "24th",
      25L to "25th",
      26L to "26th",
      27L to "27th",
      28L to "28th",
      29L to "29th",
      30L to "30th",
      31L to "31st",
      32L to "32nd",
      33L to "33rd",
      34L to "34th",
      35L to "35th",
      36L to "36th",
      37L to "37th",
      38L to "38th",
      39L to "39th",
      40L to "40th",
      41L to "41st",
      42L to "42nd",
      43L to "43rd",
      44L to "44th",
      45L to "45th",
      46L to "46th",
      47L to "47th",
      48L to "48th",
      49L to "49th",
      50L to "50th",
    )
    // when

    val results = numbers.keys.map(Long::toOrdinal)

    // verify
    assertEquals(numbers.values.toList(), results)
  }

  @Test
  fun `cast should return the same instance when cast to the same type`() {
    // given
    val incentive = fixture<String>()

    // when
    val result = incentive.castOrThrow<String>()

    // verify
    Assertions.assertSame(incentive, result) { "Expected the same instance" }
  }

  @Test
  fun `cast should throw ClassCastException when cast to a different type`() {
    // given
    val incentive = fixture<String>()

    // when
    val exception = Assertions.assertThrows(ClassCastException::class.java) {
      incentive.castOrThrow<Int>()
    }

    // verify
    Assertions.assertEquals("Value cannot be cast to kotlin.Int", exception.message)
  }
}
