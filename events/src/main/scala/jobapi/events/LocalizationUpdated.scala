package jobapi.events

import jobapi.model.{JobDetails, JobId, Language}

case class LocalizationUpdated(
    id: JobId,
    language: Language,
    details: JobDetails
)
