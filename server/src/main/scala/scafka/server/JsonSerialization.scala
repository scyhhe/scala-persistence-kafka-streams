package scafka.server

import org.apache.kafka.common.serialization.{Deserializer, Serdes, Serializer}
import play.api.libs.json._

object JsonSerialization {
  def serializer[T: OFormat] = new Serializer[T] {

    override def serialize(key: String, data: T): Array[Byte] =
      (implicitly[OFormat[T]].writes(data)).toString().getBytes("UTF-8")

  }

  def deserializer[T: OFormat] = new Deserializer[T] {
    override def deserialize(key: String, data: Array[Byte]): T = Json.parse(data).as[T]
  }

  def serde[T: OFormat] = Serdes.serdeFrom(serializer, deserializer)
}
