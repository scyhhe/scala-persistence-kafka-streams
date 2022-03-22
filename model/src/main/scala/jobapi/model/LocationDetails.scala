package jobapi.model

case class LocationDetails(
    municipality: Option[String],
    countryCode: Option[String],
    coordinates: Option[Coordinates]
)
