package jobapi.model

case class Person(
    id: Option[String],
    email: Option[String],
    firstName: Option[String],
    lastName: Option[String]
)
