package com.mongo.consumer.mongo

import com.typesafe.config.ConfigFactory

trait MongoSinkConfiguration {
  val connectionUri: String
  val database: String
  val collection: String
}

object MongoSinkConfiguration extends MongoSinkConfiguration {
  private val config = ConfigFactory.load()

  override val connectionUri: String = config.getString("mongo.database.uri")

  override val database: String = config.getString("mongo.database.name")

  override val collection: String =
    config.getString("mongo.database.collection")
}
