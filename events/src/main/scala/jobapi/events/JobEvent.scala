package jobapi.events

import play.api.libs.json._

trait JobEvent

object JobEvent {
  val formats: OFormat[JobEvent] = OFormat(
    Reads.of[JsObject].flatMapResult[JobEvent] { jso =>
      (jso \ "type").as[String] match {
        case "job.created"       => JobCreated.format.reads(jso)
        case "job.title.updated" => JobTitleUpdated.format.reads(jso)
      }
    },
    OWrites[JobEvent] {
      case e: JobCreated      => JobCreated.format.writes(e) ++ Json.obj("type" -> "job.created")
      case e: JobTitleUpdated => JobTitleUpdated.format.writes(e) ++ Json.obj("type" -> "job.title.updated")
    }
  )
}
