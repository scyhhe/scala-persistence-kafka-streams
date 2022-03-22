package jobapi.api.model

import jobapi.model.Implicits._
import jobapi.model.JobId
import play.api.libs.json.{Json, OFormat}
object CreateJobRequest {
  implicit val format: OFormat[CreateJobRequest] = Json.format[CreateJobRequest]
}

case class CreateJobRequest(
    id: Option[JobId],
    externalId: Option[String]
)
