package cloud.quaer.product.api

import com.lightbend.lagom.scaladsl.api.deser.PathParamSerializer
import play.api.libs.json.Format
import play.api.libs.json.Json

case class ProductFilter(
    ids: Option[String],
    title: Option[String],
    vendor: Option[String],
    limit: Int = 50,
    page: Int = 0,
    productType: Option[String],
    status: Option[String],
    collectionId: Option[String]
)

object ProductFilter {
  implicit val format: Format[ProductFilter] = Json.format

  val PATH = "?ids&title&vendor&limit&page&productType&status&collectionId"

  private def serializer(productFilter: ProductFilter): String = Json.stringify(Json.toJson(productFilter))

  private def deserializer(str: String): ProductFilter = Json.fromJson(Json.parse(str)).get

  implicit val productFilterPathParamSerializer: PathParamSerializer[ProductFilter] = {
    PathParamSerializer.required[ProductFilter]("ProductFilter")(deserializer)(serializer)
  }
}
