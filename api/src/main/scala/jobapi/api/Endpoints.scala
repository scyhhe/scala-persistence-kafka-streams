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

  private val jobIdPathParameter    = path[UUID].name("jobID")
  private val tenantIdPathParameter = path[UUID].name("tenantID")
  private val jobResponseBody       = jsonBody[JobResponse].example(JobResponseExample.value)

  private val jobEndpoint = endpoint.in("tenants" / tenantIdPathParameter).tag("job")

  val getJob = jobEndpoint.get
    .description("This does something.")
    .in("jobs" / jobIdPathParameter)
    .out(jobResponseBody)

  val getJobs = jobEndpoint.get
    .in("jobs")
    .out(jsonBody[GetJobsResponse])

  val createJob = jobEndpoint.post
    .in("jobs")
    .in(jsonBody[CreateJobRequest])
    .out(jobResponseBody)

  implicit val format = Json.format[JsonPatch[JsValue]]

  val patchJob = jobEndpoint.patch
    .in("jobs" / jobIdPathParameter)
    .in(jsonBody[JsonPatch[JsValue]].example(JsonPatchExample.value))
    .out(jobResponseBody)

  val deleteJob = jobEndpoint.delete
    .in("jobs" / jobIdPathParameter)

  private val tenantEndpoint = endpoint.in("tenants" / tenantIdPathParameter).tag("tenant")

  val getTenant = tenantEndpoint.get.description("Tenant")

}
