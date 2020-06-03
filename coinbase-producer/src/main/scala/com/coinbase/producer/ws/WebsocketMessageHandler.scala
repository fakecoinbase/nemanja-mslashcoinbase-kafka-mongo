package com.coinbase.producer.ws

import com.coinbase.producer.kafka.MarketEventProducer
import com.coinbase.producer.model.MarketEvent
import com.typesafe.scalalogging.Logger
import io.circe.parser.decode

trait WebsocketMessageHandler {
  def onMessage: PartialFunction[String, Unit]
}

class CoinbaseWebsocketMessageHandler(producer: MarketEventProducer)
    extends WebsocketMessageHandler {

  val logger: Logger = Logger(classOf[CoinbaseWebsocketMessageHandler])

  override def onMessage: PartialFunction[String, Unit] = {
    case message: String =>
      decode[MarketEvent](message) match {
        case Right(event: MarketEvent) =>
          producer.publish(event)
          logger.info("Event published: " + event.toString)
        case Left(error) =>
          logger.error("Error while decoding market update event.", error)
      }
  }

}
