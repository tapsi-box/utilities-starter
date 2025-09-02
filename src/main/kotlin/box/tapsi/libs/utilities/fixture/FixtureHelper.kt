package box.tapsi.libs.utilities.fixture

import box.tapsi.libs.utilities.fixture.resolvers.InstantResolver
import com.appmattus.kotlinfixture.Fixture
import com.appmattus.kotlinfixture.config.Generator
import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import com.appmattus.kotlinfixture.decorator.optional.NeverOptionalStrategy
import com.appmattus.kotlinfixture.decorator.optional.optionalStrategy
import com.appmattus.kotlinfixture.kotlinFixture
import java.time.Instant

object FixtureHelper {
  private val fixture: Fixture = kotlinFixture {
    nullabilityStrategy(NeverNullStrategy)
    optionalStrategy(NeverOptionalStrategy)
    resolvers.addFirst(InstantResolver())
  }

  fun getDefaultFixture(): Fixture = fixture

  fun Generator<Instant>.before(
    before: Instant,
  ): Instant = Instant.ofEpochMilli(random.nextLong(0L, before.toEpochMilli()))

  fun Generator<Instant>.after(
    after: Instant,
  ): Instant = Instant.ofEpochMilli(random.nextLong(after.toEpochMilli(), Long.MAX_VALUE))

  fun Generator<Instant>.between(
    start: Instant,
    end: Instant,
  ): Instant = Instant.ofEpochMilli(random.nextLong(start.toEpochMilli(), end.toEpochMilli()))
}
