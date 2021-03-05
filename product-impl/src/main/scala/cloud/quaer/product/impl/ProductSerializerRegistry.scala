package cloud.quaer.product.impl

import cloud.quaer.product.api._
import com.lightbend.lagom.scaladsl.playjson.JsonSerializer
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry

object ProductSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(
    JsonSerializer[Product],
    JsonSerializer[ProductState],
    JsonSerializer[ProductCreated],
    JsonSerializer[ProductUpdated],
    JsonSerializer[ProductDeleted],
    JsonSerializer[AcceptedResult],
    JsonSerializer[RejectedResult],
  )
}
