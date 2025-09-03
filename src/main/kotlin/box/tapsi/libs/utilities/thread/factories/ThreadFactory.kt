package box.tapsi.libs.utilities.thread.factories

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.ThreadFactory as JavaThreadFactory

class ThreadFactory(private val prefix: String) : JavaThreadFactory {
  private val counter = AtomicInteger(0)

  override fun newThread(r: Runnable): Thread {
    val threadNumber = counter.incrementAndGet()
    return Thread(r, "$prefix-$threadNumber")
  }
}
