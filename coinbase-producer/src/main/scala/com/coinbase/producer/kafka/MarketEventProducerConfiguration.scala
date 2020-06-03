package com.coinbase.producer.kafka

import com.typesafe.config.ConfigFactory

object MarketEventProducerConfiguration {

  private val config = ConfigFactory.load()

  val topic: String = config.getString("kafka.producer.topic")

  val servers: String = config.getString("kafka.producer.servers")

  val keySerializer: String = config.getString("kafka.producer.key.serializer")

  val valueSerializer: String =
    config.getString("kafka.producer.value.serializer")

  val acks: String = config.getString("kafka.producer.acks")

}
