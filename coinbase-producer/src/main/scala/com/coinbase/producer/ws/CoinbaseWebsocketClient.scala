package com.coinbase.producer.ws

import com.github.andyglow.websocket.WebsocketClient
import com.github.andyglow.websocket.WebsocketClient.Builder.Options
import com.github.andyglow.websocket.util.Uri
import com.typesafe.scalalogging.Logger
import io.circe.syntax._

class CoinbaseWebsocketClient(uri: String) {

  val logger: Logger = Logger(classOf[CoinbaseWebsocketClient])

  def subscribe(products: List[String],
                messageHandler: WebsocketMessageHandler): Unit = {
    val builder = new WebsocketClient.Builder[String](
      Uri(uri),
      messageHandler.onMessage,
      Options(
        maxFramePayloadLength = Int.MaxValue,
        exceptionHandler = exceptionHandler,
        closeHandler = closeHandler,
      )
    )

    val client = builder.build()
    val connection = client.open()

    connection ! subscriptionMessage(products)
  }

  private def subscriptionMessage(productIds: List[String]): String = {
    s"""
       |{
       |  "type": "subscribe",
       |  "channels": [{
       |    "name": "level2",
       |    "product_ids": ${productIds.asJson}
       |  }]
       |}
       |""".stripMargin
  }

  def exceptionHandler: PartialFunction[Throwable, Unit] = {
    case ex: Throwable => logger.error("Websocket connection exception.", ex)
  }

  def closeHandler: (Unit => Unit) = _ => {
    logger.info("Coinbase websocket connection closed.")
  }

}
