package cloud.quaer.product.api.models

import cloud.quaer.product.api.models.Product.ProductType
import com.lightbend.lagom.scaladsl.api.deser.PathParamSerializer
import play.api.libs.json.Format
import play.api.libs.json.Json

sealed trait ApiEntity

case class ProductRequest(
    title: String,
    vendor: String,
    productType: ProductType,
    bodyHtml: String,
    slug: Option[String],
    status: Boolean,
    tags: Set[String]
) extends ApiEntity

object ProductRequest {
  implicit val format: Format[ProductRequest] = Json.format
}

case class ProductResponse(product: Option[Product]) extends ApiEntity

object ProductResponse {
  implicit val format: Format[ProductResponse] = Json.format
}

case class ProductCountResponse(total: Int)

object ProductCountResponse {
  implicit val format: Format[ProductCountResponse] = Json.format
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
    presentmentCurrencies: Option[String],
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
