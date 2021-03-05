package cloud.quaer.product.impl

import akka.actor.typed.ActorRef
import cloud.quaer.product.api.CountQueryFilter
import cloud.quaer.product.api.ProductQueryFilter
import cloud.quaer.product.api.Product
import play.api.libs.json.Format
import play.api.libs.json.Json

/**
 * This is a marker trait for commands.
 * We will serialize them using Akka's Jackson support that is able to deal with the replyTo field.
 * (see application.conf)
 */
sealed trait ProductCommandSerializable

/**
 * This interface defines all the commands that the ProductAggregate supports.
 */
trait ProductCommand extends ProductCommandSerializable

final case class GetProducts(query: ProductQueryFilter, replyTo: ActorRef[CommandResult]) extends ProductCommand
final case class CountProducts(query: CountQueryFilter, replyTo: ActorRef[CommandResult]) extends ProductCommand
final case class GetProduct(replyTo: ActorRef[CommandResult])                             extends ProductCommand
final case class CreateProduct(product: Product, replyTo: ActorRef[CommandResult])        extends ProductCommand
final case class UpdateProduct(product: Product, replyTo: ActorRef[CommandResult])        extends ProductCommand
final case class DeleteProduct(replyTo: ActorRef[CommandResult])                          extends ProductCommand

trait CommandResult

final case class AcceptedResult(post: Option[Product]) extends CommandResult

final case class RejectedResult(reason: String) extends CommandResult

object RejectedResult {
  implicit val format: Format[RejectedResult] = Json.format
}

object AcceptedResult {
  implicit val format: Format[AcceptedResult] = Json.format
}
