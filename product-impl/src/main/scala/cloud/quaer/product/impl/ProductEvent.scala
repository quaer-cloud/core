package cloud.quaer.product.impl

import cloud.quaer.product.api.Product
import com.lightbend.lagom.scaladsl.persistence.AggregateEvent
import com.lightbend.lagom.scaladsl.persistence.AggregateEventTag
import play.api.libs.json.Format
import play.api.libs.json.Json

/**
 * This interface defines all the events that the AccountServiceAggregate supports.
 */
sealed trait ProductEvent extends AggregateEvent[ProductEvent] {
  def aggregateTag: AggregateEventTag[ProductEvent] = ProductEvent.Tag
}

object ProductEvent {
  val Tag: AggregateEventTag[ProductEvent] = AggregateEventTag[ProductEvent]
}

final case class ProductCreated(product: Product) extends ProductEvent

object ProductCreated {
  implicit val format: Format[ProductCreated] = Json.format
}

final case class ProductUpdated(product: Product) extends ProductEvent

object ProductUpdated {
  implicit val format: Format[ProductUpdated] = Json.format
}

final case class ProductDeleted(product: Product) extends ProductEvent

object ProductDeleted {
  implicit val format: Format[ProductDeleted] = Json.format
}
