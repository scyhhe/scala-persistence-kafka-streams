package scafka.events

import play.api.libs.json._
import java.util.UUID
import java.time.LocalDateTime

object MeetupEventUpdated {
  implicit val format: OFormat[MeetupEventUpdated] = Json.format[MeetupEventUpdated]
}

case class MeetupEventUpdated(id: UUID, groupId: UUID, name: String, description: String, date: LocalDateTime)
    extends Event
