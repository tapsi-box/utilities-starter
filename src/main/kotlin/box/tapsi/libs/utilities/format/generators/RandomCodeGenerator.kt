package box.tapsi.libs.utilities.format.generators

import org.springframework.stereotype.Component
import kotlin.math.pow
import kotlin.random.Random

@Component(RandomCodeGenerator.BEAN_NAME)
class RandomCodeGenerator : CodeGenerator {
  override fun generate(length: Int, prefix: String?): String = "${preparePrefix(prefix)}-${generateRandomCode(length)}"

  private fun generateRandomCode(length: Int): String {
    val uniformlyDist = Random.nextDouble()
    val digitLength = 10.toDouble().pow(length.toDouble()).toInt()
    return (uniformlyDist * digitLength).toInt().toString().padStart(length, '0')
  }

  private fun preparePrefix(prefix: String?): String = (prefix ?: DEFAULT_PREFIX).removeSuffix("-")

  companion object {
    private const val DEFAULT_PREFIX = "TP30"
    const val BEAN_NAME = "randomCodeGenerator"
  }
}
