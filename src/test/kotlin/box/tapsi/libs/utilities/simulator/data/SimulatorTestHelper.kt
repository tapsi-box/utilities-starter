package box.tapsi.libs.utilities.simulator.data

import box.tapsi.libs.utilities.simulator.data.clearable.Clearable
import box.tapsi.libs.utilities.simulator.data.seedable.Seedable
import reactor.core.publisher.Mono

object SimulatorTestHelper {
  object ValidSeedable : Seedable {
    override fun seed(): Mono<Void> = Mono.empty()
  }

  object ValidClearable : Clearable {
    override fun clear(): Mono<Void> = Mono.empty()
  }

  object InvalidSeedable : Seedable {
    override fun seed(): Mono<Void> = Mono.error(RuntimeException("Seed error"))
  }

  object InvalidClearable : Clearable {
    override fun clear(): Mono<Void> = Mono.error(RuntimeException("Clear error"))
  }
}
