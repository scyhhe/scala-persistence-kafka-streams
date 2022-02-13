package jobapi.model

case class JobLinks(
    posting: Option[Url],
    apply: Option[Url]
)