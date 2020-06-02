package com.coinbase.producer

import com.github.andyglow.websocket.WebsocketClient
import com.github.andyglow.websocket.WebsocketClient.Builder.Options
import com.github.andyglow.websocket.util.Uri
import com.typesafe.scalalogging.Logger
import org.json4s.DefaultFormats
import org.json4s.native.Serialization

class CoinbaseWebsocketClient(uri: String,
                              onMessage: PartialFunction[String, Unit]) {

  val logger: Logger = Logger(classOf[CoinbaseWebsocketClient])

  def subscribe(products: List[String]): Unit = {
    val builder = new WebsocketClient.Builder[String](
      Uri(uri),
      onMessage,
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
    val message = Map(
      "type" -> "subscribe",
      "channels" -> List(Map("name" -> "level2", "product_ids" -> productIds))
    )

    Serialization.write(message)(DefaultFormats)
  }

  private def exceptionHandler: PartialFunction[Throwable, Unit] = {
    case ex: Throwable => logger.error("Websocket connection exception.", ex)
  }

  private def closeHandler: (Unit => Unit) = _ => {
    logger.info("Coinbase websocket connection closed.")
  }

}
