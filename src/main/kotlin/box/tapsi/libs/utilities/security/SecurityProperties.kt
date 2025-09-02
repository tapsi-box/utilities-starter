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
      val secretKey: String = "G1/628+ALOHAMORA+e2/71423134669+P3/141592+SEPEHR+F40+63245986+LS1/079e9kph+VIVA+MATH",
    )
  }

  data class Crypto(
    val key: String = "G1/628+ALOHAMORA+e2/71423134669+P3/141592+MAHDI+F40+63245986+LS1/079e9kph+VIVA+MATH",
  )
}
