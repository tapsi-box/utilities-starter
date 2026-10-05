package box.tapsi.libs.utilities.fixture

import box.tapsi.libs.utilities.fixture.introspectors.InstantArbitraryIntrospector
import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.api.generator.DefaultNullInjectGenerator
import com.navercorp.fixturemonkey.api.generator.NullInjectGenerator
import com.navercorp.fixturemonkey.api.random.Randoms
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import java.time.Instant

/**
 * Test-data helpers based on [Fixture Monkey](https://naver.github.io/fixture-monkey/).
 *
 * The default [FixtureMonkey] uses the Kotlin plugin, never generates `null` values, and
 * generates [Instant] values with [InstantArbitraryIntrospector].
 *
 * Example:
 * ```kotlin
 * val fixture = FixtureHelper.getDefaultFixture()
 * val user = fixture.giveMeOne<User>()
 * val admin = fixture.giveMeBuilder<User>().setExp(User::role, Role.ADMIN).sample()
 * ```
 */
object FixtureHelper {
  private val fixture: FixtureMonkey = createFixture()

  /** Returns the shared default [FixtureMonkey]. */
  fun getDefaultFixture(): FixtureMonkey = fixture

  /** Creates a new [FixtureMonkey] with the default configuration. Use it when you need a separate instance. */
  fun createFixture(): FixtureMonkey = FixtureMonkey.builder()
    .plugin(KotlinPlugin())
    .defaultNullInjectGenerator(NullInjectGenerator { DefaultNullInjectGenerator.NOT_NULL_INJECT })
    .pushExactTypeArbitraryIntrospector(Instant::class.java, InstantArbitraryIntrospector())
    .build()

  /** Returns a random [Instant] between the epoch and [before] (exclusive). */
  fun instantBefore(before: Instant): Instant = instantBetween(Instant.EPOCH, before)

  /** Returns a random [Instant] between [after] and the maximum epoch millisecond (exclusive). */
  fun instantAfter(after: Instant): Instant = instantBetween(after, Instant.ofEpochMilli(Long.MAX_VALUE))

  /** Returns a random [Instant] between [start] (inclusive) and [end] (exclusive). */
  fun instantBetween(start: Instant, end: Instant): Instant =
    Instant.ofEpochMilli(Randoms.current().nextLong(start.toEpochMilli(), end.toEpochMilli()))
}
