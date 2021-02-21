package cloud.quaer.product.api

import akka.Done
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
        restCall(Method.GET, s"/admin/api/v1/products${ProductQueryFilter.PATH}", products _),
        restCall(Method.GET, s"/admin/api/v1/products/count${CountQueryFilter.PATH}", count _),
        restCall(Method.GET, "/admin/api/v1/products/:productId", product _),
        restCall(Method.POST, "/admin/api/v1/products", addProduct _),
        restCall(Method.PUT, "/admin/api/v1/products/:productId", updateProduct _),
        restCall(Method.DELETE, "/admin/api/v1/products/:productId", deleteProduct _)
      )
      .withAutoAcl(true)
    // @formatter:on
  }

  /**
   * Retrieves all products available using productFilter
   *
   * @param productQueryFilter path filters available for product
   * @return ProductResponse
   */
  def products(productQueryFilter: ProductQueryFilter): ServiceCall[NotUsed, ProductResponse]

  /**
   * Return the number of products using path filter
   *
   * @param countQueryFilter query filter available to count products
   * @return number of products matching the filter
   */
  def count(countQueryFilter: CountQueryFilter): ServiceCall[NotUsed, ProductCount]

  /**
   * Retrieves details about a single product
   *
   * @param productId unique ID of the product
   * @return Product
   */
  def product(productId: UUID): ServiceCall[NotUsed, Product]

  /**
   * Add a new Product to the Database
   *
   * @return Product type
   */
  def addProduct(): ServiceCall[ProductRequest, Product]

  /**
   * Update details about a specific product
   *
   * @param productId Product unique ID
   * @return Product
   */
  def updateProduct(productId: UUID): ServiceCall[ProductRequest, Product]

  /**
   * Delete a product from the database using the unique ID
   *
   * @param productId Product unique ID
   */
  def deleteProduct(productId: UUID): ServiceCall[NotUsed, Done]
}
