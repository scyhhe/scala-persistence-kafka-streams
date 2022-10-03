package scafka.events

import play.api.libs.json._

trait Event

object Event {
  val formats: OFormat[Event] = OFormat(
    Reads.of[JsObject].flatMapResult[Event] { jso =>
      (jso \ "type").as[String] match {
        case "meetup.group.created"   => MeetupGroupCreated.format.reads(jso)
        case "meetup.group.updated"   => MeetupGroupUpdated.format.reads(jso)
        case "meetup.group.deleted"   => MeetupGroupDeleted.format.reads(jso)
        case "meetup.event.created"   => MeetupEventCreated.format.reads(jso)
        case "meetup.event.updated"   => MeetupEventUpdated.format.reads(jso)
        case "meetup.event.deleted"   => MeetupEventDeleted.format.reads(jso)
        case "meetup.event.user.rsvp" => MeetupUserRSPV.format.reads(jso)
      }
    },
    OWrites[Event] {
      case e: MeetupGroupCreated => MeetupGroupCreated.format.writes(e) ++ Json.obj("type" -> "meetup.group.created")
      case e: MeetupGroupUpdated => MeetupGroupUpdated.format.writes(e) ++ Json.obj("type" -> "meetup.group.updated")
      case e: MeetupGroupDeleted => MeetupGroupDeleted.format.writes(e) ++ Json.obj("type" -> "meetup.group.deleted")
      case e: MeetupEventCreated => MeetupEventCreated.format.writes(e) ++ Json.obj("type" -> "meetup.event.created")
      case e: MeetupEventUpdated => MeetupEventUpdated.format.writes(e) ++ Json.obj("type" -> "meetup.event.updated")
      case e: MeetupEventDeleted => MeetupEventDeleted.format.writes(e) ++ Json.obj("type" -> "meetup.event.deleted")
      case e: MeetupUserRSPV     => MeetupUserRSPV.format.writes(e) ++ Json.obj("type" -> "meetup.event.user.rsvp")
    }
  )
}
