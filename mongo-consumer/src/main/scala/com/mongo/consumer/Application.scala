package com.mongo.consumer

import ch.qos.logback.classic.{Level, Logger}
import com.mongo.consumer.kafka.MongoConsumer
import com.mongo.consumer.mongo.MongoSinkClient
import org.slf4j.LoggerFactory

object Application extends App {
  LoggerFactory
    .getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)
    .asInstanceOf[Logger]
    .setLevel(Level.INFO)

  val mongoSinkClient = MongoSinkClient()
  MongoConsumer(mongoSinkClient).consume()
}
