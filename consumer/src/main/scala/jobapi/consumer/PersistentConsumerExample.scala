package scafka.consumer

import org.apache.kafka.common.serialization._
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams._

import java.util.Properties
import java.util.UUID
import java.nio.charset.StandardCharsets
import scafka.events._
import org.apache.kafka.streams.state.Stores
import org.apache.kafka.streams.kstream.Printed
import org.apache.kafka.streams.state.internals.KeyValueStoreBuilder
import org.apache.kafka.streams.state.KeyValueStore
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier
import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.syntax.all._
import play.api.libs.json.Format

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerConfig._
import java.time.Duration
import _root_.scala.jdk.CollectionConverters._

object PersistentConsumerExample extends IOApp {

  implicit val keySerde: Deserializer[UUID]          = JsonSerialization.deserializer[UUID]
  implicit val valueSerde: Deserializer[MeetupState] = JsonSerialization.deserializer[MeetupState]

  val props: Map[String, Object] = Map(
    GROUP_ID_CONFIG                -> "scafka-state-consumer",
    BOOTSTRAP_SERVERS_CONFIG       -> "localhost:9092",
    AUTO_COMMIT_INTERVAL_MS_CONFIG -> "5000"
  )

  override def run(args: List[String]): IO[ExitCode] =
    pollForever[UUID, MeetupState]("meetup-state").as(ExitCode.Success)

  private def pollForever[K, V](
      topic: String)(implicit keyDeser: Deserializer[K], valueDeser: Deserializer[V]): IO[Nothing] = {
    val database = Map.empty[K, V]
    Resource
      .make {
        val consumer = new KafkaConsumer[K, V](props.asJava, keyDeser, valueDeser)
        consumer.subscribe(Seq(topic).asJava)
        IO(consumer)
      }(c => IO(println(s"[$topic] closing consumer...")) *> IO(c.close()))
      .use { consumer =>
        val consume: IO[Unit] = for {
          records <- IO(consumer.poll(Duration.ofSeconds(5)).asScala.toSeq)
          keyValue = records.map { r => ((r.key()), r.value()) }
          _ <- keyValue.traverse { case (k, v) =>
            IO {
              println(s"[$topic] $k => $v")
              database + ((k, v))
              println(database)
            }
          }
        } yield ()
        consume.foreverM.onCancel(consumer.close(Duration.ofSeconds(2)).pure[IO])
      }
  }
}
