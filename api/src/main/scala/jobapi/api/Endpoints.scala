package jobapi.api

import jobapi.api.model.JobRequest._
import jobapi.api.model._
import jobapi.model._
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.play._

object Endpoints {

  implicit val languageSchema: Schema[Language] = Schema.string
  implicit val localizationSchema: Schema[Map[Language, JobDetails]] = Schema(
    SchemaType.SOpenProduct[Map[Language, JobDetails], JobDetails](implicitly[Schema[JobDetails]])(
      _.map { case (k, v) => (k.value, v) }
    ),
    Some(Schema.SName("Map", List("Details")))
  )

  val getJob = endpoint.get
    .in("jobs")
    .out(jsonBody[JobRequest].example(JobRequestExample.value))

}
