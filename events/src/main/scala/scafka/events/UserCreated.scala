package scafka.events

import play.api.libs.json.OFormat
import play.api.libs.json.Json
import java.util.UUID
import scafka.events.Event._

object UserCreated {
  implicit val format: OFormat[UserCreated] = Json.format[UserCreated]
}

case class UserCreated(id: UUID, name: String) extends UserEvent
