package cloud.quaer.product.impl

import akka.actor.typed.ActorRef
import akka.cluster.sharding.typed.scaladsl.EntityTypeKey
import akka.persistence.typed.scaladsl.ReplyEffect
import cloud.quaer.product.api.CountQueryFilter
import cloud.quaer.product.api.Product
import cloud.quaer.product.api.ProductQueryFilter
import play.api.libs.json.Format
import play.api.libs.json.Json

case class ProductState(products: Set[Product]) {

  /**
   * Defines how to handle command by producing Effects e.g. persisting events, stopping the persistent actor.
   *
   * @param command ProductCommand
   * @return Answer to the command
   */
  def handleCommand(command: ProductCommand): ReplyEffect[ProductEvent, ProductState] = {
    command match {
      case GetProducts(query, replyTo)     => handleGetProducts(query, replyTo)
      case CountProducts(query, replyTo)   => handleCountProducts(query, replyTo)
      case GetProduct(replyTo)             => handleGetProduct(replyTo)
      case CreateProduct(product, replyTo) => handleCreateProduct(product, replyTo)
      case UpdateProduct(product, replyTo) => handleUpdateProduct(product, replyTo)
      case DeleteProduct(replyTo)          => handleDeleteProduct(replyTo)
    }
  }

  // command handlers
  private def handleGetProducts(
      query: ProductQueryFilter,
      replyTo: ActorRef[CommandResult]
  ): ReplyEffect[ProductEvent, ProductState] = ???

  private def handleCountProducts(
      query: CountQueryFilter,
      replyTo: ActorRef[CommandResult]
  ): ReplyEffect[ProductEvent, ProductState] = ???

  private def handleGetProduct(replyTo: ActorRef[CommandResult]): ReplyEffect[ProductEvent, ProductState] = ???

  private def handleCreateProduct(
      product: Product,
      replyTo: ActorRef[CommandResult]
  ): ReplyEffect[ProductEvent, ProductState] = ???

  private def handleUpdateProduct(
      product: Product,
      replyTo: ActorRef[CommandResult]
  ): ReplyEffect[ProductEvent, ProductState] = ???

  private def handleDeleteProduct(replyTo: ActorRef[CommandResult]): ReplyEffect[ProductEvent, ProductState] = ???

  /**
   * Returns the new state given the current state when an event has been persisted.
   *
   * @param event Event
   * @return Current State
   */
  def handleEvent(event: ProductEvent): ProductState = {
    event match {
      case ProductCreated(product) => handleProductUpdated(product)
      case ProductUpdated(product) => handleProductUpdated(product)
      case ProductDeleted(product) => handleProductDeleted(product)
    }
  }

  final def handleProductDeleted(product: Product): ProductState = handleProductUpdated(
    product.copy(status = Some(false))
  )

  final def handleProductUpdated(product: Product): ProductState = {
    val newState = products.filterNot(p => p.id == product.id)
    newState ++ product

    copy(newState)
  }

}

object ProductState {
  val typeKey: EntityTypeKey[ProductCommand] = EntityTypeKey[ProductCommand]("ProductAggregate")

  def initial: ProductState = ProductState(Set.empty)

  implicit val format: Format[ProductState] = Json.format
}
