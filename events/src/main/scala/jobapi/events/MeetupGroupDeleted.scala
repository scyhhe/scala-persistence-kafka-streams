package scafka.events

import play.api.libs.json._
import java.util.UUID

object MeetupGroupDeleted {
  implicit val format: OFormat[MeetupGroupDeleted] = Json.format[MeetupGroupDeleted]
}

case class MeetupGroupDeleted(id: UUID) extends Event
