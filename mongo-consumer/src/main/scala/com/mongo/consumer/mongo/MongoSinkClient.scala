package com.mongo.consumer.mongo

import com.typesafe.scalalogging.Logger
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.model.{
  BulkWriteOptions,
  Filters,
  ReplaceOneModel,
  ReplaceOptions
}
import org.mongodb.scala.{
  BulkWriteResult,
  MongoClient,
  MongoCollection,
  Observer
}

class MongoSinkClient(configuration: MongoSinkConfiguration) {

  val logger: Logger = Logger(classOf[MongoSinkClient])

  val mongoClient: MongoClient = MongoClient(configuration.connectionUri)

  val marketEventsCollection: MongoCollection[Document] =
    mongoClient
      .getDatabase(configuration.database)
      .getCollection(configuration.collection)

  def upsertMany(records: Seq[(Long, String)]): Unit = {
    val writes = records.map {
      case (key: Long, value: String) =>
        ReplaceOneModel(
          Filters.eq("_id", key),
          Document(value),
          ReplaceOptions().upsert(true)
        )
    }

    marketEventsCollection
      .bulkWrite(writes, BulkWriteOptions().ordered(false))
      .subscribe(new Observer[BulkWriteResult] {
        override def onNext(result: BulkWriteResult): Unit =
          logger.info(s"Upserted ${writes.size} market events")

        override def onError(e: Throwable): Unit =
          logger.error("Failed to upsert market event documents", e)

        override def onComplete(): Unit = {}
      })
  }

}

object MongoSinkClient {
  def apply(
    implicit configuration: MongoSinkConfiguration = MongoSinkConfiguration
  ) = new MongoSinkClient(configuration)
}
