package jobapi.events

import jobapi.model.JobId
import play.api.libs.json.Format

object Implicits {
  implicit val jobIdFormat: Format[JobId] = Format.of[String].bimap(JobId(_), _.value)
}
