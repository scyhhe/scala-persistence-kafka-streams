package scafka.events

import java.util.UUID
import java.time.LocalDateTime
import play.api.libs.json.Json
import play.api.libs.json.OFormat

object UserState {
  final case class AttendingEvent(id: UUID, name: String, description: String, date: LocalDateTime)

  object AttendingEvent {
    implicit val format: OFormat[AttendingEvent] = Json.format[AttendingEvent]
  }

  val empty: UserState                    = UserState(new UUID(0, 0), "", Nil)
  implicit val format: OFormat[UserState] = Json.format[UserState]
}

final case class UserState(id: UUID, name: String, attending: List[UserState.AttendingEvent])
