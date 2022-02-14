package jobapi.model

case class JobDetails(
    title: String,
    skills: List[Skill],
    description: Description
)

object JobDetails {
    def empty(title: String): JobDetails = JobDetails(title, List.empty, Description.empty)
}