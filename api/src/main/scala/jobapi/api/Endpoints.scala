package jobapi.api

import jobapi.api.model.JobRequest._
import jobapi.api.model._
import jobapi.model._
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.play._

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

object Endpoints {

  implicit val languageSchema: Schema[Language] = Schema.string
  implicit val localizationSchema: Schema[Map[Language, JobDetails]] = Schema(
    SchemaType.SOpenProduct[Map[Language, JobDetails], JobDetails](implicitly[Schema[JobDetails]])(
      _.map { case (k, v) => (k.value, v) }
    ),
    Some(Schema.SName("Map", List("Details")))
  )

  val personExample = Person(
    id = Some(s"https://id.1brd.com/tenant/${UUID.randomUUID()}/user/${UUID.randomUUID()}"),
    email = Some("birdy.pigeon@firstbird.com"),
    firstName = Some("Birdy"),
    lastName = Some("Pigeon")
  )

  val jobRequestExample = JobRequest(
    UUID.randomUUID().toString(),
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
        // address = Some("Gertrude-Fröhlich-Sandner Straße 2-4, Tower 9, Spaces"),
        municipality = Some("Vienna"),
        countryCode = Some("AUT"),
        coordinates = Some(Coordinates(longitude = 48.186618, latitude = 16.375152))
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

  val getJob = endpoint.get
    .in("jobs")
    .out(jsonBody[JobRequest].example(jobRequestExample))

}
