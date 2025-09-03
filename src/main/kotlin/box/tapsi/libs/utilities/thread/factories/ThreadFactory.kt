package box.tapsi.libs.utilities.thread.factories

import java.util.concurrent.ThreadFactory as JavaThreadFactory
import java.util.concurrent.atomic.AtomicInteger

  private val counter = AtomicInteger(0)

  override fun newThread(r: Runnable): Thread {
    val threadNumber = counter.incrementAndGet()
    return Thread(r, "$prefix-$threadNumber")
  }
}
