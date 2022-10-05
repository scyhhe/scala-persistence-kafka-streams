package scafka.consumer

import org.apache.kafka.common.serialization._
import play.api.libs.json._
import java.nio.charset.StandardCharsets

object JsonSerialization {
  def serializer[T: Format] = new Serializer[T] {
    override def serialize(key: String, data: T): Array[Byte] = {
      if (data == null) null
      else (implicitly[Format[T]].writes(data)).toString().getBytes(StandardCharsets.UTF_8)
    }
  }

  def deserializer[T: Format] = new Deserializer[T] {
    override def deserialize(key: String, data: Array[Byte]): T = Json.parse(data).as[T]
  }

  def nullableDeserializer[T >: Null: Format] = new Deserializer[T] {
    override def deserialize(key: String, data: Array[Byte]): T =
      if (data == null) null
      else Json.parse(data).as[T]
  }

  def serde[T: Format] = Serdes.serdeFrom(serializer, deserializer)
  // def nullableSerde[T >: Null: Format] = Serdes.serdeFrom(serializer, nullableDeserializer)
}
