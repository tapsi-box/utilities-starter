package box.tapsi.libs.utilities.security.token

import box.tapsi.libs.utilities.ErrorCodeString
import box.tapsi.libs.utilities.TapsiException

sealed class TokenException(message: String) : TapsiException(message) {
  class InvalidTokenException private constructor(
    message: String,
    val token: String,
    val userId: String,
  ) : TokenException(message) {
    private var errorCode: ErrorCodeString = ErrorCodeString.UnknownError

    override fun getErrorCodeString(): ErrorCodeString = errorCode

    companion object {
      fun createCorruptedTokenException(
        token: String,
        userId: String = "",
      ): InvalidTokenException = InvalidTokenException("CORRUPTED_TOKEN", token, userId).apply {
        errorCode = ErrorCode.CorruptedToken
      }

      fun createExpiredTokenException(
        token: String,
        userId: String = "",
      ): InvalidTokenException = InvalidTokenException("TOKEN_EXPIRED", token, userId).apply {
        errorCode = ErrorCode.TokenExpired
      }
    }
  }

  enum class ErrorCode(override val codeString: String) : ErrorCodeString {
    CorruptedToken("CORRUPTED_TOKEN"),
    TokenExpired("TOKEN_EXPIRED"),
  }
}
