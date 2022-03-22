package jobapi.api.model

import play.api.libs.json.Json

object GetJobsResponse {
  implicit val format = Json.format[GetJobsResponse]
}

case class GetJobsResponse(
    items: List[JobResponse]
)
