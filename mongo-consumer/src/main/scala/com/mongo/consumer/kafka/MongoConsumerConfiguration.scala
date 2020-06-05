package com.mongo.consumer.kafka

import com.typesafe.config.ConfigFactory

trait ConsumerConfiguration {
  val servers: String
  val topic: String
  val groupId: String
  val autoOffsetReset: String
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

  override val servers: String = config.getString("kafka.consumer.servers")

  override val topic: String = config.getString("kafka.consumer.topic")

  override val groupId: String = config.getString("kafka.consumer.group.id")

  override val autoOffsetReset: String =
    config.getString("kafka.consumer.auto.offset.reset")

  override val allowAutoCreateTopic: Boolean =
    config.getBoolean("kafka.consumer.allow.auto.create.topic")

  override val enableAutoCommit: Boolean =
    config.getBoolean("kafka.consumer.enable.auto.commit")

  override val fetchMinBytes: Int =
    config.getInt("kafka.consumer.fetch.min.bytes")

  override val maxPollRecords: Int =
    config.getInt("kafka.consumer.max.poll.records")

  override val bufferSize: Int = config.getInt("kafka.consumer.buffer.size")

  override val keyDeserializer: String =
    config.getString("kafka.consumer.deserializer.key")

  override val valueDeserializer: String =
    config.getString("kafka.consumer.deserializer.value")
}
