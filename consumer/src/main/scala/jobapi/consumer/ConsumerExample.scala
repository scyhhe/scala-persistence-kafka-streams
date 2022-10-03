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
import scafka.events.MeetupUserRSPV.Status._
import org.apache.kafka.clients.consumer.KafkaConsumer
import cats.effect.kernel.Async
import cats.effect.IO
import cats.syntax.all._
import cats.effect.IOApp
import cats.effect.ExitCode
import org.apache.kafka.streams.scala.kstream.Materialized
import org.apache.kafka.streams.state.KeyValueStore
import org.apache.kafka.common.utils.Bytes

object ConsumerExample extends IOApp {

  val config: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, "scafka")
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    p.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, "1000")
    p
  }

  val builder = new StreamsBuilder()

  implicit val keySerde: Serde[UUID] =
    Serdes.serdeFrom((_, data) => data.toString.getBytes(StandardCharsets.UTF_8),
                     (_, data) => UUID.fromString(new String(data, StandardCharsets.UTF_8)))
  implicit val valueSerde: Serde[Event]                  = JsonSerialization.serde[Event](Event.formats)
  implicit val stateSerde: Serde[MeetupState]            = JsonSerialization.serde[MeetupState]
  implicit val eventStateSerde: Serde[MeetupState.Event] = JsonSerialization.serde[MeetupState.Event]
  implicit val userStateSerde: Serde[MeetupState.User]   = JsonSerialization.serde[MeetupState.User]

  val meetups = builder
    .stream[UUID, Event]("meetups")
    .groupByKey
    .aggregate(MeetupState.empty) { case (id, event, state) =>
      event match {
        case MeetupGroupCreated(id, name, description) => state.copy(id = id, name = name, description = description)
        case MeetupGroupUpdated(id, name, description) => state.copy(id = id, name = name, description = description)
        case MeetupGroupDeleted(id)                    => null
      }
    }

  val meetupEvents = builder
    .stream[UUID, Event]("meetup-events")
    .groupByKey
    // .cogroup[Event] { case (id, event, userEvent) =>
    //   userEvent match {
    //     case MeetupUserRSPV(id, _, name, status) =>
    //       status match {
    //         case Going    => state.copy(users = upsertMeetupUser(state.users, MeetupState.User(id, name)))
    //         case Maybe    => state.copy(users = upsertMeetupUser(state.users, MeetupState.User(id, name)))
    //         case NotGoing => state.copy(users = state.users.filterNot(_.id == id))
    //       }
    //   }
    // }
    // .groupBy {
    //   case (_, MeetupEventCreated(_, groupId, _, _, _)) => groupId
    //   case (_, MeetupEventUpdated(_, groupId, _, _, _)) => groupId
    //   case (_, MeetupEventDeleted(_, groupId))          => groupId
    //   case (_, MeetupUserRSPV(_, eventId, _, _))        => eventId
    // }
    .aggregate(MeetupState.Event.empty) { case (id, event, state) =>
      event match {
        case MeetupEventCreated(id, _, name, description, date) =>
          state.copy(id = id, name = name, description = description, date = date, Nil)
        case MeetupEventUpdated(id, _, name, description, date) =>
          state.copy(id = id, name = name, description = description, date = date)
        case MeetupEventDeleted(id, _) => null
        case MeetupUserRSPV(id, _, name, status) =>
          status match {
            case Going    => state.copy(users = upsertMeetupUser(state.users, MeetupState.User(id, name)))
            case Maybe    => state.copy(users = upsertMeetupUser(state.users, MeetupState.User(id, name)))
            case NotGoing => state.copy(users = state.users.filterNot(_.id == id))
          }
      }
    }

  val meetupUsers = builder
    .stream[UUID, Event]("meetup-users")
    .groupByKey
  // .cogroup((a, s, d) => ???)
  // .groupBy { case (_, MeetupUserRSPV(_, eventId, _, _)) => eventId }
  // .aggregate(MeetupState.User.empty) { case (id, event, state) =>
  //   event match {
  //     case MeetupUserRSPV(id, _, name, _) => state.copy(id = id, name = name)
  //   }
  // }

  meetups
    .leftJoin(
      other = meetupEvents,
      keyExtractor = (state: MeetupState) => state.id,
      joiner = (state: MeetupState, eventState: MeetupState.Event) =>
        state.copy(events = upsertMeetupEvent(state.events, eventState)),
      materialized = implicitly[Materialized[UUID, MeetupState, KeyValueStore[Bytes, Array[Byte]]]]
    )
    // .leftJoin(
    //   other = meetupUsers,
    //   keyExtractor = (state: MeetupState) => state.id,
    //   joiner = (state: MeetupState, eventState: MeetupState.User) =>
    //     state.copy(events = upsertMeetupEvent(state.events, eventState)),
    //   materialized = implicitly[Materialized[UUID, MeetupState, KeyValueStore[Bytes, Array[Byte]]]]
    // )
    .toStream
    .to("meetup-state")

  val streams = new KafkaStreams(builder.build(), config)

  override def run(args: List[String]): IO[ExitCode] =
    Async[IO]
      .start(streams.start().pure[IO])
      .map(_.cancel)
      .as(ExitCode.Success)
      .flatTap(_ => streams.close().pure[IO])

  private def upsertMeetupEvent(existing: List[MeetupState.Event],
                                event: MeetupState.Event): List[MeetupState.Event] = {
    val newEvent = existing
      .find(_.id == event.id)
      .fold(event)(old => old.copy(name = event.name, description = event.description, date = event.date))

    existing :+ newEvent
  }

  private def upsertMeetupUser(existing: List[MeetupState.User], user: MeetupState.User): List[MeetupState.User] = {
    val newEvent = existing
      .find(_.id == user.id)
      .fold(user)(old => old.copy(name = user.name))

    existing :+ newEvent
  }

}
