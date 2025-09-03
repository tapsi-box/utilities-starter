package box.tapsi.libs.utilities

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UtilitiesStarterApplication

fun main(args: Array<String>) {
  @Suppress("detekt.SpreadOperator")
  runApplication<UtilitiesStarterApplication>(*args)
}
