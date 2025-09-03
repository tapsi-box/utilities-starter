package box.tapsi.libs.utilities

open class TapsiException(message: String?) : Exception(message) {
  open fun getErrorCodeString(): ErrorCodeString = ErrorCodeString.UnknownError
}

interface ErrorCodeString {
  val codeString: String

  companion object {
    val UnknownError = object : ErrorCodeString {
      override val codeString: String
        get() = "UNKNOWN_ERROR"
    }
  }
}
