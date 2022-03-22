package jobapi.model

import play.api.libs.json.{Format, Json, OFormat}

trait JsonProtocol {
  implicit val jobIdFormat: Format[JobId]                            = Json.valueFormat[JobId]
  implicit val htmlFormat: Format[Html]                              = Json.valueFormat[Html]
  implicit val descriptionPartFormat: OFormat[DescriptionPart]       = Json.format[DescriptionPart]
  implicit val descriptionFormat: OFormat[Description]               = Json.format[Description]
  implicit val skillFormat: Format[Skill]                            = Json.valueFormat[Skill]
  implicit val detailsFormat: OFormat[JobDetails]                    = Json.format[JobDetails]
  implicit val languageFormat: Format[Language]                      = Json.valueFormat[Language]
  implicit val keyWordFormat: OFormat[Keyword]                       = Json.format[Keyword]
  implicit val remoteFormat: Format[Remote]                          = Json.valueFormat[Remote]
  implicit val scheduleFormat: Format[Schedule]                      = Json.valueFormat[Schedule]
  implicit val experienceFormat: Format[Experience]                  = Json.valueFormat[Experience]
  implicit val categoryFormat: Format[Category]                      = Json.valueFormat[Category]
  implicit val attributesFormat: OFormat[JobAttributes]              = Json.format[JobAttributes]
  implicit val urlFormat: Format[Url]                                = Json.valueFormat[Url]
  implicit val linksFormat: OFormat[JobLinks]                        = Json.format[JobLinks]
  implicit val coordinatesFormat: OFormat[Coordinates]               = Json.format[Coordinates]
  implicit val locationDetailsFormat: OFormat[LocationDetails]       = Json.format[LocationDetails]
  implicit val locationFormat: OFormat[Location]                     = Json.format[Location]
  implicit val validityFormat: OFormat[Validity]                     = Json.format[Validity]
  implicit val personFormat: OFormat[Person]                         = Json.format[Person]
  implicit val organizationalUnitFormat: OFormat[OrganizationalUnit] = Json.format[OrganizationalUnit]
  implicit val organizationReads: OFormat[Organization]              = Json.format[Organization]
}
