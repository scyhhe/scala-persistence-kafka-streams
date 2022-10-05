package scafka.events

import play.api.libs.json._
import java.util.UUID
import java.time.LocalDateTime
import scafka.events.Event._

object UserRSVP {

  sealed abstract class Status(val value: String)

  object Status {
    case object Going    extends Status("GOING")
    case object Maybe    extends Status("MAYBE")
    case object NotGoing extends Status("NOT_GOING")

    def fromString(value: String): Status = value.toUpperCase match {
      case Going.value    => Going
      case Maybe.value    => Maybe
      case NotGoing.value => NotGoing
    }

    implicit val format = Format.of[String].bimap(Status.fromString(_), (status: Status) => status.value)
  }
  implicit val format: OFormat[UserRSVP] = Json.format[UserRSVP]
}

case class UserRSVP(id: UUID, eventId: UUID, status: UserRSVP.Status) extends UserEvent
