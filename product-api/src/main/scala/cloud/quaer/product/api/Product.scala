package cloud.quaer.product.api

import cloud.quaer.product.api.Product._
import com.lightbend.lagom.scaladsl.api.deser.PathParamSerializer
import play.api.libs.json.Format
import play.api.libs.json.Json

import java.time.Instant
import java.util.Currency
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
    presentmentCurrencies: Option[Currency]
)

object Product {
  type ProductType = String
  type Vendor      = String

  implicit val format: Format[Product] = Json.format
}

case class ProductRequest(
    title: String,
    vendor: String,
    productType: ProductType,
    bodyHtml: String,
    slug: String,
    status: Boolean,
    tags: Set[String]
)

object ProductRequest {
  implicit val format: Format[ProductRequest] = Json.format
}

case class ProductResponse(products: Set[Product], size: Int, page: Int)

object ProductResponse {
  implicit val format: Format[ProductResponse] = Json.format
}

case class ProductCount(total: Int)

object ProductCount {
  implicit val format: Format[ProductCount] = Json.format
}

sealed trait QueryFilter

case class ProductQueryFilter(
    title: Option[String],
    vendor: Option[String],
    limit: Int = 50,
    page: Int = 0,
    productType: Option[String],
    status: Option[String],
    collectionId: Option[String],
    publishedStatus: Option[String],
    presentmentCurrencies: Option[Currency],
) extends QueryFilter

object ProductQueryFilter {
  implicit val format: Format[ProductQueryFilter] = Json.format

  val PATH = "?title&vendor&limit&page&productType&status&collectionId&publishedStatus&presentmentCurrencies"

  private def serializer(productFilter: ProductQueryFilter): String = Json.stringify(Json.toJson(productFilter))

  private def deserializer(str: String): ProductQueryFilter = Json.fromJson(Json.parse(str)).get

  implicit val productFilterPathParamSerializer: PathParamSerializer[ProductQueryFilter] = {
    PathParamSerializer.required[ProductQueryFilter]("ProductQueryFilter")(deserializer)(serializer)
  }
}

case class CountQueryFilter(
    title: Option[String],
    vendor: Option[String],
    productType: Option[String],
    status: Option[String],
    collectionId: Option[String],
    publishedStatus: Option[String]
) extends QueryFilter

object CountQueryFilter {
  implicit val format: Format[CountQueryFilter] = Json.format

  val PATH = "?title&vendor&productType&status&collectionId&publishedStatus"

  private def serializer(productCountQueryFilter: CountQueryFilter): String =
    Json.stringify(Json.toJson(productCountQueryFilter))

  private def deserializer(str: String): CountQueryFilter = Json.fromJson(Json.parse(str)).get

  implicit val productFilterPathParamSerializer: PathParamSerializer[CountQueryFilter] = {
    PathParamSerializer.required[CountQueryFilter]("CountQueryFilter")(deserializer)(serializer)
  }
}
