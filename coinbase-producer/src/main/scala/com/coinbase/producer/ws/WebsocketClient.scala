package com.coinbase.producer.ws

import com.github.andyglow.websocket.WebsocketClient
import com.github.andyglow.websocket.WebsocketClient.Builder.Options
import com.github.andyglow.websocket.util.Uri
import com.typesafe.scalalogging.Logger
import io.circe.syntax._

trait WebsocketClient {

  def subscribe(products: List[String],
                messageHandler: WebsocketMessageHandler): Unit

  def exceptionHandler: PartialFunction[Throwable, Unit]

  def closeHandler: Unit => Unit

}

class CoinbaseWebsocketClient(uri: String) extends WebsocketClient {

  val logger: Logger = Logger(classOf[CoinbaseWebsocketClient])

  override def subscribe(products: List[String],
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

  override def exceptionHandler: PartialFunction[Throwable, Unit] = {
    case ex: Throwable => logger.error("Websocket connection exception.", ex)
  }

  override def closeHandler: (Unit => Unit) = _ => {
    logger.info("Coinbase websocket connection closed.")
  }

}
