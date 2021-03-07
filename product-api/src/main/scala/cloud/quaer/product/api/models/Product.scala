package cloud.quaer.product.api.models

import cloud.quaer.product.api.models.Product._
import play.api.libs.json.Format
import play.api.libs.json.Json

import java.time.Instant
import java.util.UUID

case class Product(
    id: UUID,
    title: String,
    vendor: String,
    productType: ProductType,
    bodyHtml: String,
    slug: String,
    updatedAt: Instant,
    publishedAt: Instant,
    status: Option[Boolean],
    tags: Set[String],
    presentmentCurrencies: Option[String]
)

object Product {
  type ProductType = String
  type Vendor      = String

  implicit val format: Format[Product] = Json.format
}
