package com.coinbase.producer.ws

import com.typesafe.config.{Config, ConfigFactory}

import scala.jdk.CollectionConverters._

trait WebsocketClientConfiguration {
  val uri: String
  val products: List[String]
}

object CoinbaseWebsocketClientConfiguration
    extends WebsocketClientConfiguration {

  private val config: Config = ConfigFactory.load()

  val uri: String = config.getString("ws.uri")

  val products: List[String] =
    config.getStringList("ws.products").asScala.toList
}
