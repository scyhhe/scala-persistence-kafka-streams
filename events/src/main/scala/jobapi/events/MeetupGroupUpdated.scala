package scafka.events

import play.api.libs.json._
import java.util.UUID

object MeetupGroupUpdated {
  implicit val format: OFormat[MeetupGroupUpdated] = Json.format[MeetupGroupUpdated]
}

case class MeetupGroupUpdated(id: UUID, name: String, description: String) extends Event
