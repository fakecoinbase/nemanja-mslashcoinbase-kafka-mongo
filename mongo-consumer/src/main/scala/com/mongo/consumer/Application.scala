package com.mongo.consumer

import com.mongo.consumer.kafka.MongoConsumer
import com.mongo.consumer.mongo.MongoSinkClient

object Application extends App {
  val mongoSinkClient = MongoSinkClient()
  MongoConsumer(mongoSinkClient).consume()
}
