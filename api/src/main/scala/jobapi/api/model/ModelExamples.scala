package jobapi.api.model

import jobapi.model.Person

import java.util.UUID

object ModelExamples {

  val personExample = Person(
    id = Some(s"https://id.1brd.com/tenant/${UUID.randomUUID()}/user/${UUID.randomUUID()}"),
    email = Some("birdy.pigeon@firstbird.com"),
    firstName = Some("Birdy"),
    lastName = Some("Pigeon")
  )
}
