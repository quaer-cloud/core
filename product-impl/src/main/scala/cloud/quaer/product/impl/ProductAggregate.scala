package cloud.quaer.product.impl

import akka.actor.typed.Behavior
import akka.cluster.sharding.typed.scaladsl._
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.{EventSourcedBehavior, ReplyEffect}
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, AkkaTaggerAdapter}
import play.api.libs.json.{Format, Json}

object ProductBehavior {
  /**
   * Given a sharding [[EntityContext]] this function produces an Akka [[Behavior]] for the aggregate.
   */
  def create(entityContext: EntityContext[ProductCommand]): Behavior[ProductCommand] = {
    val persistenceId: PersistenceId = PersistenceId(entityContext.entityTypeKey.name, entityContext.entityId)

    create(persistenceId)
      .withTagger(
        // Using Akka Persistence Typed in Lagom requires tagging your events
        // in Lagom-compatible way so Lagom ReadSideProcessors and TopicProducers
        // can locate and follow the event streams.
        AkkaTaggerAdapter.fromLagom(entityContext, ProductEvent.Tag)
      )
  }

  private[impl] def create(persistenceId: PersistenceId) = EventSourcedBehavior
    .withEnforcedReplies[ProductCommand, ProductEvent, ProductState](
      persistenceId = persistenceId,
      emptyState = ProductState.initial,
      commandHandler = (cart, cmd) => cart.applyCommand(cmd),
      eventHandler = (cart, evt) => cart.applyEvent(evt)
    )
}

/**
 * This interface defines all the events that the AccountServiceAggregate supports.
 */
sealed trait ProductEvent extends AggregateEvent[ProductEvent] {
  def aggregateTag: AggregateEventTag[ProductEvent] = ProductEvent.Tag
}

object ProductEvent {
  val Tag: AggregateEventTag[ProductEvent] = AggregateEventTag[ProductEvent]
}

/**
 * This is a marker trait for commands.
 * We will serialize them using Akka's Jackson support that is able to deal with the replyTo field.
 * (see application.conf)
 */
trait ProductCommandSerializable

/**
 * This interface defines all the commands that the ProductAggregate supports.
 */
sealed trait ProductCommand
  extends ProductCommandSerializable


case class ProductState(products: Set[Product]) {
  def applyCommand(command: ProductCommand): ReplyEffect[ProductEvent, ProductState] = ???

  def applyEvent(event: ProductEvent): ProductState = this.copy()
}

object ProductState {
  val typeKey: EntityTypeKey[ProductCommand] = EntityTypeKey[ProductCommand]("ProductAggregate")

  def initial: ProductState = ProductState(Set.empty)

  implicit val format: Format[ProductState] = Json.format
}
