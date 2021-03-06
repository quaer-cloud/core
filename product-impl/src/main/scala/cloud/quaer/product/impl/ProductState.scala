package cloud.quaer.product.impl

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.cluster.sharding.typed.scaladsl.EntityContext
import akka.cluster.sharding.typed.scaladsl.EntityTypeKey
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.Effect
import akka.persistence.typed.scaladsl.EventSourcedBehavior
import akka.persistence.typed.scaladsl.ReplyEffect
import akka.persistence.typed.scaladsl.RetentionCriteria
import cloud.quaer.product.api.models.Product
import com.lightbend.lagom.scaladsl.persistence._
import play.api.libs.json.Format
import play.api.libs.json.Json

import java.time.Instant

case class ProductState(product: Option[Product], timeStamp: String) {

  def applyEvent(evt: Event): ProductState = {
    evt match {
      case ProductAdded(product) => onProductAdded(product)
    }
  }

  private def onProductAdded(product: Product): ProductState = copy(Some(product), Instant.now().toString)

  def applyCommand(cmd: Command): ReplyEffect[Event, ProductState] = {
    cmd match {
      case AddProduct(product, replyTo) => onAddProduct(product, replyTo)
    }
  }

  private def onAddProduct(product: Product, replyTo: ActorRef[Confirmation]): ReplyEffect[Event, ProductState] = {
    val expectedStatus = product.status.getOrElse(false)
    val publishedTime  = Instant.now()

    Effect
      .persist(ProductAdded(product.copy(status = Some(expectedStatus), publishedAt = publishedTime)))
      .thenReply(replyTo)(updatedProduct => Accepted(toProduct(updatedProduct)))
  }

  private def toProduct(state: ProductState): Product = state.product.get
}

object ProductState {
  implicit val format: Format[ProductState] = Json.format

  val empty: ProductState = ProductState(product = Option.empty, timeStamp = Instant.now().toString)

  val typeKey: EntityTypeKey[Command] = EntityTypeKey[Command]("Product")

  def apply(entityContext: EntityContext[Command]): Behavior[Command] =
    apply(PersistenceId(entityContext.entityTypeKey.name, entityContext.entityId))
      .withTagger(AkkaTaggerAdapter.fromLagom(entityContext, Event.Tag))
      .withRetention(RetentionCriteria.snapshotEvery(numberOfEvents = 100, keepNSnapshots = 2))

  def apply(persistenceId: PersistenceId): EventSourcedBehavior[Command, Event, ProductState] = {
    EventSourcedBehavior
      .withEnforcedReplies[Command, Event, ProductState](
        persistenceId = persistenceId,
        emptyState = ProductState.empty,
        commandHandler = (state, cmd) => state.applyCommand(cmd),
        eventHandler = (state, evt) => state.applyEvent(evt)
      )
  }
}

// Commands
trait CommandSerializable

sealed trait Command extends CommandSerializable

final case class AddProduct(product: Product, replyTo: ActorRef[Confirmation]) extends Command

// Events
sealed trait Event extends AggregateEvent[Event] {
  override def aggregateTag: AggregateEventTagger[Event] = Event.Tag
}

object Event {
  val Tag: AggregateEventShards[Event] = AggregateEventTag.sharded[Event](numShards = 10)
}

final case class ProductAdded(product: Product) extends Event
object ProductAdded {
  implicit val format: Format[ProductAdded] = Json.format
}

// Answer
sealed trait Confirmation

final case class Accepted(product: Product) extends Confirmation
object Accepted {
  implicit val format: Format[Accepted] = Json.format
}

final case class Rejected(reason: String) extends Confirmation
object Rejected {
  implicit val format: Format[Rejected] = Json.format
}
