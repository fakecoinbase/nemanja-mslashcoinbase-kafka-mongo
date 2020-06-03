package com.coinbase.producer.ws

import com.coinbase.producer.model.MarketUpdateEvent
import com.typesafe.scalalogging.Logger
import io.circe.parser.decode

trait WebsocketMessageHandler {

  def onMessage: PartialFunction[String, Unit]

}

class CoinbaseWebsocketMessageHandler extends WebsocketMessageHandler {

  val logger: Logger = Logger(classOf[CoinbaseWebsocketMessageHandler])

  override def onMessage: PartialFunction[String, Unit] = {
    case message: String =>
      decode[MarketUpdateEvent](message) match {
        case Right(result) => logger.info(result.toString)
        case Left(error) =>
          logger.error("Error while decoding market update event.", error)
      }
  }

}
