package cloud.quaer.product.api

import play.api.libs.json.Format
import play.api.libs.json.Json

case class ProductVariant()

object ProductVariant {
  implicit val format: Format[ProductVariant] = Json.format
}

case class ProductVariantResponse()

object ProductVariantResponse {
  implicit val format: Format[ProductVariantResponse] = Json.format
}

case class ProductVariantRequest()

object ProductVariantRequest {
  implicit val format: Format[ProductVariantRequest] = Json.format
}
