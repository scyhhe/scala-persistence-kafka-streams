package jobapi.model

case class Location(
    name: String,
    address: Option[String],
    city: Option[String],
    country: Option[String],
    coordinates: Option[Coordinates]
)