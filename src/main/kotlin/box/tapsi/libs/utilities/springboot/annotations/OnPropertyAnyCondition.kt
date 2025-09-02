package box.tapsi.libs.utilities.springboot.annotations

import org.springframework.boot.autoconfigure.condition.ConditionMessage
import org.springframework.boot.autoconfigure.condition.ConditionOutcome
import org.springframework.boot.autoconfigure.condition.SpringBootCondition
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.Ordered
import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.annotation.MergedAnnotationPredicates
import org.springframework.core.annotation.Order
import org.springframework.core.env.PropertyResolver
import org.springframework.core.type.AnnotatedTypeMetadata
import org.springframework.util.Assert
import org.springframework.util.StringUtils

/**
 * [SpringBootCondition] that checks if properties are defined in environment.
 * @see ConditionalOnAnyProperty
 */
@Order(OnPropertyAnyCondition.ORDER)
class OnPropertyAnyCondition : SpringBootCondition() {
  override fun getMatchOutcome(context: ConditionContext, metadata: AnnotatedTypeMetadata): ConditionOutcome {
    val allAnnotationAttributes =
      metadata.annotations.stream(ConditionalOnAnyProperty::class.java)
        .filter(MergedAnnotationPredicates.unique { it.metaTypes })
        .map { it.asAnnotationAttributes() }
        .toList()

    val noMatch = mutableListOf<ConditionMessage>()
    val match = mutableListOf<ConditionMessage>()
    allAnnotationAttributes.forEach { annotationAttributes ->
      val outcome = determineOutcome(annotationAttributes, context.environment)
      if (outcome.isMatch) match.add(outcome.conditionMessage) else noMatch.add(outcome.conditionMessage)
    }

    if (match.isNotEmpty()) {
      return ConditionOutcome.match(ConditionMessage.of(match))
    }
    return ConditionOutcome.noMatch(ConditionMessage.of(noMatch))
  }

  private fun determineOutcome(
    annotationAttributes: AnnotationAttributes,
    resolver: PropertyResolver,
  ): ConditionOutcome {
    val spec = Spec(annotationAttributes)
    val missingProperties = mutableListOf<String>()
    val nonMatchingProperties = mutableListOf<String>()
    val matchingProperties = mutableListOf<String>()
    spec.collectProperties(resolver, missingProperties, nonMatchingProperties, matchingProperties)

    return if (matchingProperties.isNotEmpty()) {
      ConditionOutcome.match(
        ConditionMessage.forCondition(ConditionalOnAnyProperty::class.java, spec)
          .found("property", "properties")
          .items(ConditionMessage.Style.QUOTE, matchingProperties),
      )
    } else if (missingProperties.isNotEmpty()) {
      ConditionOutcome.noMatch(
        ConditionMessage.forCondition(ConditionalOnAnyProperty::class.java, spec)
          .didNotFind("property", "properties")
          .items(ConditionMessage.Style.QUOTE, missingProperties),
      )
    } else if (nonMatchingProperties.isNotEmpty()) {
      ConditionOutcome.noMatch(
        ConditionMessage.forCondition(ConditionalOnAnyProperty::class.java, spec)
          .found("different value in property", "different value in properties")
          .items(ConditionMessage.Style.QUOTE, nonMatchingProperties),
      )
    } else {
      ConditionOutcome
        .match(ConditionMessage.forCondition(ConditionalOnAnyProperty::class.java, spec).because("matched"))
    }
  }

  private class Spec(annotationAttributes: AnnotationAttributes) {
    private val prefix: String

    private val havingValue: String

    private val names: Array<String>?

    private val matchIfMissing: Boolean

    init {
      var prefix = annotationAttributes.getString("prefix").trim { it <= ' ' }
      if (StringUtils.hasText(prefix) && !prefix.endsWith(".")) {
        prefix = "$prefix."
      }
      this.prefix = prefix
      this.havingValue = annotationAttributes.getString("havingValue")
      this.names = getNames(annotationAttributes)
      this.matchIfMissing = annotationAttributes.getBoolean("matchIfMissing")
    }

    fun collectProperties(
      resolver: PropertyResolver,
      missing: MutableList<String>,
      nonMatching: MutableList<String>,
      match: MutableList<String>,
    ) {
      for (name in names!!) {
        val key = this.prefix + name
        if (resolver.containsProperty(key)) {
          if (!isMatch(resolver.getProperty(key), this.havingValue)) {
            nonMatching.add(name)
          } else {
            match.add(name)
          }
        } else {
          if (!this.matchIfMissing) {
            missing.add(name)
          }
        }
      }
    }

    override fun toString(): String {
      val result = StringBuilder()
      result.append("(")
      result.append(this.prefix)
      if (names!!.size == 1) {
        result.append(names[0])
      } else {
        result.append("[")
        result.append(StringUtils.arrayToCommaDelimitedString(this.names))
        result.append("]")
      }
      if (StringUtils.hasLength(this.havingValue)) {
        result.append("=").append(this.havingValue)
      }
      result.append(")")
      return result.toString()
    }

    private fun getNames(annotationAttributes: Map<String, Any>): Array<String>? {
      val value = annotationAttributes["value"] as Array<String>?
      val name = annotationAttributes["name"] as Array<String>?
      Assert.state(
        value!!.isNotEmpty() || name!!.isNotEmpty(),
        "The name or value attribute of @ConditionalOnProperty must be specified",
      )
      Assert.state(
        value.isEmpty() || name!!.isEmpty(),
        "The name and value attributes of @ConditionalOnProperty are exclusive",
      )
      return if ((value.isNotEmpty())) value else name
    }

    private fun isMatch(value: String?, requiredValue: String): Boolean {
      if (StringUtils.hasLength(requiredValue)) {
        return requiredValue.equals(value, ignoreCase = true)
      }
      return !"false".equals(value, ignoreCase = true)
    }
  }

  companion object {
    const val ORDER = Ordered.HIGHEST_PRECEDENCE + 50
  }
}
