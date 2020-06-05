package com.coinbase.producer.kafka

import com.typesafe.config.ConfigFactory

trait ProducerConfiguration {
  val topic: String
  val servers: String
  val keySerializer: String
  val valueSerializer: String
  val acks: String
  val batchSize: Int
  val lingerMs: Int
}

object MarketEventProducerConfiguration extends ProducerConfiguration {

  private val config = ConfigFactory.load()

  override val topic: String = config.getString("kafka.producer.topic")

  override val servers: String = config.getString("kafka.producer.servers")

  override val keySerializer: String =
    config.getString("kafka.producer.key.serializer")

  override val valueSerializer: String =
    config.getString("kafka.producer.value.serializer")

  override val acks: String = config.getString("kafka.producer.acks")

  override val batchSize: Int = config.getInt("kafka.producer.batch.size")

  override val lingerMs: Int = config.getInt("kafka.producer.linger.ms")
}
