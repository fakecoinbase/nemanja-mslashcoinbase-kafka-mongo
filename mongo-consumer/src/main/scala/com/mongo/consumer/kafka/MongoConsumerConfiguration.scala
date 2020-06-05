package com.mongo.consumer.kafka

import com.typesafe.config.ConfigFactory

trait ConsumerConfiguration {
  val servers: String
  val topic: String
  val groupId: String
  val allowAutoCreateTopic: Boolean
  val enableAutoCommit: Boolean
  val fetchMinBytes: Int
  val maxPollRecords: Int
  val bufferSize: Int
  val keyDeserializer: String
  val valueDeserializer: String
}

object MongoConsumerConfiguration extends ConsumerConfiguration {

  private val config = ConfigFactory.load()

  val servers: String = config.getString("kafka.consumer.servers")

  val topic: String = config.getString("kafka.consumer.topic")

  val groupId: String = config.getString("kafka.consumer.group.id")

  val allowAutoCreateTopic: Boolean =
    config.getBoolean("kafka.consumer.allow.auto.create.topic")

  val enableAutoCommit: Boolean =
    config.getBoolean("kafka.consumer.enable.auto.commit")

  val fetchMinBytes: Int = config.getInt("kafka.consumer.fetch.min.bytes")

  val maxPollRecords: Int = config.getInt("kafka.consumer.max.poll.records")

  val bufferSize: Int = config.getInt("kafka.consumer.buffer.size")

  val keyDeserializer: String =
    config.getString("kafka.consumer.deserializer.key")

  val valueDeserializer: String =
    config.getString("kafka.consumer.deserializer.value")

}
