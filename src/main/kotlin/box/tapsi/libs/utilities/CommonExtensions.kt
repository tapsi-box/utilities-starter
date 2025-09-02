@file:Suppress("detekt.TooManyFunctions")

package box.tapsi.libs.utilities

import org.springframework.util.ClassUtils
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.lang.reflect.Method
import kotlin.math.absoluteValue
import kotlin.reflect.KClass
import kotlin.reflect.full.cast

/**
 * Attempts to cast the current instance to the specified type T.
 * If the cast is not possible, a ClassCastException is thrown.
 *
 * @return The current instance cast to the specified type T.
 * @throws ClassCastException if the current instance cannot be cast to the specified type T.
 */
inline fun <reified T : Any> Any.castOrThrow(): T = T::class.cast(this)

/**
 * Casts the current object to the specified type or throws an exception if the cast is not possible.
 *
 * @param kClass the class of the type to cast to.
 * @return the object cast to the specified type.
 * @throws ClassCastException if the object cannot be cast to the specified type.
 */
fun <T : Any> Any.castOrThrow(
  kClass: KClass<T>,
): T = kClass.cast(this)

/**
 * Determines if the method's return type is a reactive publisher such as `Mono` or `Flux`.
 *
 * @return `true` if the method returns a `Mono` or `Flux`, otherwise `false`.
 */
fun Method.isReturningPublisher(): Boolean = when (this.returnType) {
  Mono::class.java -> true
  Flux::class.java -> true
  else -> false
}

/**
 * Checks if the return type of the method is of type `Mono`.
 *
 * @return `true` if the return type is `Mono`, otherwise `false`.
 */
fun Method.isReturningMono(): Boolean = this.returnType == Mono::class.java

/**
 * Determines whether the method has a return type of `Flux`.
 *
 * @return `true` if the method's return type is `Flux`, otherwise `false`.
 */
fun Method.isReturningFlux(): Boolean = this.returnType == Flux::class.java

/**
 * Returns a list containing only the distinct elements from the original collection,
 * where distinct is determined by the last occurrence of an element when compared by the key
 * generated via the provided keySelector function.
 *
 * @param keySelector A function that takes an element from the collection and returns a key to determine uniqueness.
 * @return A list of elements from the original collection, preserving only the last occurrence of each key.
 */
inline fun <T, K> Iterable<T>.distinctLastBy(crossinline keySelector: (T) -> K): List<T> {
  val set = mutableSetOf<K>()
  val result = mutableListOf<T>()
  for (element in this.reversed()) {
    val key = keySelector(element)
    if (set.add(key)) {
      result.add(element)
    }
  }
  return result.reversed()
}

/**
 * Converts all English digits in the string to their corresponding Persian digits.
 *
 * @return A new string where all numeric characters (0-9) have been replaced with their Persian equivalents.
 */
fun String.toPersianNumber(): String = this.replace("0", "۰")
  .replace("1", "۱")
  .replace("2", "۲")
  .replace("3", "۳")
  .replace("4", "۴")
  .replace("5", "۵")
  .replace("6", "۶")
  .replace("7", "۷")
  .replace("8", "۸")
  .replace("9", "۹")

/**
 * Converts all Persian digits in the string to their corresponding English digits.
 *
 * @return A new string where all Persian numeric characters (۰-۹) have been replaced with their English equivalents.
 */
fun String.toEnglishNumber(): String = this.replace("۰", "0")
  .replace("۱", "1")
  .replace("۲", "2")
  .replace("۳", "3")
  .replace("۴", "4")
  .replace("۵", "5")
  .replace("۶", "6")
  .replace("۷", "7")
  .replace("۸", "8")
  .replace("۹", "9")

/**
 * Converts a Long into its corresponding ordinal number as a String
 * (e.g., 1 -> "1st", 2 -> "2nd", 3 -> "3rd", 4 -> "4th").
 * Handles edge cases for numbers ending in 11, 12, or 13, which are always suffixed with "th".
 *
 * @return The ordinal representation of the Long value as a String.
 */
@Suppress("detekt.MagicNumber")
fun Long.toOrdinal(): String {
  val thisAbs = this.absoluteValue
  return when {
    thisAbs % 100 in 11..13 -> "${thisAbs}th"
    thisAbs % 10 == 1L -> "${thisAbs}st"
    thisAbs % 10 == 2L -> "${thisAbs}nd"
    thisAbs % 10 == 3L -> "${thisAbs}rd"
    else -> "${thisAbs}th"
  }
}

/**
 * Retrieves the original class of the given Kotlin class, resolving any proxy or subclass wrappers.
 *
 * @return the original class of the given Kotlin class.
 */
fun KClass<out Any>.getOriginalClass(): KClass<out Any> = ClassUtils.getUserClass(this.java).kotlin

/**
 * Retrieves the original class associated with the provided class,
 * unwrapping any proxies that might be wrapping the given class.
 *
 * @return the original class of the given class, which might be
 * unwrapped from a proxy or similar structure.
 */
fun Class<out Any>.getOriginalClass(): Class<out Any> = ClassUtils.getUserClass(this)

/**
 * Retrieves a `Class` object for a given fully qualified class name using the specified class loader.
 *
 * @param className the fully qualified name of the class to retrieve
 * @param classLoader the class loader to use for loading the class; may be null,
 * in which case the default class loader is used
 * @return the `Class` object corresponding to the specified class name
 * @throws ClassNotFoundException if the class cannot be found or loaded
 * @throws LinkageError if the class has a dependency on another class that cannot be found or loaded
 */
fun getClassByFullQualifiedName(
  className: String,
  classLoader: ClassLoader?,
): Class<out Any> = ClassUtils.forName(className, classLoader)

/**
 * Retrieves a method from the specified class if it is available with the given name and parameter types.
 *
 * @param clazz the class from which to retrieve the method
 * @param methodName the name of the method to retrieve
 * @param parameterTypes the parameter types of the method
 * @return the method if available, or null if it does not exist
 */
fun getMethodIfAvailable(
  clazz: Class<out Any>,
  methodName: String,
  vararg parameterTypes: Class<*>,
): Method? = ClassUtils.getMethodIfAvailable(clazz, methodName, *parameterTypes)
