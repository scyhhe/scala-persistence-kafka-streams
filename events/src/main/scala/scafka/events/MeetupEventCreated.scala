package scafka.events

import play.api.libs.json._
import java.util.UUID
import java.time.LocalDateTime
import scafka.events.Event._

object MeetupEventCreated {
  implicit val format: OFormat[MeetupEventCreated] = Json.format[MeetupEventCreated]
}

case class MeetupEventCreated(id: UUID, groupId: UUID, name: String, description: String, date: LocalDateTime)
    extends MeetupEvent
