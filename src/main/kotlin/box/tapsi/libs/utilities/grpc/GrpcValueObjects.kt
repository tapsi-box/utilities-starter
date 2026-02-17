package box.tapsi.libs.utilities.grpc

data class XAgentHeaderInfo(
  val version: String?,
  val product: String?,
  val platform: String?,
  val appVersion: String?,
  val advertisingId: String?,
  val buildVersion: String?,
  val osVersion: String? = null,
) {
  companion object {
    private const val SPLITTING_DELIMITER = "|"
    private const val VERSION_INDEX = 0
    private const val PRODUCT_INDEX = 1
    private const val PLATFORM_INDEX = 2
    private const val APP_VERSION_INDEX = 3
    private const val BUILD_VERSION_INDEX = 4
    private const val OS_VERSION_INDEX = 5
    private const val ADVERTISING_ID_INDEX = 16

    fun fromString(
      xAgent: String,
    ): XAgentHeaderInfo = xAgent.split(SPLITTING_DELIMITER)
      .map { it.takeIf { it.isNotBlank() } }
      .let { split ->
        XAgentHeaderInfo(
          version = split.getOrNull(VERSION_INDEX),
          product = split.getOrNull(PRODUCT_INDEX),
          platform = split.getOrNull(PLATFORM_INDEX),
          appVersion = split.getOrNull(APP_VERSION_INDEX),
          advertisingId = split.getOrNull(ADVERTISING_ID_INDEX),
          buildVersion = split.getOrNull(BUILD_VERSION_INDEX),
          osVersion = split.getOrNull(OS_VERSION_INDEX),
        )
      }
  }
}
