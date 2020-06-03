package com.coinbase.producer.ws

import com.coinbase.producer.kafka.MarketEventProducer
import com.coinbase.producer.model.MarketEvent
import com.typesafe.scalalogging.Logger
import io.circe.parser.decode

class MarketEventMessageHandler(producer: MarketEventProducer) {

  val logger: Logger = Logger(classOf[MarketEventMessageHandler])

  def onMessage: PartialFunction[String, Unit] = {
    case message: String =>
      decode[MarketEvent](message) match {
        case Left(error) =>
          logger.error("Error while decoding market update event.", error)
        case Right(event: MarketEvent) =>
          producer.publish(event)
          logger.info("Event published: " + event.toString)
      }
  }

}

object MarketEventMessageHandler {
  def apply(producer: MarketEventProducer): MarketEventMessageHandler =
    new MarketEventMessageHandler(producer)
}
