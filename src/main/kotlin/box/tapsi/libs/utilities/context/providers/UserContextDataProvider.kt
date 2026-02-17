package box.tapsi.libs.utilities.context.providers

import box.tapsi.libs.utilities.context.ContextProperties
import box.tapsi.libs.utilities.grpc.XAgentHeaderInfo
import box.tapsi.libs.utilities.localization.Language
import box.tapsi.libs.utilities.user.User
import io.grpc.Context
import io.grpc.Metadata

class UserContextDataProvider(
  private val properties: ContextProperties,
) : AbstractContextDataProvider<User>() {
  val requestHeaderUserIdKey: Metadata.Key<String> =
    Metadata.Key.of(properties.grpcHeaderKeys.userIdKey, METADATA_MARSHALLER)

  val requestHeaderUserIdGlobalKey: Metadata.Key<String> =
    Metadata.Key.of(properties.grpcHeaderKeys.userGlobalIdKey, METADATA_MARSHALLER)

  val requestHeaderUserRoleKey: Metadata.Key<String> =
    Metadata.Key.of(properties.grpcHeaderKeys.userRoleKey, METADATA_MARSHALLER)

  val requestHeaderUserLanguageKey: Metadata.Key<String> =
    Metadata.Key.of(properties.grpcHeaderKeys.userLanguageKey, METADATA_MARSHALLER)

  val requestHeaderXAgentKey: Metadata.Key<String> =
    Metadata.Key.of(properties.grpcHeaderKeys.xAgentKey, METADATA_MARSHALLER)

  val requestHeaderUserIpKey: Metadata.Key<String> =
    Metadata.Key.of(properties.grpcHeaderKeys.userIpKey, METADATA_MARSHALLER)

  val userGrpcContextKey: Context.Key<User> = Context.key(properties.reactorContextKeys.userKey)

  override fun getProviderName(): String = properties.userProviderName

  override fun getGrpcContextKey(): Context.Key<User> = userGrpcContextKey

  override fun getReactorContextKey(): String = properties.reactorContextKeys.userKey

  override fun getDataFromHeaders(headers: Metadata?): User? {
    val userId = findUserIdFromHeaders(headers)
    val userRole = findRoleFromHeaders(headers)
    val language = findLanguageFromHeaders(headers)
    val xAgent = findXAgentInfoFromHeaders(headers)
    val userIp = findIpFromHeaders(headers)
    val globalUserId = findGlobalUserIdFromHeaders(headers) ?: ""
    return if (userId == null || userRole == null) {
      null
    } else {
      User(userId, userRole, language, xAgent, userIp, globalUserId)
    }
  }

  private fun findXAgentInfoFromHeaders(headers: Metadata?): XAgentHeaderInfo? {
    val xAgent = headers?.get(requestHeaderXAgentKey)?.let {
      XAgentHeaderInfo.fromString(it)
    }
    return xAgent
  }

  private fun findUserIdFromHeaders(headers: Metadata?): String? = headers?.get(requestHeaderUserIdKey)

  private fun findGlobalUserIdFromHeaders(headers: Metadata?): String? = headers?.get(requestHeaderUserIdGlobalKey)

  private fun findLanguageFromHeaders(headers: Metadata?): Language {
    val userLanguage = headers?.get(requestHeaderUserLanguageKey)
    return userLanguage?.let { Language.findByCultureCode(it) } ?: Language.default
  }

  private fun findRoleFromHeaders(headers: Metadata?): User.Role? {
    val userRoleName = headers?.get(requestHeaderUserRoleKey)
    return userRoleName?.let { User.Role.findByRoleName(roleName = it) }
  }

  private fun findIpFromHeaders(
    headers: Metadata?,
  ): String? = headers?.get(requestHeaderUserIpKey)?.let { it.split(",")[0] }

  companion object {
    private val METADATA_MARSHALLER: Metadata.AsciiMarshaller<String> = Metadata.ASCII_STRING_MARSHALLER
  }
}
