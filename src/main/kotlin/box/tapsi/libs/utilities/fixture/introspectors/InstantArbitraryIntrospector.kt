package box.tapsi.libs.utilities.fixture.introspectors

import com.navercorp.fixturemonkey.api.arbitrary.CombinableArbitrary
import com.navercorp.fixturemonkey.api.generator.ArbitraryGeneratorContext
import com.navercorp.fixturemonkey.api.introspector.ArbitraryIntrospector
import com.navercorp.fixturemonkey.api.introspector.ArbitraryIntrospectorResult
import com.navercorp.fixturemonkey.api.random.Randoms
import java.time.Instant
import java.util.concurrent.TimeUnit
import java.util.function.Supplier

/**
 * Generates [Instant] values within ±10 years of 2020-01-01T00:00:00Z.
 *
 * Uses the Fixture Monkey random source, so a seeded [com.navercorp.fixturemonkey.FixtureMonkey]
 * gives repeatable values.
 */
class InstantArbitraryIntrospector : ArbitraryIntrospector {
  override fun introspect(context: ArbitraryGeneratorContext): ArbitraryIntrospectorResult =
    ArbitraryIntrospectorResult(CombinableArbitrary.from(Supplier { generateInstant() }))

  private fun generateInstant(): Instant = Instant.ofEpochMilli(
    Randoms.current().nextLong(
      referenceTime.toEpochMilli() - TimeUnit.DAYS.toMillis(RANGE_DAYS),
      referenceTime.toEpochMilli() + TimeUnit.DAYS.toMillis(RANGE_DAYS),
    ),
  )

  private companion object {
    const val RANGE_DAYS = 3650L // 10 years
    val referenceTime: Instant = Instant.parse("2020-01-01T00:00:00Z")
  }
}
