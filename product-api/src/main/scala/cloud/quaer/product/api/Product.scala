package cloud.quaer.product.api

import play.api.libs.json.{Format, Json}

import java.util.UUID

case class Product(id: UUID, name: String)

object Product {
  implicit val format: Format[Product] = Json.format
}

case class ProductResponse(products: Set[Product], size: Int, page: Int)

object ProductResponse {
  implicit val format: Format[ProductResponse] = Json.format
}
