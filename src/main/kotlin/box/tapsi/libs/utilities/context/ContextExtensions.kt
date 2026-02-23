package box.tapsi.libs.utilities.context

val ContextProperties.userProviderName: String
  get() = this.dataProviders.filter { (_, value) -> value.isUserProvider }
    .let { filteredKeys ->
      check(filteredKeys.size == 1) {
        "There should be exactly one user provider, but found ${filteredKeys.size}. " +
          "Providers: ${filteredKeys.keys.joinToString(", ")}"
      }
      return@let filteredKeys.keys.first()
    }
