package box.tapsi.libs.utilities.reactor

import box.tapsi.libs.utilities.fixture.FixtureHelper
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.kotlin.test.test

class ReactorContextExtensionsKtTest {
  data class TestObject(val name: String)

  private val fixture = FixtureHelper.getDefaultFixture()

  @Test
  fun `monoWithObject should return Mono with object from context`() {
    // given
    val testObject = fixture.giveMeOne<TestObject>()

    // when

    // verify

    monoDeferWithObject(TestObject::class) { test ->
      Mono.just(test.name)
    }
      .withContextualObject(testObject)
      .test()
      .expectNext(testObject.name)
      .verifyComplete()
  }

  @Test
  fun `fluxWithObject should return Flux with object from context`() {
    // given
    val testObject = fixture.giveMeOne<TestObject>()

    // when

    // verify

    fluxDeferWithObject(TestObject::class) { test ->
      Mono.just(test.name)
    }.withContextualObject(testObject)
      .test()
      .expectNext(testObject.name)
      .verifyComplete()
  }
}
