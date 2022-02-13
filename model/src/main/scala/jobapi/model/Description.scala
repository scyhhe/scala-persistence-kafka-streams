package jobapi.model

case class Description(
    html: Option[Html],
    parts: List[DescriptionPart]
)
