package box.tapsi.libs.utilities.time

import box.tapsi.libs.utilities.FixtureTestHelper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

class TimeOperatorImplTest {
  @InjectMocks
  private lateinit var timeOperator: TimeOperatorImpl

  private val fixture = FixtureTestHelper.getDefaultFixture()

  @BeforeEach
  fun init() {
    MockitoAnnotations.openMocks(this)
  }

  @Test
  fun `should get duration difference between two time instant successfully`() {
    // given
    val startTime = fixture<Instant>()

    val difference = fixture<Duration> {
      filter<Duration> {
        filter { it.seconds > 1 }
      }
    }

    val endTime = timeOperator.addToInstant(startTime, difference.toSeconds(), TimeUnit.SECONDS)

    // when

    // verify
    assertTrue(timeOperator.getDurationBetween(startTime, endTime) == difference)
  }

  @Test
  fun `should get duration difference between two time instant with different time zone successfully`() {
    // given
    val startTime = ZonedDateTime.parse("2020-03-18T12:30+03:30[Asia/Tehran]").toInstant()

    val endTime = ZonedDateTime.parse("2020-03-22T12:30+04:30[Asia/Tehran]").toInstant()

    // when

    // verify
    assertTrue(
      timeOperator.getDurationBetween(startTime, endTime).seconds ==
        endTime.epochSecond - startTime.epochSecond,
    )
  }

  @Test
  fun `convertLocalDateToInstant should convert LocalDate to Instant correctly`() {
    // given
    val localDate = LocalDate.of(2023, 4, 15)
    val timezone = SupportedTimezone.Tehran

    // when
    val result = timeOperator.convertLocalDateToInstant(localDate, timezone)

    // verify
    val expectedDate = Instant.parse("2023-04-14T20:30:00Z")
    assertEquals(expectedDate, result)
  }

  @Test
  fun `convertLocalDateToInstant should handle different timezones correctly`() {
    // given
    val localDate = LocalDate.of(2023, 4, 15)
    val tehranTimezone = SupportedTimezone.Tehran
    val utcTimezone = SupportedTimezone.UTC

    // when
    val tehranResult = timeOperator.convertLocalDateToInstant(localDate, tehranTimezone)
    val utcResult = timeOperator.convertLocalDateToInstant(localDate, utcTimezone)

    // verify
    assertTrue(tehranResult != utcResult)
  }

  @Test
  fun `getStartOfTheDayTimestamp should return correct start of day for Tehran timezone`() {
    // given
    val time = Instant.parse("2023-04-15T14:30:00Z")
    val timezone = SupportedTimezone.Tehran

    // when
    val result = timeOperator.getStartOfTheDayTime(time, timezone)

    // verify
    val expected = Instant.parse("2023-04-14T20:30:00Z")
    assertEquals(expected, result)
  }

  @Test
  fun `getStartOfTheDayTimestamp should return correct start of day for UTC timezone`() {
    // given
    val time = Instant.parse("2023-04-15T14:30:00Z")
    val timezone = SupportedTimezone.UTC

    // when
    val result = timeOperator.getStartOfTheDayTime(time, timezone)

    // verify
    val expected = Instant.parse("2023-04-15T00:00:00Z")
    assertEquals(expected, result)
  }

  @Test
  fun `getStartOfTheDayTimestamp should return same result for any time within the same day`() {
    // given
    val timezone = SupportedTimezone.Tehran
    val time1 = Instant.parse("2023-04-14T21:30:00Z")
    val time2 = Instant.parse("2023-04-15T14:30:00Z")
    val time3 = Instant.parse("2023-04-15T20:29:59Z")

    // when
    val result1 = timeOperator.getStartOfTheDayTime(time1, timezone)
    val result2 = timeOperator.getStartOfTheDayTime(time2, timezone)
    val result3 = timeOperator.getStartOfTheDayTime(time3, timezone)

    // verify
    val expected = Instant.parse("2023-04-14T20:30:00Z")
    assertEquals(expected, result1)
    assertEquals(expected, result2)
    assertEquals(expected, result3)
  }
}
