package scafka.server

import scafka.events._
import org.apache.kafka.clients.producer._

import java.util.Properties
import java.util.UUID
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime

object Produce extends App {
  val config: Properties = {
    val p = new Properties()
    p.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    p
  }

  val meetupProducer = new KafkaProducer[UUID, Event.MeetupEvent](
    config,
    (_: String, d: UUID) => d.toString.getBytes(StandardCharsets.UTF_8),
    JsonSerialization.serializer[Event.MeetupEvent](Event.meetupFormats)
  )

  val userProducer = new KafkaProducer[UUID, Event.UserEvent](
    config,
    (_: String, d: UUID) => d.toString.getBytes(StandardCharsets.UTF_8),
    JsonSerialization.serializer[Event.UserEvent](Event.userFormats)
  )

  val zeroes        = new UUID(0, 0)
  val meetupId      = UUID.fromString("1f6cff27-46c9-4716-add8-311d118c0001")
  val meetupEventId = UUID.fromString("2f6cff27-46c9-4716-add8-311d118c0002")
  val userId        = UUID.fromString("3f6cff27-46c9-4716-add8-311d118c0003")

  val createGroup = MeetupGroupCreated(meetupId, "Scala Vienna", "Group of people doing things")
  val updateGroup = MeetupGroupUpdated(meetupId, "Scala Varna", "Another one")
  val deleteGroup = MeetupGroupDeleted(meetupId)

  val createEvent = MeetupEventCreated(meetupEventId,
                                       meetupId,
                                       "Scala Vienna 2nd October",
                                       "Some boy trying to sound smart.",
                                       LocalDateTime.parse("2022-10-02T18:30:00"))

  val updateEvent = MeetupEventUpdated(meetupEventId,
                                       meetupId,
                                       "Scala Varna 2nd November",
                                       "failing miserably",
                                       LocalDateTime.parse("2022-11-02T19:30:00"))
  val deleteEvent = MeetupEventDeleted(meetupEventId, meetupId)

  val userCreated = UserCreated(userId, "Danny P")

  val rsvpUser         = UserRSVP(userId, meetupEventId, UserRSVP.Status.Going)
  val rsvpUserNotGoing = UserRSVP(userId, meetupEventId, UserRSVP.Status.NotGoing)

  meetupProducer.send(new ProducerRecord("meetups", meetupId, createGroup))
  Thread.sleep(1000)
  meetupProducer.send(new ProducerRecord("meetups", meetupEventId, createEvent))
  Thread.sleep(1000)
  userProducer.send(new ProducerRecord("users", userId, userCreated))
  // Thread.sleep(1000)
  // userProducer.send(new ProducerRecord("user-attendance", meetupEventId, rsvpUser))

  Runtime
    .getRuntime()
    .addShutdownHook(new Thread() {
      override def run(): Unit = {
        meetupProducer.close()
        userProducer.close()
      }
    })

}
