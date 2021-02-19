package cloud.quaer.product.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.Descriptor
import com.lightbend.lagom.scaladsl.api.Service
import com.lightbend.lagom.scaladsl.api.ServiceCall

import java.util.UUID

/**
 * Manage a store's products, which are the individual items and services for sale in the store.
 *
 * @author tiare.balbi
 * @version 1.0.0
 */
trait ProductService extends Service {

  final override def descriptor: Descriptor = {
    import Service._

    // @formatter:off
    named("products")
      .withCalls(
        restCall(Method.GET, s"/admin/api/v1/products${ProductFilter.PATH}", products _),
        restCall(Method.GET, s"/admin/api/v1/products/count${ProductFilter.PATH}", count _),
        restCall(Method.GET, "/admin/api/v1/products/:productId", product _)
      )
      .withAutoAcl(true)
    // @formatter:on
  }

  /**
   * Retrieves all products available using productFilter
   *
   * @param productFilter path filters available for product
   * @return ProductResponse
   */
  def products(productFilter: ProductFilter): ServiceCall[NotUsed, ProductResponse]

  /**
   * Return the number of products using path filter
   *
   * @param productFilter path filters available for product
   * @return count
   */
  def count(productFilter: ProductFilter): ServiceCall[NotUsed, Int]

  /**
   * Retrieves details about a single product
   *
   * @param productId unique ID of the product
   * @return Product
   */
  def product(productId: UUID): ServiceCall[NotUsed, Product]
}
