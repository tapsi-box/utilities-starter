package box.tapsi.libs.utilities.fixture

import com.navercorp.fixturemonkey.kotlin.giveMeOne
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class FixtureHelperTest {
  private val fixture = FixtureHelper.getDefaultFixture()

  @Test
  fun `should generate objects without null values`() {
    // when
    val samples = List(SAMPLE_COUNT) { fixture.giveMeOne<Sample>() }

    // verify
    samples.forEach {
      assertNotNull(it.nickname)
      assertNotNull(it.tags)
      it.tags.forEach { tag -> assertNotNull(tag) }
    }
  }

  @Test
  fun `should generate instants within ten years of 2020`() {
    // given
    val lowerBound = Instant.parse("2010-01-01T00:00:00Z")
    val upperBound = Instant.parse("2030-01-01T00:00:00Z")

    // when
    val instants = List(SAMPLE_COUNT) { fixture.giveMeOne<Sample>().createdAt }

    // verify
    instants.forEach { assertTrue(it in lowerBound..upperBound, "Unexpected instant $it") }
  }

  @Test
  fun `should generate instants in the requested range`() {
    // given
    val start = Instant.parse("2024-01-01T00:00:00Z")
    val end = Instant.parse("2024-02-01T00:00:00Z")

    // when
    val between = List(SAMPLE_COUNT) { FixtureHelper.instantBetween(start, end) }
    val before = List(SAMPLE_COUNT) { FixtureHelper.instantBefore(start) }
    val after = List(SAMPLE_COUNT) { FixtureHelper.instantAfter(end) }

    // verify
    between.forEach { assertTrue(it >= start && it < end) }
    before.forEach { assertTrue(it < start) }
    after.forEach { assertTrue(it >= end) }
  }

  data class Sample(val name: String, val nickname: String?, val tags: List<String?>?, val createdAt: Instant)

  private companion object {
    const val SAMPLE_COUNT = 50
  }
}
