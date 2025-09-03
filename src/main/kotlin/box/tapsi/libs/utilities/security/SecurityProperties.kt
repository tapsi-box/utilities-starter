package box.tapsi.libs.utilities.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("box.libs.utilities.security")
data class SecurityProperties(
  val crypto: Crypto = Crypto(),
  val token: Token = Token(),
) {
  data class Token(
    val jwt: Jwt = Jwt(),
  ) {
    data class Jwt(
      val secretKey: String = "CHANGE_ME", // Must be set via external configuration
    )
  }

  data class Crypto(
    val key: String = "CHANGE_ME", // Must be set via external configuration
  )
}
