package com.coinbase.producer.model

import java.text.SimpleDateFormat

import io.circe.Decoder

case class MarketUpdateEvent(productId: String,
                             side: String,
                             price: BigDecimal,
                             size: BigDecimal,
                             timestamp: Long)

object MarketUpdateEvent {

  private val timestampFormatter =
    new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss.ssssss'Z'")

  implicit val decoder: Decoder[MarketUpdateEvent] = Decoder.instance {
    rawJson =>
      {
        for {
          productId <- rawJson.get[String]("product_id")
          changes <- rawJson.get[List[List[String]]]("changes")
          time <- rawJson.get[String]("time")
        } yield
          MarketUpdateEvent(
            productId,
            changes.head.head,
            BigDecimal(changes.head(1)),
            BigDecimal(changes.head(2)),
            timestampFormatter.parse(time).getTime
          )
      }
  }

}
