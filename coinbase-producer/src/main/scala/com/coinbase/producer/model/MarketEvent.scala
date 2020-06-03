package com.coinbase.producer.model

import java.text.SimpleDateFormat

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

case class MarketEvent(id: Long,
                       productId: String,
                       side: String,
                       price: BigDecimal,
                       size: BigDecimal,
                       timestamp: String)

object MarketEvent {

  private val timestampFormatter =
    new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss.ssssss'Z'")

  implicit val encoder: Encoder[MarketEvent] = deriveEncoder

  implicit val decoder: Decoder[MarketEvent] = Decoder.instance { rawJson =>
    {
      for {
        productId <- rawJson.get[String]("product_id")
        changes <- rawJson.get[List[List[String]]]("changes")
        time <- rawJson.get[String]("time")
      } yield
        MarketEvent(
          timestampFormatter.parse(time).getTime,
          productId,
          changes.head.head,
          BigDecimal(changes.head(1)),
          BigDecimal(changes.head(2)),
          time
        )
    }
  }

}
