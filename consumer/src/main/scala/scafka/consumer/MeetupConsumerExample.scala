package scafka.consumer

import scala.io.StdIn.readLine

import org.apache.kafka.common.serialization._
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.KTable
import org.apache.kafka.streams._

import java.util.Properties
import java.util.UUID
import java.nio.charset.StandardCharsets
import scafka.events._
import scafka.events.Event._
import scafka.events.UserRSVP.Status._
import org.apache.kafka.clients.consumer.KafkaConsumer
import cats.effect.kernel.Async
import cats.effect.IO
import cats.syntax.all._
import cats.effect.IOApp
import cats.effect.ExitCode
import org.apache.kafka.streams.scala.kstream.Materialized
import org.apache.kafka.streams.state.KeyValueStore
import org.apache.kafka.common.utils.Bytes
import _root_.scala.io.StdIn
import java.time.LocalDateTime
import play.api.libs.json.Json

object MeetupConsumerExample extends App {

  val config: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, "scafka-meetup")
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    p.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, "1000")
    p
  }

  val builder = new StreamsBuilder()

  implicit val keySerde: Serde[UUID] =
    Serdes.serdeFrom((_, data) => data.toString.getBytes(StandardCharsets.UTF_8),
                     (_, data) => UUID.fromString(new String(data, StandardCharsets.UTF_8)))

  implicit val meetupSerde: Serde[MeetupEvent]           = JsonSerialization.serde[MeetupEvent](Event.meetupFormats)
  implicit val stateSerde: Serde[MeetupState]            = JsonSerialization.serde[MeetupState]
  implicit val eventStateSerde: Serde[MeetupState.Event] = JsonSerialization.serde[MeetupState.Event]
  implicit val userStateSerde1: Serde[MeetupState.User]  = JsonSerialization.serde[MeetupState.User]
  implicit val userRSVPSerde: Serde[UserRSVP]            = JsonSerialization.serde[UserRSVP]

  val meetups = {
    builder
      .stream[UUID, MeetupEvent]("meetups")
      .groupBy {
        case (_, MeetupEventCreated(_, groupId, _, _, _)) => groupId
        case (_, MeetupEventUpdated(_, groupId, _, _, _)) => groupId
        case (_, MeetupEventDeleted(_, groupId))          => groupId
        case (id, _)                                      => id
      }
      .aggregate(MeetupState.empty) { case (id, event, state) =>
        event match {
          case MeetupGroupCreated(id, name, description) => state.copy(id = id, name = name, description = description)
          case MeetupGroupUpdated(id, name, description) =>
            state.copy(id = id, name = name, description = description)
          case MeetupEventCreated(id, _, name, description, date) =>
            state.copy(events = upsertMeetupEvent(state.events, MeetupState.Event(id, name, description, date, Nil)))
          case MeetupEventUpdated(id, _, name, description, date) =>
            state.copy(events = upsertMeetupEvent(state.events, MeetupState.Event(id, name, description, date, Nil)))
        }
      }
  }

  meetups.toStream.to("meetup-state")

  val streams = new KafkaStreams(builder.build(), config)

  streams.cleanUp()
  streams.start()

  StdIn.readLine()

  Runtime
    .getRuntime()
    .addShutdownHook(new Thread() {
      override def run(): Unit = {
        streams.close()
      }
    })

  private def upsertMeetupEvent(existing: List[MeetupState.Event],
                                event: MeetupState.Event): List[MeetupState.Event] = {
    val newEvent = existing
      .find(_.id == event.id)
      .fold(event)(old => old.copy(name = event.name, description = event.description, date = event.date))

    existing :+ newEvent
  }
}
