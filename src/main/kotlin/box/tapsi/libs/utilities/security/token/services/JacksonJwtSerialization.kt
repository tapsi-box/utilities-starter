package box.tapsi.libs.utilities.security.token.services

import io.jsonwebtoken.io.AbstractDeserializer
import io.jsonwebtoken.io.AbstractSerializer
import tools.jackson.core.StreamReadFeature
import tools.jackson.core.StreamWriteFeature
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.ObjectReader
import tools.jackson.databind.ObjectWriter
import java.io.OutputStream
import java.io.Reader

/**
 * JJWT JSON serializer backed by a Jackson 3 [ObjectMapper].
 *
 * JJWT ships only a Jackson 2 integration (`jjwt-jackson`). Spring Boot 4 auto-configures a
 * Jackson 3 mapper, so this adapter lets JJWT use the application mapper directly.
 */
internal class JacksonJwtSerializer(objectMapper: ObjectMapper) : AbstractSerializer<Map<String, *>>() {
  private val writer: ObjectWriter = objectMapper.writer().without(StreamWriteFeature.AUTO_CLOSE_TARGET)

  override fun doSerialize(t: Map<String, *>, out: OutputStream) {
    writer.writeValue(out, t)
  }
}

/**
 * JJWT JSON deserializer backed by a Jackson 3 [ObjectMapper].
 *
 * Duplicate JSON keys are rejected, the same as the default JJWT Jackson deserializer.
 */
internal class JacksonJwtDeserializer(objectMapper: ObjectMapper) : AbstractDeserializer<Map<String, *>>() {
  private val reader: ObjectReader = objectMapper.readerFor(Map::class.java)
    .with(StreamReadFeature.STRICT_DUPLICATE_DETECTION)

  override fun doDeserialize(reader: Reader): Map<String, *> = this.reader.readValue(reader)
}
