package scafka.events

import play.api.libs.json._
import java.util.UUID

sealed trait Event[A] {
  def id: A
}

object Event {
  trait MeetupEvent extends Event[MeetupId]
  trait UserEvent   extends Event[UserId]

  type UserId   = UUID
  type MeetupId = UUID

  val meetupFormats: OFormat[MeetupEvent] = OFormat(
    Reads.of[JsObject].flatMapResult[MeetupEvent] { jso =>
      (jso \ "type").as[String] match {
        case "meetup.group.created" => MeetupGroupCreated.format.reads(jso)
        case "meetup.group.updated" => MeetupGroupUpdated.format.reads(jso)
        case "meetup.group.deleted" => MeetupGroupDeleted.format.reads(jso)
        case "meetup.event.created" => MeetupEventCreated.format.reads(jso)
        case "meetup.event.updated" => MeetupEventUpdated.format.reads(jso)
        case "meetup.event.deleted" => MeetupEventDeleted.format.reads(jso)
      }
    },
    OWrites[MeetupEvent] {
      case e: MeetupGroupCreated => MeetupGroupCreated.format.writes(e) ++ Json.obj("type" -> "meetup.group.created")
      case e: MeetupGroupUpdated => MeetupGroupUpdated.format.writes(e) ++ Json.obj("type" -> "meetup.group.updated")
      case e: MeetupGroupDeleted => MeetupGroupDeleted.format.writes(e) ++ Json.obj("type" -> "meetup.group.deleted")
      case e: MeetupEventCreated => MeetupEventCreated.format.writes(e) ++ Json.obj("type" -> "meetup.event.created")
      case e: MeetupEventUpdated => MeetupEventUpdated.format.writes(e) ++ Json.obj("type" -> "meetup.event.updated")
      case e: MeetupEventDeleted => MeetupEventDeleted.format.writes(e) ++ Json.obj("type" -> "meetup.event.deleted")
    }
  )

  val userFormats: OFormat[UserEvent] = OFormat(
    Reads.of[JsObject].flatMapResult[UserEvent] { jso =>
      (jso \ "type").as[String] match {
        case "user.created" => UserCreated.format.reads(jso)
        case "user.rsvp"    => UserRSVP.format.reads(jso)
      }
    },
    OWrites[UserEvent] {
      case e: UserRSVP    => UserRSVP.format.writes(e) ++ Json.obj("type" -> "user.rsvp")
      case e: UserCreated => UserCreated.format.writes(e) ++ Json.obj("type" -> "user.created")
    }
  )
}
