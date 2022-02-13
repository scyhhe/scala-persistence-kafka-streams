package jobapi.api

import sttp.tapir._
import sttp.tapir.docs.openapi._
import sttp.tapir.openapi._

private[api] class ApiDefinition {

  private val endpoints: List[Endpoint[_, _, _, _]] = List(
    Endpoints.getJob
  )

  private val info: Info = Info(
    "Job Management API",
    "1.0.0",
    Some("Management API"),
    None,
    Some(
      Contact(
        Some("Firstbird Development Team"),
        Some("dev@firstbird.com"),
        Some("https://firstbird.com")
      ))
  )

  private val servers: List[Server] = List(
    Server("https://jobs.services.qa-1brd.com").description("QA"),
    Server("https://jobs.services.staging-1brd.com").description("Staging"),
    Server("https://jobs.services.1brd.com").description("Production")
  )

  val openAPI: OpenAPI = OpenAPIDocsInterpreter().toOpenAPI(endpoints, info).servers(servers)

}
