package box.tapsi.libs.utilities.context

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("box.libs.utilities.context")
data class ContextProperties(
  val userProviderName: String = "defaultUserProvider",
  val grpcHeaderKeys: GrpcHeaderKeys = GrpcHeaderKeys(),
  val reactorContextKeys: ReactorContextKeys = ReactorContextKeys(),
) {
  data class GrpcHeaderKeys(
    val xAgentKey: String = "x-agent",
    val userLanguageKey: String = "x-agw-user-language",
    val userIpKey: String = "x-forwarded-for",
    val userIdKey: String = "x-agw-user-id",
    val userRoleKey: String = "x-agw-user-role",
    val userGlobalIdKey: String = "x-agw-user-global-id",
  )

  data class ReactorContextKeys(
    val userKey: String = "user",
  )
}
