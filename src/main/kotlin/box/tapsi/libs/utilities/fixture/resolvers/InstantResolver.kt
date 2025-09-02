package box.tapsi.libs.utilities.fixture.resolvers

import com.appmattus.kotlinfixture.Context
import com.appmattus.kotlinfixture.Unresolved
import com.appmattus.kotlinfixture.resolver.Resolver
import java.time.Instant
import java.util.concurrent.TimeUnit

class InstantResolver : Resolver {
  override fun resolve(context: Context, obj: Any): Any = when (obj) {
    Instant::class -> context.generateJavaUtilInstant()
    else -> Unresolved.Unhandled
  }

  @Suppress("detekt.MagicNumber")
  private fun Context.generateJavaUtilInstant(): Instant = Instant.ofEpochMilli(
    random.nextLong(
      referenceTime.toEpochMilli() - TimeUnit.DAYS.toMillis(3650),
      referenceTime.toEpochMilli() + TimeUnit.DAYS.toMillis(3650),
    ),
  )

  companion object {
    private val referenceTime = Instant.parse("2020-01-01T00:00:00Z")
  }
}
