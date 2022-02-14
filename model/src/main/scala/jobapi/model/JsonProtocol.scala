package jobapi.model

import play.api.libs.json.Json

trait JsonProtocol {
  implicit val htmlFormat               = Json.valueFormat[Html]
  implicit val descriptionPartFormat    = Json.format[DescriptionPart]
  implicit val descriptionFormat        = Json.format[Description]
  implicit val skillFormat              = Json.valueFormat[Skill]
  implicit val detailsFormat            = Json.format[JobDetails]
  implicit val languageFormat           = Json.valueFormat[Language]
  implicit val keyWordFormat            = Json.format[Keyword]
  implicit val remoteFormat             = Json.valueFormat[Remote]
  implicit val scheduleFormat           = Json.valueFormat[Schedule]
  implicit val experienceFormat         = Json.valueFormat[Experience]
  implicit val categoryFormat           = Json.valueFormat[Category]
  implicit val attributesFormat         = Json.format[JobAttributes]
  implicit val urlFormat                = Json.valueFormat[Url]
  implicit val linksFormat              = Json.format[JobLinks]
  implicit val coordinatesFormat        = Json.format[Coordinates]
  implicit val locationFormat           = Json.format[Location]
  implicit val validityFormat           = Json.format[Validity]
  implicit val personFormat             = Json.format[Person]
  implicit val organizationalUnitFormat = Json.format[OrganizationalUnit]
  implicit val organizationReads        = Json.format[Organization]
}
