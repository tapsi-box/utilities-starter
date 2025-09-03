package box.tapsi.libs.utilities.springboot.annotations

import org.springframework.context.annotation.Conditional

/**
 * This annotation is used to conditionally enable a bean based on the provided properties.
 * This annotation will register the bean if any of the provided properties are matched to the required value.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@MustBeDocumented
@Conditional(OnPropertyAnyCondition::class)
annotation class ConditionalOnAnyProperty(
  /**
   * Alias for [.name].
   * @return the names
   */
  val value: Array<String>,

  /**
   * A prefix that should be applied to each property. The prefix automatically ends
   * with a dot if not specified. A valid prefix is defined by one or more words
   * separated with dots (e.g. `"acme.system.feature"`).
   * @return the prefix
   */
  val prefix: String = "",

  /**
   * The name of the properties to test. If a prefix has been defined, it is applied to
   * compute the full key of each property. For instance if the prefix is
   * `app.config` and one value is `my-value`, the full key would be
   * `app.config.my-value`
   *
   *
   * Use the dashed notation to specify each property, that is all a lower case with a "-"
   * to separate words (e.g. `my-long-property`).
   * @return the names
   */
  val name: Array<String> = [],

  /**
   * The string representation of the expected value for the properties. If not
   * specified, the property must **not** be equal to `false`.
   * @return the expected value
   */
  val havingValue: String = "",

  /**
   * Specify if the condition should match if the property is not set. Defaults to
   * `false`.
   * @return if the condition should match if the property is missing
   */
  val matchIfMissing: Boolean = false,
)
