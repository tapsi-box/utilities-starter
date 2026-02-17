package box.tapsi.libs.utilities.localization

enum class Language(val cultureCode: String) {
  Persian("fa-IR"),
  ;

  companion object {
    val default = Persian
    fun findByCultureCode(cultureCode: String): Language = entries.find {
      it.cultureCode == cultureCode
    } ?: default
  }
}
