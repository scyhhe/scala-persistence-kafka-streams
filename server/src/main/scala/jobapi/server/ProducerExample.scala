package jobapi.server

import jobapi.events.{JobEvent, JobTitleUpdated}
import jobapi.model.JobId
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import java.util.Properties

object Produce extends App {
  val config: Properties = {
    val p = new Properties()
    p.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    p
  }

  val producer = new KafkaProducer[JobId, JobEvent](
    config,
    (_: String, d: JobId) => d.value.getBytes("UTF-8"),
    JsonSerialization.serializer[JobEvent](JobEvent.formats)
  )
  val jobId = JobId("job-2")
  // val result   = producer.send(new ProducerRecord("jobs", jobId, JobCreated(jobId))).get()
  producer.send(new ProducerRecord("jobs", jobId, JobTitleUpdated(jobId, "Junior Software Developer"))).get()
}
