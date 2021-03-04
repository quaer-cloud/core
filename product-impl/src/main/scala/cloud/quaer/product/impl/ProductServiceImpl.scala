package cloud.quaer.product.impl

import akka.{Done, NotUsed}
import cloud.quaer.product.api
import cloud.quaer.product.api._
import com.lightbend.lagom.scaladsl.api.ServiceCall

import java.util.UUID

class ProductServiceImpl extends ProductService {
  /**
   * Retrieves all products available using productFilter
   *
   * @param productQueryFilter path filters available for product
   * @return ProductResponse
   */
  override def products(productQueryFilter: ProductQueryFilter): ServiceCall[NotUsed, ProductResponse] = ???

  /**
   * Return the number of products using path filter
   *
   * @param countQueryFilter query filter available to count products
   * @return number of products matching the filter
   */
  override def count(countQueryFilter: CountQueryFilter): ServiceCall[NotUsed, ProductCount] = ???

  /**
   * Retrieves details about a single product
   *
   * @param productId unique ID of the product
   * @return Product
   */
  override def product(productId: UUID): ServiceCall[NotUsed, api.Product] = ???

  /**
   * Add a new Product to the Database
   *
   * @return Product type
   */
  override def addProduct(): ServiceCall[ProductRequest, api.Product] = ???

  /**
   * Update details about a specific product
   *
   * @param productId Product unique ID
   * @return Product
   */
  override def updateProduct(productId: UUID): ServiceCall[ProductRequest, api.Product] = ???

  /**
   * Delete a product from the database using the unique ID
   *
   * @param productId Product unique ID
   */
  override def deleteProduct(productId: UUID): ServiceCall[NotUsed, Done] = ???
}
