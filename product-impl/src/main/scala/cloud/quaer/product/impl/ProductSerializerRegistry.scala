package cloud.quaer.product.impl

import cloud.quaer.product.api._
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

object ProductSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(
    JsonSerializer[ProductState],
    JsonSerializer[Product],
    JsonSerializer[ProductResponse],
    JsonSerializer[ProductCount],
    JsonSerializer[CountQueryFilter],
    JsonSerializer[ProductQueryFilter],
    JsonSerializer[ProductRequest]
  )
}
