package box.tapsi.libs.utilities.simulator.data

import box.tapsi.libs.utilities.simulator.data.clearable.Clearable
import box.tapsi.libs.utilities.simulator.data.seedable.Seedable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.slf4j.Logger
import org.springframework.context.ApplicationContext
import reactor.kotlin.test.test

@Suppress("JavaStyleCallReplaceableByKotlinExtension")
class DataSimulatorImplTest {
  private lateinit var dataSimulator: DataSimulatorImpl

  @Mock
  private lateinit var applicationContext: ApplicationContext

  @Mock
  private lateinit var logger: Logger

  @BeforeEach
  fun init() {
    MockitoAnnotations.openMocks(this)
    dataSimulator = DataSimulatorImpl(applicationContext, logger)
  }

  @Test
  fun `should seed data successfully when seedables are available`() {
    // given
    val seedable1 = SimulatorTestHelper.ValidSeedable
    val seedable2 = SimulatorTestHelper.ValidSeedable
    val beanNames = arrayOf("seedable1", "seedable2")

    // when
    whenever(applicationContext.getBeanNamesForType(Seedable::class.java)).thenReturn(beanNames)
    whenever(applicationContext.getBean("seedable1")).thenReturn(seedable1)
    whenever(applicationContext.getBean("seedable2")).thenReturn(seedable2)

    // verify
    dataSimulator.seedData()
      .test()
      .verifyComplete()
  }

  @Test
  fun `should complete successfully when no seedables are available`() {
    // given

    // when
    whenever(applicationContext.getBeanNamesForType(Seedable::class.java)).thenReturn(emptyArray())

    // verify
    dataSimulator.seedData()
      .test()
      .verifyComplete()
  }

  @Test
  fun `should propagate error when seedable fails`() {
    // given
    val invalidSeedable = SimulatorTestHelper.InvalidSeedable
    val beanNames = arrayOf("invalidSeedable")

    // when
    whenever(applicationContext.getBeanNamesForType(Seedable::class.java)).thenReturn(beanNames)
    whenever(applicationContext.getBean("invalidSeedable")).thenReturn(invalidSeedable)

    // verify
    dataSimulator.seedData()
      .test()
      .verifyError()

    verify(logger).error(eq("Error seeding data"), any())
  }

  @Test
  fun `should clear data successfully when clearables are available`() {
    // given
    val clearable1 = SimulatorTestHelper.ValidClearable
    val clearable2 = SimulatorTestHelper.ValidClearable
    val beanNames = arrayOf("clearable1", "clearable2")

    // when
    whenever(applicationContext.getBeanNamesForType(Clearable::class.java)).thenReturn(beanNames)
    whenever(applicationContext.getBean("clearable1")).thenReturn(clearable1)
    whenever(applicationContext.getBean("clearable2")).thenReturn(clearable2)

    // verify
    dataSimulator.clearData()
      .test()
      .verifyComplete()
  }

  @Test
  fun `should complete successfully when no clearables are available`() {
    // given

    // when
    whenever(applicationContext.getBeanNamesForType(Clearable::class.java)).thenReturn(emptyArray())

    // verify
    dataSimulator.clearData()
      .test()
      .verifyComplete()
  }

  @Test
  fun `should propagate error when clearable fails`() {
    // given
    val invalidClearable = SimulatorTestHelper.InvalidClearable
    val beanNames = arrayOf("invalidClearable")

    // when
    whenever(applicationContext.getBeanNamesForType(Clearable::class.java)).thenReturn(beanNames)
    whenever(applicationContext.getBean("invalidClearable")).thenReturn(invalidClearable)

    // verify
    dataSimulator.clearData()
      .test()
      .verifyError()

    verify(logger).error(eq("Error clearing data"), any())
  }

  @Test
  fun `should process multiple seedables in sequence`() {
    // given
    val seedable1 = SimulatorTestHelper.ValidSeedable
    val seedable2 = SimulatorTestHelper.ValidSeedable
    val seedable3 = SimulatorTestHelper.ValidSeedable
    val beanNames = arrayOf("seedable1", "seedable2", "seedable3")

    // when
    whenever(applicationContext.getBeanNamesForType(Seedable::class.java)).thenReturn(beanNames)
    whenever(applicationContext.getBean("seedable1")).thenReturn(seedable1)
    whenever(applicationContext.getBean("seedable2")).thenReturn(seedable2)
    whenever(applicationContext.getBean("seedable3")).thenReturn(seedable3)

    // verify
    dataSimulator.seedData()
      .test()
      .verifyComplete()
  }

  @Test
  fun `should process multiple clearables in sequence`() {
    // given
    val clearable1 = SimulatorTestHelper.ValidClearable
    val clearable2 = SimulatorTestHelper.ValidClearable
    val clearable3 = SimulatorTestHelper.ValidClearable
    val beanNames = arrayOf("clearable1", "clearable2", "clearable3")

    // when
    whenever(applicationContext.getBeanNamesForType(Clearable::class.java)).thenReturn(beanNames)
    whenever(applicationContext.getBean("clearable1")).thenReturn(clearable1)
    whenever(applicationContext.getBean("clearable2")).thenReturn(clearable2)
    whenever(applicationContext.getBean("clearable3")).thenReturn(clearable3)

    // verify
    dataSimulator.clearData()
      .test()
      .verifyComplete()
  }
}
