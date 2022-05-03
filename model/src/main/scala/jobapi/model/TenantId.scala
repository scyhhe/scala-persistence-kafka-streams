package jobapi.model

import java.util.UUID

case class TenantId(value: String) extends AnyVal

object TenantId {
  def random(): TenantId = TenantId(UUID.randomUUID().toString())
}
