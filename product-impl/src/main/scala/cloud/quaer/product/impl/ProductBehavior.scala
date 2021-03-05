package cloud.quaer.product.impl

import akka.actor.typed.Behavior
import akka.cluster.sharding.typed.scaladsl._
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.EventSourcedBehavior
import akka.persistence.typed.scaladsl.RetentionCriteria
import com.lightbend.lagom.scaladsl.persistence.AkkaTaggerAdapter

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
      // snapshot every 2 events and keep at most 2 snapshots on db
      .withRetention(
        RetentionCriteria
          .snapshotEvery(numberOfEvents = 2, keepNSnapshots = 2)
      )
  }

  private[impl] def create(persistenceId: PersistenceId) = EventSourcedBehavior
    .withEnforcedReplies[ProductCommand, ProductEvent, ProductState](
      persistenceId = persistenceId,
      emptyState = ProductState.initial,
      commandHandler = (cart, cmd) => cart.handleCommand(cmd),
      eventHandler = (cart, evt) => cart.handleEvent(evt)
    )
}
