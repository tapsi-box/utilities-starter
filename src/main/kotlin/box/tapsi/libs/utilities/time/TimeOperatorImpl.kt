package box.tapsi.libs.utilities.time

import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.util.concurrent.TimeUnit

@Suppress("TooManyFunctions")
@Component
class TimeOperatorImpl : TimeOperator {
  override fun addToCurrentTime(
    offset: Long,
    timeUnit: TimeUnit,
  ): Instant = Instant.now().plusMillis(timeUnit.toMillis(offset))

  override fun getCurrentTime(): Instant = Instant.now()

  override fun addToInstant(
    time: Instant,
    offset: Long,
    timeUnit: TimeUnit,
  ): Instant = time.plusMillis(timeUnit.toMillis(offset))

  override fun subtractFromInstant(
    time: Instant,
    offset: Long,
    timeUnit: TimeUnit,
  ): Instant = time.minusMillis(timeUnit.toMillis(offset))

  override fun isBeforeInstant(
    time: Instant,
    otherTime: Instant,
  ): Boolean = time.isBefore(otherTime)

  override fun getDurationBetween(
    startTime: Instant,
    endTime: Instant,
  ): Duration = Duration.between(startTime, endTime)

  override fun getElapsedTimeFrom(startTime: Instant): Duration = Duration.between(startTime, getCurrentTime())

  override fun getStartOfTheDayTime(
    time: Instant,
    timezone: SupportedTimezone,
  ): Instant = LocalDate.ofInstant(time, timezone.asZoneId())
    .atStartOfDay(timezone.asZoneId())
    .toInstant()

  override fun convertLocalDateToInstant(
    localDate: LocalDate,
    timezone: SupportedTimezone,
  ): Instant = localDate.atStartOfDay(timezone.asZoneId()).toInstant()

  override fun getCurrentLocalDate(timezone: SupportedTimezone): LocalDate = LocalDate.now(timezone.asZoneId())

  override fun getCurrentDayOfWeek(timezone: SupportedTimezone): DayOfWeek = getCurrentLocalDate(timezone).dayOfWeek

  override fun isBeforeCurrentLocalTime(
    time: LocalTime,
    timezone: SupportedTimezone,
  ): Boolean = time.isBefore(getCurrentLocalTime(timezone))

  override fun isAfterCurrentLocalTime(
    time: LocalTime,
    timezone: SupportedTimezone,
  ): Boolean = time.isAfter(getCurrentLocalTime(timezone))

  override fun isAfterInstant(time: Instant, otherTime: Instant): Boolean = time.isAfter(otherTime)

  override fun getCurrentLocalTime(timezone: SupportedTimezone): LocalTime = LocalTime.now(timezone.asZoneId())
}
