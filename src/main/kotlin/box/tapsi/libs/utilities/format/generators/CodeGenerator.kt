package box.tapsi.libs.utilities.format.generators

interface CodeGenerator {
  fun generate(length: Int, prefix: String? = null): String
}
