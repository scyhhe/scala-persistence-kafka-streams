package jobapi.api.model

import jobapi.api.model.ModelExamples._
import jobapi.model._

import java.time.Instant
import java.time.temporal.ChronoUnit

object JobResponseExample {
  val value = JobResponse(
    JobId.random(),
    organization = Some(
      Organization("Firstbird")
    ),
    organizationalUnit = Some(
      OrganizationalUnit("Technology Swarm")
    ),
    contact = Some(personExample),
    responsible = List(personExample),
    validity = Some(
      Validity(
        from = Some(Instant.now().minus(1, ChronoUnit.DAYS)),
        to = Some(Instant.now().plus(1, ChronoUnit.DAYS))
      )
    ),
    locations = List(
      Location(
        name = "Firstbird HQ",
        details = LocationDetails(
          municipality = Some("Vienna"),
          countryCode = Some("AUT"),
          coordinates = Some(Coordinates(longitude = 48.186618, latitude = 16.375152))
        )
      )
    ),
    externalId = Some("123"),
    links = JobLinks(
      posting = Some(Url("https://localhost")),
      `apply` = Some(Url("https://localhost"))
    ),
    metadata = Map(
      "x-firstbird-job-parsing" -> Map(
        "a" -> "b"
      )
    ),
    attributes = JobAttributes(
      categories = List(Category("Trainee")),
      experience = Some(Experience("senior")),
      schedule = Some(Schedule("full-time")),
      remote = Some(Remote("onsite")),
      keywords = List(
        Keyword("Java", 0.99),
        Keyword("Scala", 0.95)
      )
    ),
    localization = Map(
      Language("de") -> JobDetails(
        "Software Developer",
        skills = List(Skill("Java")),
        description = Description(
          html = Some(Html("<h1>Requirements</h1><ul><li>Java</li></ul>")),
          parts = List(
            DescriptionPart(name = "requirements",
                            title = Some("Requirements"),
                            html = Some(Html("<ul><li>Java</li></ul>")))
          )
        )
      )
    )
  )
}
