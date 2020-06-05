package com.mongo.consumer.kafka

import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.Properties

import com.mongo.consumer.mongo.MongoSinkClient
import com.typesafe.scalalogging.Logger
import org.apache.kafka.clients.consumer.{
  ConsumerConfig,
  ConsumerRecord,
  ConsumerRecords,
  KafkaConsumer
}

import scala.annotation.tailrec
import scala.jdk.CollectionConverters._

class MongoConsumer(mongoSinkClient: MongoSinkClient,
                    configuration: ConsumerConfiguration) {

  val logger: Logger = Logger(classOf[MongoConsumer])

  val consumer = new KafkaConsumer[Long, String](properties)

  def consume(bufferSize: Int = configuration.bufferSize): Unit = {
    consumer.subscribe(List(configuration.topic).asJava)

    @tailrec
    def bulkConsume(
      buffer: Seq[ConsumerRecord[Long, String]] = Seq()
    ): Nothing =
      buffer.size match {
        case size if size < bufferSize =>
          val records: ConsumerRecords[Long, String] =
            consumer.poll(Duration.of(100, ChronoUnit.MILLIS))
          bulkConsume(buffer ++ records.asScala)
        case _ =>
          mongoSinkClient.upsertMany(
            buffer.map(record => (record.key(), record.value()))
          )
          consumer.commitSync()
          bulkConsume()
      }

    try {
      bulkConsume()
    } catch {
      case e: Throwable => logger.error("Error while consuming messages", e)
    } finally {
      consumer.close()
    }
  }

  private def properties: Properties = {
    val properties = new Properties()
    properties.put(
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
      MongoConsumerConfiguration.servers
    )
    properties.put(
      ConsumerConfig.GROUP_ID_CONFIG,
      MongoConsumerConfiguration.groupId
    )
    properties.put(
      ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG,
      MongoConsumerConfiguration.allowAutoCreateTopic
    )
    properties.put(
      ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
      MongoConsumerConfiguration.enableAutoCommit
    )
    properties.put(
      ConsumerConfig.FETCH_MIN_BYTES_CONFIG,
      MongoConsumerConfiguration.fetchMinBytes
    )
    properties.put(
      ConsumerConfig.MAX_POLL_RECORDS_CONFIG,
      configuration.maxPollRecords
    )
    properties.put(
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
      MongoConsumerConfiguration.keyDeserializer
    )
    properties.put(
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
      MongoConsumerConfiguration.valueDeserializer
    )
    properties
  }

}

object MongoConsumer {
  def apply(mongoSinkClient: MongoSinkClient)(
    implicit configuration: ConsumerConfiguration = MongoConsumerConfiguration
  ): MongoConsumer = new MongoConsumer(mongoSinkClient, configuration)
}
