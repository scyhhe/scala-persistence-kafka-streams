package jobapi.api.model

import jobapi.model._
import play.api.libs.json.Format
import play.api.libs.json.Json
import play.api.libs.json.Writes

object JobRequest {

  implicit val htmlFormat            = Json.valueFormat[Html]
  implicit val descriptionPartFormat = Json.format[DescriptionPart]
  implicit val descriptionFormat     = Json.format[Description]
  implicit val skillFormat           = Json.valueFormat[Skill]
  implicit val detailsFormat         = Json.format[JobDetails]
  implicit val localizationFormat: Format[Map[Language, JobDetails]] = Format(
    Format.of[Map[String, JobDetails]].map(x => x.map { case (key, value) => (Language(key), value) }),
    Writes
      .of[Map[String, JobDetails]]
      .contramap[Map[Language, JobDetails]](_.map { case (key, value) => (key.value, value) })
  )
  implicit val languageFormat            = Json.valueFormat[Language]
  implicit val keyWordFormat             = Json.format[Keyword]
  implicit val remoteFormat              = Json.valueFormat[Remote]
  implicit val scheduleFormat            = Json.valueFormat[Schedule]
  implicit val experienceFormat          = Json.valueFormat[Experience]
  implicit val categoryFormat            = Json.valueFormat[Category]
  implicit val attributesFormat          = Json.format[JobAttributes]
  implicit val urlFormat                 = Json.valueFormat[Url]
  implicit val linksFormat               = Json.format[JobLinks]
  implicit val coordinatesFormat         = Json.format[Coordinates]
  implicit val locationFormat            = Json.format[Location]
  implicit val validityFormat            = Json.format[Validity]
  implicit val personFormat              = Json.format[Person]
  implicit val organizationalUnitFormat  = Json.format[OrganizationalUnit]
  implicit val organizationReads         = Json.format[Organization]
  implicit val reads: Format[JobRequest] = Json.format[JobRequest]

}

case class JobRequest(
    id: String,
    organization: Option[Organization],
    organizationalUnit: Option[OrganizationalUnit],
    contact: Person,
    responsible: List[Person],
    validity: Option[Validity],
    locations: List[Location],
    externalId: Option[String],
    links: JobLinks,
    metadata: Map[String, Map[String, String]],
    attributes: JobAttributes,
    localization: Map[Language, JobDetails]
)
