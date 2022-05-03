package jobapi.events

import jobapi.model.Implicits._
import jobapi.model.JobId
import play.api.libs.json._
object JobTitleUpdated {
  implicit val format: OFormat[JobTitleUpdated] = Json.format[JobTitleUpdated]
}

case class JobTitleUpdated(id: JobId, title: String) extends JobEvent
