package jobapi.model

import java.util.UUID

case class JobId(value: String) extends AnyVal
object JobId {
  def random(): JobId = JobId(UUID.randomUUID().toString())
}
