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

  val producer = new KafkaProducer[UUID, Event](
    config,
    (_: String, d: UUID) => d.toString.getBytes(StandardCharsets.UTF_8),
    JsonSerialization.serializer[Event](Event.formats)
  )
  val zeroes = new UUID(0, 0)
  val id     = UUID.fromString("1f6cff27-46c9-4716-add8-311d118c0001")
  val id2    = UUID.fromString("2f6cff27-46c9-4716-add8-311d118c0002")

  val createGroup = MeetupGroupCreated(zeroes, "Scala Vienna", "Group of people doing things")
  val updateGroup = MeetupGroupUpdated(zeroes, "Scala Varna", "Another one")
  val deleteGroup = MeetupGroupUpdated(zeroes, "Scala Varna", "Another one")
  val createEvent = MeetupEventCreated(zeroes,
                                       id2,
                                       "Scala Vienna 2nd October",
                                       "Some boy trying to sound smart.",
                                       LocalDateTime.parse("2022-10-02T18:30:00"))
  val rsvpUser         = MeetupUserRSPV(id, id2, "Danny P", MeetupUserRSPV.Status.Going)
  val rsvpUserNotGoing = MeetupUserRSPV(zeroes, id2, "Danny P", MeetupUserRSPV.Status.NotGoing)

  val updateEvent = MeetupEventUpdated(zeroes,
                                       id2,
                                       "Scala Varna 2nd November",
                                       "failing miserably",
                                       LocalDateTime.parse("2022-11-02T19:30:00"))
  val deleteEvent = MeetupEventDeleted(id, id2)

  // producer.send(new ProducerRecord("meetups", id, createGroup))
  // producer.send(new ProducerRecord("meetups", id2, createGroup))
  // producer.send(new ProducerRecord("meetup-events", createEvent.id, createEvent))
  producer.send(new ProducerRecord("meetup-events", updateEvent.groupId, updateEvent))
  producer.send(new ProducerRecord("meetup-events", rsvpUser.eventId, rsvpUser))

  // producer.send(new ProducerRecord("meetups", id2, updateGroup))
  // producer.send(new ProducerRecord("meetup-events", id, createEvent))
  // producer.send(new ProducerRecord("meetup-events", id, updateEvent))
  // producer.send(new ProducerRecord("meetup-events", id2, deleteEvent))

  Runtime
    .getRuntime()
    .addShutdownHook(new Thread() {
      override def run(): Unit = {
        producer.close()
      }
    })

}
