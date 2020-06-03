package com.coinbase.producer.kafka

import java.util.Properties

import com.coinbase.producer.model.MarketEvent
import io.circe.syntax._
import org.apache.kafka.clients.producer.{
  KafkaProducer,
  ProducerConfig,
  ProducerRecord
}

class MarketEventProducer(configuration: ProducerConfiguration) {

  private val producer = new KafkaProducer[Long, String](properties)

  def publish(event: MarketEvent): Unit = {
    val record =
      new ProducerRecord[Long, String](
        configuration.topic,
        event.id,
        event.asJson.noSpaces
      )
    producer.send(record)
  }

  private def properties: Properties = {
    val properties = new Properties()
    properties.put(
      ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
      configuration.servers
    )
    properties.put(
      ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
      configuration.keySerializer
    )
    properties.put(
      ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
      configuration.valueSerializer
    )
    properties.put(ProducerConfig.ACKS_CONFIG, configuration.acks)
    properties
  }
}

object MarketEventProducer {
  def apply(
    implicit configuration: ProducerConfiguration =
      MarketEventProducerConfiguration
  ): MarketEventProducer = new MarketEventProducer(configuration)
}
