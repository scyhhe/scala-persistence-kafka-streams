package jobapi.api.model

import jobapi.model.TenantId

sealed trait PostProcessor

object PostProcessor {
  case class LocationGeocoding() extends PostProcessor
  case class DescriptionParser() extends PostProcessor
}

case class TenantResponse(
    id: TenantId
)
