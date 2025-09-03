package box.tapsi.libs.utilities

import box.tapsi.libs.utilities.fixture.resolvers.InstantResolver
import com.appmattus.kotlinfixture.Fixture
import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.appmattus.kotlinfixture.decorator.nullability.nullabilityStrategy
import com.appmattus.kotlinfixture.decorator.optional.NeverOptionalStrategy
import com.appmattus.kotlinfixture.decorator.optional.optionalStrategy
import com.appmattus.kotlinfixture.kotlinFixture

object FixtureTestHelper {
  fun getDefaultFixture(): Fixture = kotlinFixture {
    nullabilityStrategy(NeverNullStrategy)
    optionalStrategy(NeverOptionalStrategy)
    resolvers.addFirst(InstantResolver())
  }
}
