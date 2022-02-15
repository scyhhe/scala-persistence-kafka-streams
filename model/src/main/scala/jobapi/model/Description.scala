package jobapi.model

case class Description(
    html: Option[Html],
    parts: List[DescriptionPart]
)

object Description {
  val empty: Description = Description(Option.empty, List.empty)
}
