package com.coinbase.producer

import com.typesafe.config.{Config, ConfigFactory}

import scala.jdk.CollectionConverters._

object Configuration {
  private val config: Config = ConfigFactory.load()

  val websocketUri: String = config.getString("websocket.uri")

  val products: List[String] =
    config.getStringList("websocket.products").asScala.toList
}
