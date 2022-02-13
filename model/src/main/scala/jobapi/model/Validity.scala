package jobapi.model

import java.time.Instant

case class Validity(
    from: Option[Instant],
    to: Option[Instant]
)
