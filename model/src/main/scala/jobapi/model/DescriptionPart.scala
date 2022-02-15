package jobapi.model

case class DescriptionPart(
    name: String,
    title: Option[String],
    html: Option[Html]
)
