package jobapi.model

case class Location(
    name: String,
    municipality: Option[String],
    countryCode: Option[String],
    coordinates: Option[Coordinates]
)
