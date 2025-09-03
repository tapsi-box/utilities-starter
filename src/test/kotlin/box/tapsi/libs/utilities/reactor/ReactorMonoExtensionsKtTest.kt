package box.tapsi.libs.utilities.reactor

import org.junit.jupiter.api.Test
import org.mockito.kotlin.spy
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import reactor.core.publisher.Mono
import reactor.kotlin.test.test
import reactor.kotlin.test.verifyError

class ReactorMonoExtensionsKtTest {
  @Test
  fun `should do nothing on doOnErrorExcept if the exceptionType has been excluded`() {
    // given
    val exception = TestException()
    val spyThrowable = spy<Throwable>()

    // when

    // verify
    Mono.error<Int>(exception)
      .doOnErrorExcept(TestException::class) {
        spyThrowable.initCause(it)
      }.test()
      .verifyError<TestException>()

    verifyNoInteractions(spyThrowable)
  }

  @Test
  fun `should call onError if the exceptionType has not been excluded`() {
    // given
    val exception = TestException()
    val spyThrowable = spy<Throwable>()

    // when

    // verify
    Mono.error<Int>(exception)
      .doOnErrorExcept(RuntimeException::class) {
        spyThrowable.initCause(it)
      }.test()
      .verifyError<TestException>()

    verify(spyThrowable, times(1)).initCause(exception)
  }

  @Test
  fun `should exclude all exceptions if the exceptionType is not provided`() {
    // given
    val exception = TestException()
    val spyThrowable = spy<Throwable>()

    // when

    // verify
    Mono.error<Int>(exception)
      .doOnErrorExcept(Throwable::class) {
        spyThrowable.initCause(it)
      }.test()
      .verifyError<TestException>()

    verifyNoInteractions(spyThrowable)
  }

  private class TestException : Throwable()
}
