package scafka.events

import java.util.UUID
import java.time.LocalDateTime
import play.api.libs.json._

object MeetupState {
  private val zeroes = new UUID(0, 0)

  final case class User(id: UUID, name: String, status: UserRSVP.Status)
  final case class Event(id: UUID, name: String, description: String, date: LocalDateTime, users: List[User])

  object User {
    implicit val format: OFormat[User] = Json.format[User]
    val empty                          = User(zeroes, "", UserRSVP.Status.NotGoing)
  }

  object Event {
    implicit val format: OFormat[Event] = Json.format[Event]
    val empty                           = Event(zeroes, "", "", LocalDateTime.MIN, Nil)
  }

  implicit val format: OFormat[MeetupState] = Json.format[MeetupState]

  val empty = MeetupState(zeroes, "", "", Nil)
}

final case class MeetupState(id: UUID, name: String, description: String, events: List[MeetupState.Event])
