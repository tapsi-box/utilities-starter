package box.tapsi.libs.utilities.reactor

import reactor.util.function.Tuple2
import reactor.util.function.Tuple3
import reactor.util.function.Tuple4
import reactor.util.function.Tuples

fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any> combineTuples(
  t1: Tuple2<T1, T2>,
  t2: Tuple2<T3, T4>,
): Tuple4<T1, T2, T3, T4> = Tuples.of(t1.t1, t1.t2, t2.t1, t2.t2)

fun <T1 : Any, T2 : Any, T3 : Any> withTuples2(
  t1: T1,
  t2: Tuple2<T2, T3>,
): Tuple3<T1, T2, T3> = Tuples.of(t1, t2.t1, t2.t2)
