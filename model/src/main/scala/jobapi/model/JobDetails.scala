package jobapi.model

case class JobDetails(
    title: String,
    skills: List[Skill],
    description: Description
)