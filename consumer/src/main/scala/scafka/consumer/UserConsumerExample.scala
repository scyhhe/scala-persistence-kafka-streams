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

object UserConsumerExample extends App {

  val config: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, "scafka-user")
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    p.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, "1000")
    p
  }

  val builder = new StreamsBuilder()

  implicit val keySerde: Serde[UUID] =
    Serdes.serdeFrom((_, data) => data.toString.getBytes(StandardCharsets.UTF_8),
                     (_, data) => UUID.fromString(new String(data, StandardCharsets.UTF_8)))
  implicit val meetupSerde: Serde[MeetupEvent]  = JsonSerialization.serde[MeetupEvent](Event.meetupFormats)
  implicit val userSerde: Serde[UserEvent]      = JsonSerialization.serde[UserEvent](Event.userFormats)
  implicit val userRSVPSerde: Serde[UserRSVP]   = JsonSerialization.serde[UserRSVP]
  implicit val userStateSerde: Serde[UserState] = JsonSerialization.serde[UserState]

  val userAttendanceGlobal = builder
    .globalTable[UUID, UserRSVP]("user-attendance")

  val userAttendance = {
    val users = builder
      .stream[UUID, UserEvent]("users")
      .groupByKey
      .aggregate(UserState.empty) { case (id, event, state) =>
        event match {
          case UserCreated(id, name) => state.copy(id = id, name = name)
        }
      }

    // val events = builder
    //   .stream[UUID, MeetupEvent]("meetups")
    //   .leftJoin(userAttendanceGlobal)(
    //     (k, v) => k,
    //     { case (event, rsvp) =>
    //       event match {
    //         case e: MeetupEventCreated if e.id == rsvp.eventId && rsvp.status != UserRSVP.Status.NotGoing =>
    //           UserState.AttendingEvent(rsvp.id, e.name, e.description, e.date)
    //         case e: MeetupEventUpdated if e.id == rsvp.eventId && rsvp.status != UserRSVP.Status.NotGoing =>
    //           UserState.AttendingEvent(rsvp.id, e.name, e.description, e.date)
    //       }
    //     }
    //   )
    //   .toTable

    users
    // .join(events)((userState, evt) => userState.copy(attending = upsertUserEvent(userState.attending, evt)))

  }

  userAttendance.toStream.to("user-state")

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

  private def upsertUserEvent(existing: List[UserState.AttendingEvent],
                              event: UserState.AttendingEvent): List[UserState.AttendingEvent] = {
    val newEvent = existing
      .find(_.id == event.id)
      .fold(event)(old => old.copy(name = event.name, description = event.description, date = event.date))

    existing :+ newEvent
  }

}
