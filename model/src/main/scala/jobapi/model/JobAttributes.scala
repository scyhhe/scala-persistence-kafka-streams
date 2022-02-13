package jobapi.model

case class JobAttributes(
    categories: List[Category],
    experience: Option[Experience],
    schedule: Option[Schedule],
    remote: Option[Remote],
    keywords: List[Keyword]
)