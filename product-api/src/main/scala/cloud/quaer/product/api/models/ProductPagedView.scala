package cloud.quaer.product.api.models

import play.api.libs.json.Format
import play.api.libs.json.Json

sealed trait PagedView

case class ProductPagedView(records: Set[Product]) extends PagedView

object ProductPagedView {
  implicit val format: Format[ProductPagedView] = Json.format
}
