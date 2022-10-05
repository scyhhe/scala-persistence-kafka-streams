package scafka.events

import play.api.libs.json._
import java.util.UUID
import scafka.events.Event._

object MeetupGroupUpdated {
  implicit val format: OFormat[MeetupGroupUpdated] = Json.format[MeetupGroupUpdated]
}

case class MeetupGroupUpdated(id: UUID, name: String, description: String) extends MeetupEvent
