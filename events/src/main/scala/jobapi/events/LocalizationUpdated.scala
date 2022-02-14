package jobapi.events

import jobapi.model.JobId
import jobapi.model.Language
import jobapi.model.JobDetails

case class LocalizationUpdated(
    id: JobId,
    language: Language,
    details: JobDetails
)
