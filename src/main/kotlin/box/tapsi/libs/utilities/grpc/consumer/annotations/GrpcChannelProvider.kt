package box.tapsi.libs.utilities.grpc.consumer.annotations

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.annotation.AliasFor

@Target(
  AnnotationTarget.FIELD,
  AnnotationTarget.FUNCTION,
  AnnotationTarget.TYPE_PARAMETER,
  AnnotationTarget.VALUE_PARAMETER,
)
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class GrpcChannelProvider(
  @get:AliasFor(annotation = Qualifier::class)
  val value: String,
)
