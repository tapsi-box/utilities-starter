package box.tapsi.libs.utilities.time

import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.util.concurrent.TimeUnit

/**
 * Interface for performing various time-related operations such as time manipulation, comparisons,
 * and conversions.
 * Provides utility methods to work with `Instant`, `LocalDate`, `LocalTime`, and `SupportedTimezone`.
 */
@Suppress("TooManyFunctions")
interface TimeOperator {
  /**
   * Adds the specified offset to the current time based on the given time unit.
   *
   * @param offset The amount of time to add to the current time.
   * @param timeUnit The unit of time for the offset (e.g., seconds, minutes, hours).
   * @return The resulting Instant after applying the offset.
   */
  fun addToCurrentTime(offset: Long, timeUnit: TimeUnit): Instant

  /**
   * Retrieves the current system time as an Instant.
   *
   * @return the current time as an Instant object
   */
  fun getCurrentTime(): Instant

  /**
   * Adds a specified offset to the given Instant based on the provided TimeUnit.
   *
   * @param time the initial instant to which the offset will be added
   * @param offset the amount to be added to the instant
   * @param timeUnit the unit of time that specifies the scale of the offset
   * @return a new Instant with the applied offset
   */
  fun addToInstant(time: Instant, offset: Long, timeUnit: TimeUnit): Instant

  /**
   * Subtracts the specified offset from the given instant based on the provided time unit.
   *
   * @param time the initial instant from which the offset will be subtracted
   * @param offset the amount of time to subtract from the instant
   * @param timeUnit the unit of time for the offset (e.g., seconds, minutes, hours)
   * @return a new Instant with the applied subtraction
   */
  fun subtractFromInstant(time: Instant, offset: Long, timeUnit: TimeUnit): Instant

  /**
   * Determines if the given time is before the specified other time.
   *
   * @param time the Instant to be compared
   * @param otherTime the Instant to compare against
   * @return true if time is before otherTime, false otherwise
   */
  fun isBeforeInstant(time: Instant, otherTime: Instant): Boolean

  /**
   * Checks whether the given `time` occurs after the `otherTime`.
   *
   * @param time the time to be compared
   * @param otherTime the time to compare against
   * @return `true` if `time` occurs after `otherTime`, `false` otherwise
   */
  fun isAfterInstant(time: Instant, otherTime: Instant): Boolean

  /**
   * Calculates the duration between two given instants.
   *
   * @param startTime the starting point in time.
   * @param endTime the ending point in time.
   * @return the duration between the startTime and endTime.
   */
  fun getDurationBetween(startTime: Instant, endTime: Instant): Duration

  /**
   * Calculates the elapsed time from the given start time to the current system time.
   *
   * @param startTime The starting point in time from which to calculate the elapsed duration.
   * @return The duration between the current system time and the provided start time.
   */
  fun getElapsedTimeFrom(startTime: Instant): Duration

  /**
   * Returns the start of the day for the given time in the specified timezone.
   *
   * @param time the input time as an Instant
   * @param timezone the timezone for which the start of the day is calculated, defaults to SupportedTimezone.Tehran
   * @return the start of the day as an Instant based on the provided time and timezone
   */
  fun getStartOfTheDayTime(time: Instant, timezone: SupportedTimezone = SupportedTimezone.Tehran): Instant

  /**
   * Converts a given LocalDate to an Instant using the specified timezone.
   *
   * @param localDate the LocalDate object to be converted.
   * @param timezone the timezone to be used for the conversion; defaults to Tehran timezone.
   * @return the corresponding Instant representation of the provided LocalDate in the specified timezone.
   */
  fun convertLocalDateToInstant(localDate: LocalDate, timezone: SupportedTimezone = SupportedTimezone.Tehran): Instant

  /**
   * Retrieves the current local date based on the specified timezone.
   *
   * @param timezone The timezone for which the current local date should be calculated.
   * Defaults to SupportedTimezone.Tehran.
   * @return The current local date in the specified timezone.
   */
  fun getCurrentLocalDate(timezone: SupportedTimezone = SupportedTimezone.Tehran): LocalDate

  /**
   * Retrieves the current day of the week based on the specified timezone.
   *
   * @param timezone the timezone to consider when determining the current day of the week,
   * defaulting to `SupportedTimezone.Tehran`.
   * @return the day of the week as a `DayOfWeek` enum constant.
   */
  fun getCurrentDayOfWeek(timezone: SupportedTimezone = SupportedTimezone.Tehran): DayOfWeek

  /**
   * Determines whether a given time is before the current local time in a specified timezone.
   *
   * @param time the local time to compare.
   * @param timezone the timezone to consider when comparing the time. Defaults to SupportedTimezone.Tehran.
   * @return true if the provided time is before the current local time in the specified timezone, false otherwise.
   */
  fun isBeforeCurrentLocalTime(time: LocalTime, timezone: SupportedTimezone = SupportedTimezone.Tehran): Boolean

  /**
   * Determines whether the given `time` is after the current local time in a specified timezone.
   *
   * @param time The local time to compare.
   * @param timezone The timezone to consider when comparing the time. Defaults to SupportedTimezone.Tehran.
   * @return `true` if the provided time is after the current local time in the specified timezone, `false` otherwise.
   */
  fun isAfterCurrentLocalTime(time: LocalTime, timezone: SupportedTimezone = SupportedTimezone.Tehran): Boolean

  /**
   * Retrieves the current local time for the specified timezone.
   *
   * @param timezone the timezone for which to retrieve the current local time. Defaults to SupportedTimezone.Tehran.
   * @return the current local time based on the specified timezone.
   */
  fun getCurrentLocalTime(timezone: SupportedTimezone = SupportedTimezone.Tehran): LocalTime
}
