package cloud.quaer.product.api

import akka.Done
import akka.NotUsed
import cloud.quaer.product.api.models.CountQueryFilter
import cloud.quaer.product.api.models.ProductCountResponse
import cloud.quaer.product.api.models.ProductPagedView
import cloud.quaer.product.api.models.ProductQueryFilter
import cloud.quaer.product.api.models.ProductRequest
import cloud.quaer.product.api.models.ProductResponse
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.Descriptor
import com.lightbend.lagom.scaladsl.api.Service
import com.lightbend.lagom.scaladsl.api.ServiceCall

import java.util.UUID

/**
 * The Product resource lets you update and create products in a merchant's store.
 * You can use product variants with the Product resource to create or update different versions of the same product.
 *
 * @see ProductVariantService
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
        restCall(Method.GET, "/admin/api/v1/products/:productId", getProduct _),
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
  def products(productQueryFilter: ProductQueryFilter): ServiceCall[NotUsed, ProductPagedView]

  /**
   * Return the number of products using path filter
   *
   * @param countQueryFilter query filter available to count products
   * @return number of products matching the filter
   */
  def count(countQueryFilter: CountQueryFilter): ServiceCall[NotUsed, ProductCountResponse]

  /**
   * Retrieves details about a single product
   *
   * @param productId unique ID of the product
   * @return Product
   */
  def getProduct(productId: UUID): ServiceCall[NotUsed, ProductResponse]

  /**
   * Add a new Product to the Database
   *
   * @return Product type
   */
  def addProduct(): ServiceCall[ProductRequest, ProductResponse]

  /**
   * Update details about a specific product
   *
   * @param productId Product unique ID
   * @return Product
   */
  def updateProduct(productId: UUID): ServiceCall[ProductRequest, ProductResponse]

  /**
   * Delete a product from the database using the unique ID
   *
   * @param productId Product unique ID
   */
  def deleteProduct(productId: UUID): ServiceCall[NotUsed, Done]
}
