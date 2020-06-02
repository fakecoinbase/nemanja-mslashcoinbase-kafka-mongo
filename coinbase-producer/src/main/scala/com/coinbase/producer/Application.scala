package com.coinbase.producer

import com.typesafe.scalalogging.Logger

object Application extends App {
  val logger = Logger("coinbase-producer")

  val coinbaseWebsocketClient =
    new CoinbaseWebsocketClient(Configuration.websocketUri, {
      case message => logger.info(message)
    })

  coinbaseWebsocketClient.subscribe(Configuration.products)
}
