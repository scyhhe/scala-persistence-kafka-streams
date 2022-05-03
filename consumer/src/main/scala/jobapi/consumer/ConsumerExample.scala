package jobapi.consumer

import jobapi.events.Implicits._
import jobapi.events.{JobCreated, JobEvent, JobTitleUpdated}
import jobapi.model.JobId
import org.apache.kafka.common.serialization.{Serde, Serdes}
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.KTable
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import play.api.libs.json.{Json, OFormat}

import java.util.Properties

object Job {
  implicit val format: OFormat[Job] = Json.format[Job]
}

case class Job(id: Option[JobId], title: Option[String])
object ConsumerExample extends App {

  val config: Properties = {
    val p = new Properties()
    p.put(StreamsConfig.APPLICATION_ID_CONFIG, "a")
    p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    p.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, "1000")
    p
  }

  val builder = new StreamsBuilder()

  implicit val jobIdSerde: Serde[JobId] =
    Serdes.serdeFrom((_, data) => data.value.getBytes("UTF-8"), (_, data) => JobId(new String(data)))
  implicit val jobEventSerde: Serde[JobEvent] = JsonSerialization.serde[JobEvent](JobEvent.formats)
  implicit val jobSerde: Serde[Job]           = JsonSerialization.serde[Job]

  val jobs: KTable[JobId, Job] = builder
    .stream[JobId, JobEvent]("jobs")
    .groupByKey
    .aggregate(Job(None, None)) { case (id, e, job) =>
      e match {
        case JobCreated(id)             => job.copy(id = Some(id))
        case JobTitleUpdated(id, title) => job.copy(title = Some(title))
      }
    }

  jobs.filter { case (_, job) => job.title.nonEmpty }.toStream.foreach { case (_, job) => println(job) }

  val streams = new KafkaStreams(builder.build(), config)

  streams.cleanUp()
  streams.start()

  Runtime
    .getRuntime()
    .addShutdownHook(new Thread() {
      override def run(): Unit = {
        streams.close()
      }
    })

}
