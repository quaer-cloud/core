package cloud.quaer.product.impl.repository

import cloud.quaer.product.impl.Event
import cloud.quaer.product.impl.ProductAdded
import com.lightbend.lagom.scaladsl.persistence.slick.SlickReadSide
import com.lightbend.lagom.scaladsl.persistence.AggregateEventTag
import com.lightbend.lagom.scaladsl.persistence.ReadSideProcessor

class ProductReportProcessor(readSide: SlickReadSide, repository: ProductReportRepository)
    extends ReadSideProcessor[Event] {
  override def buildHandler(): ReadSideProcessor.ReadSideHandler[Event] =
    readSide
      .builder[Event]("product-event")
      .setGlobalPrepare(repository.createTable)
      .setEventHandler[ProductAdded](envelope => repository.createProduct(envelope.event.product))
      .build()

  override def aggregateTags: Set[AggregateEventTag[Event]] = Event.Tag.allTags
}
