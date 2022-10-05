package scafka.events

import play.api.libs.json._
import java.util.UUID
import scafka.events.Event._

object MeetupGroupCreated {
  implicit val format: OFormat[MeetupGroupCreated] = Json.format[MeetupGroupCreated]
}

case class MeetupGroupCreated(id: UUID, name: String, description: String) extends MeetupEvent
