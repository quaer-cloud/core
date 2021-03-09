package cloud.quaer.product.impl

import akka.Done
import akka.NotUsed
import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import akka.cluster.sharding.typed.scaladsl.EntityRef
import akka.dispatch.Futures
import akka.util.Timeout
import cloud.quaer.product.api.ProductService
import cloud.quaer.product.api.models._
import cloud.quaer.product.impl.repository.ProductReportRepository
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.NotFound

import java.util.UUID
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class ProductServiceImpl(
    clusterSharding: ClusterSharding,
    reportRepository: ProductReportRepository
)(implicit ec: ExecutionContext)
    extends ProductService {

  implicit val timeout: Timeout = Timeout(5.seconds)

  /**
   * Retrieves all products available using productFilter
   *
   * @param productQueryFilter path filters available for product
   * @return ProductResponse
   */
  override def products(productQueryFilter: ProductQueryFilter): ServiceCall[NotUsed, ProductPagedView] = ServiceCall {
    // TODO Review logic here
    _ => Futures.successful(ProductPagedView(Set.empty))
  }

  /**
   * Return the number of products using path filter
   *
   * @param countQueryFilter query filter available to count products
   * @return number of products matching the filter
   */
  override def count(countQueryFilter: CountQueryFilter): ServiceCall[NotUsed, ProductCountResponse] = ServiceCall {
    // TODO Review logic here
    _ => Futures.successful(ProductCountResponse(-1))
  }

  /**
   * Retrieves details about a single product
   *
   * @param productId unique ID of the product
   * @return Product
   */
  override def getProduct(productId: UUID): ServiceCall[NotUsed, Product] = ServiceCall { _ =>
    reportRepository
      .findById(productId)
      .map {
        case Some(product) => product
        case _             => throw NotFound(s"Could not find the product ID ${productId}")
      }
  }

  /**
   * Add a new Product to the Database
   *
   * @return Product type
   */
  override def addProduct(): ServiceCall[ProductRequest, ProductResponse] = ???

  /**
   * Update details about a specific product
   *
   * @param productId Product unique ID
   * @return Product
   */
  override def updateProduct(productId: UUID): ServiceCall[ProductRequest, ProductResponse] = ???

  /**
   * Delete a product from the database using the unique ID
   *
   * @param productId Product unique ID
   */
  override def deleteProduct(productId: UUID): ServiceCall[NotUsed, Done] = ???

  /**
   * Looks up the shopping cart entity for the given ID.
   */
  private def entityRef(id: UUID): EntityRef[Command] =
    clusterSharding.entityRefFor(ProductState.typeKey, id.toString)
}
