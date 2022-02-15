package jobapi.api.model

import jobapi.model.Implicits._
import jobapi.model._
import play.api.libs.json.{Format, Json, Reads, Writes}

object JobRequest {

  implicit val localizationFormat: Format[Map[Language, JobDetails]] = Format(
    Reads.of[Map[String, JobDetails]].map(x => x.map { case (key, value) => (Language(key), value) }),
    Writes
      .of[Map[String, JobDetails]]
      .contramap[Map[Language, JobDetails]](_.map { case (key, value) => (key.value, value) })
  )

  implicit val reads: Format[JobRequest] = Json.format[JobRequest]

}

case class JobRequest(
    id: String,
    organization: Option[Organization],
    organizationalUnit: Option[OrganizationalUnit],
    contact: Option[Person],
    responsible: List[Person],
    validity: Option[Validity],
    locations: List[Location],
    externalId: Option[String],
    links: JobLinks,
    metadata: Map[String, Map[String, String]],
    attributes: JobAttributes,
    localization: Map[Language, JobDetails]
)
