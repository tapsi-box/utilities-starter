package box.tapsi.libs.utilities.logging

import org.reactivestreams.Subscription
import org.slf4j.MDC
import reactor.core.CoreSubscriber
import reactor.util.context.Context

class MdcContextLifter<T>(private val coreSubscriber: CoreSubscriber<T>) : CoreSubscriber<T> {

  override fun onNext(t: T) {
    coreSubscriber.currentContext().copyToMdc()
    coreSubscriber.onNext(t)
    MDC.remove(LoggerReactorContextKeys.TRACE_ID_KEY)
  }

  override fun onSubscribe(subscription: Subscription) {
    coreSubscriber.currentContext().copyToMdc()
    coreSubscriber.onSubscribe(subscription)
    MDC.remove(LoggerReactorContextKeys.TRACE_ID_KEY)
  }

  override fun onComplete() {
    coreSubscriber.currentContext().copyToMdc()
    coreSubscriber.onComplete()
    MDC.remove(LoggerReactorContextKeys.TRACE_ID_KEY)
  }

  override fun onError(throwable: Throwable?) {
    coreSubscriber.currentContext().copyToMdc()
    coreSubscriber.onError(throwable)
    MDC.remove(LoggerReactorContextKeys.TRACE_ID_KEY)
  }

  override fun currentContext(): Context = coreSubscriber.currentContext()

  companion object {
    private fun Context.copyToMdc() {
      if (this.isEmpty) {
        return
      }

      val traceId = this.getOrEmpty<String>(LoggerReactorContextKeys.TRACE_ID_KEY).orElse(null)
      if (traceId != null) {
        MDC.put(LoggerReactorContextKeys.TRACE_ID_KEY, traceId.toString())
      }
    }
  }
}
