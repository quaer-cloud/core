package cloud.quaer.product.impl

import cloud.quaer.product.api.models.Product
import com.lightbend.lagom.scaladsl.playjson.JsonSerializer
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry

object ProductSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(
    JsonSerializer[ProductState],
    JsonSerializer[Product],
    // Replies
    JsonSerializer[Accepted],
    JsonSerializer[Rejected],
    JsonSerializer[ProductAdded],
  )
}
