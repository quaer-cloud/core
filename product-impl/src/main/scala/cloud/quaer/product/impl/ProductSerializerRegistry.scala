package cloud.quaer.product.impl

import com.lightbend.lagom.scaladsl.playjson.JsonSerializer
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry

object ProductSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(
    JsonSerializer[ProductState],
    // Replies
    JsonSerializer[Accepted],
    JsonSerializer[Rejected],
  )
}
