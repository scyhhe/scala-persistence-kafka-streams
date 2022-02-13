package jobapi.api

import sttp.tapir.openapi.OpenAPI
import sttp.tapir.openapi.circe.yaml._

import java.nio.file._

private[api] object OpenApiGenerator {

  def main(args: Array[String]): Unit = {
    val jobApi = new ApiDefinition

    write("openapi", jobApi.openAPI)
  }

  private def write(name: String, api: OpenAPI): Unit = {
    val output = Paths.get(s"target/$name.yaml").toAbsolutePath()
    Files.writeString(output, api.toYaml)
  }

}
