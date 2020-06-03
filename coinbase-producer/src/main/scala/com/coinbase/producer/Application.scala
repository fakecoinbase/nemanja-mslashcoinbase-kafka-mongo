package com.coinbase.producer

import com.coinbase.producer.kafka.MarketEventProducer
import com.coinbase.producer.ws.{
  CoinbaseWebsocketClient,
  MarketEventMessageHandler
}

object Application extends App {
  val coinbaseWebsocketClient: CoinbaseWebsocketClient =
    CoinbaseWebsocketClient()

  val marketEventProducer: MarketEventProducer = MarketEventProducer()

  val marketEventMessageHandler: MarketEventMessageHandler =
    MarketEventMessageHandler(marketEventProducer)

  coinbaseWebsocketClient.subscribe(marketEventMessageHandler)
}
