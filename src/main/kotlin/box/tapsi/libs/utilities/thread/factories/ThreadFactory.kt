package box.tapsi.libs.utilities.thread.factories

import java.util.concurrent.ThreadFactory as JavaThreadFactory

class ThreadFactory(private val prefix: String) : JavaThreadFactory {
  private var counter = 0

  override fun newThread(r: Runnable): Thread {
    counter++
    return Thread(r, "$prefix-$counter")
  }
}
