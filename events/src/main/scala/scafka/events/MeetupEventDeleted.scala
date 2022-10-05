package scafka.events

import play.api.libs.json._
import java.util.UUID
import scafka.events.Event._

object MeetupEventDeleted {
  implicit val format: OFormat[MeetupEventDeleted] = Json.format[MeetupEventDeleted]
}

case class MeetupEventDeleted(id: UUID, groupId: UUID) extends MeetupEvent
