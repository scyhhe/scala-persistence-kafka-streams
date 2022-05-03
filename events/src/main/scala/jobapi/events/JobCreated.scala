package jobapi.events

import jobapi.model.Implicits._
import jobapi.model.JobId
import play.api.libs.json._
object JobCreated {
  implicit val format: OFormat[JobCreated] = Json.format[JobCreated]
}

case class JobCreated(id: JobId) extends JobEvent
