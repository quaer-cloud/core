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
    updatedAt: Option[Instant],
    publishedAt: Instant,
    status: Option[Boolean]
)

object Product {
  type ProductType = String
  type Vendor      = String

  implicit val format: Format[Product] = Json.format

  def tupled(t: (UUID, String, String, ProductType, String, String, Option[Instant], Instant, Option[Boolean])) =
    Product(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9)
}
