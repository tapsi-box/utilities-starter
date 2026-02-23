package box.tapsi.libs.utilities.grpc.consumer.annotations

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.core.annotation.AliasFor
import java.lang.annotation.Inherited

/**
 * Annotation to conditionally enable a gRPC client-based service or component in the application.
 *
 * This annotation can be applied to classes, and its activation depends on the specified property being set to "true"
 * in the application's configuration file. If the property is not explicitly defined, the default behavior is to enable
 * the annotated component (`matchIfMissing = true`).
 *
 * The targeted property key is derived by combining the prefix:
 * `box.libs.utilities.grpc.consumer.clients` with the provided `name` values.
 *
 *
 * Usage context is generally within applications that require enabling specific gRPC clients managed via configuration
 * flags, providing greater control over which services are active in a given environment.
 *
 * This annotation behaves as a combination of `@ConditionalOnProperty` for seamless
 * configuration-driven activation of gRPC client-based functionality.
 *
 * @property name The list of property names whose values determine whether the annotated class is enabled.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
@Inherited
@ConditionalOnProperty(
  prefix = "box.libs.utilities.grpc.consumer.clients",
  havingValue = "true",
  matchIfMissing = true,
)
annotation class OnGrpcClientEnabled(
  @get:AliasFor(annotation = ConditionalOnProperty::class)
  val name: Array<String>,
)
