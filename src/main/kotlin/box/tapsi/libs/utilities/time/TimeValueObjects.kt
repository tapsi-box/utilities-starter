package box.tapsi.libs.utilities.time

import java.time.ZoneId

enum class SupportedTimezone(private val zoneId: String) {
  Tehran("Asia/Tehran"),
  UTC("UTC"),
  ;

  fun asZoneId(): ZoneId = ZoneId.of(zoneId)
}
