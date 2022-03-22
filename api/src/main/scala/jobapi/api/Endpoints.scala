package jobapi.api

import diffson.jsonpatch.JsonPatch
import diffson.playJson.DiffsonProtocol._
import diffson.playJson._
import jobapi.api.model.JobResponse._
import jobapi.api.model._
import jobapi.model._
import play.api.libs.json.{JsValue, Json}
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.play._

import java.util.UUID

object Endpoints {

  implicit val languageSchema: Schema[Language] = Schema.string
  implicit val localizationSchema: Schema[Map[Language, JobDetails]] = Schema(
    SchemaType.SOpenProduct[Map[Language, JobDetails], JobDetails](implicitly[Schema[JobDetails]])(
      _.map { case (k, v) => (k.value, v) }
    ),
    Some(Schema.SName("Map", List("Details")))
  )

  private val jobIdPathParameter = path[UUID].name("jobID")
  private val jobResponseBody    = jsonBody[JobResponse].example(JobResponseExample.value)

  val getJob = endpoint.get
    .in("jobs" / jobIdPathParameter)
    .out(jobResponseBody)

  val getJobs = endpoint.get
    .in("jobs")
    .out(jsonBody[GetJobsResponse])

  val createJob = endpoint.post
    .in("jobs")
    .in(jsonBody[CreateJobRequest])
    .out(jobResponseBody)

  implicit val format = Json.format[JsonPatch[JsValue]]

  val patchJob = endpoint.patch
    .in("jobs" / jobIdPathParameter)
    .in(jsonBody[JsonPatch[JsValue]].example(JsonPatchExample.value))
    .out(jobResponseBody)

  val deleteJob = endpoint.delete
    .in("jobs" / jobIdPathParameter)

}
