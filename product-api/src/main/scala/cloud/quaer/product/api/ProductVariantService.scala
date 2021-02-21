package cloud.quaer.product.api

import akka.Done
import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.Descriptor
import com.lightbend.lagom.scaladsl.api.Service
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.Method

import java.util.UUID

/**
 * A variant can be added to a Product resource to represent one version of a product with several options.
 * The Product resource will have a variant for every possible combination of its options. Each product can have
 * a maximum of three options and a maximum of 100 variants.
 *
 * @author tiare.balbi
 * @version 1.0.0
 */
trait ProductVariantService extends Service {

  final override def descriptor: Descriptor = {
    import Service._
    // @formatter:off
    named("productsVariant")
      .withCalls(
        restCall(Method.GET, "/admin/api/v1/products/:productId/variants", variants _),
        restCall(Method.GET, "/admin/api/v1/products/:productId/variants/count", countVariants _),
        restCall(Method.POST, "/admin/api/v1/products/:productId/variants", addVariant _),
        restCall(Method.GET, "/admin/api/v1/variants/:variantId", variant _),
        restCall(Method.PUT, "/admin/api/v1/variants/:variantId", updateVariant _),
        restCall(Method.DELETE, "/admin/api/v1/variants/:variantId", deleteVariant _),
      )
      .withAutoAcl(true)
    // @formatter:on
  }

  /**
   * Get all variants for a given product
   *
   * @param productId unique ID of the product
   * @return all variants available for given product
   */
  def variants(productId: UUID): ServiceCall[NotUsed, ProductVariantResponse]

  /**
   * count the number of variants available for product
   *
   * @param productId unique ID of the product
   * @return number of variants
   */
  def countVariants(productId: UUID): ServiceCall[NotUsed, Int]

  /**
   * Get details of a single variant
   *
   * @param variantId unique ID of the variant
   * @return
   */
  def variant(variantId: UUID): ServiceCall[NotUsed, ProductVariant]

  /**
   * Add a new variant to product
   *
   * @param productId unique ID of the product
   * @return details of the new variant added
   */
  def addVariant(productId: UUID): ServiceCall[ProductVariantRequest, ProductVariant]

  /**
   * Update details of a single variant
   *
   * @param variantId variant ID
   * @return updated details
   */
  def updateVariant(variantId: UUID): ServiceCall[ProductVariantRequest, ProductVariant]

  /**
   * Remove a variant from the product
   *
   * @param variantId unique ID of the variant
   * @return confirmation
   */
  def deleteVariant(variantId: UUID): ServiceCall[NotUsed, Done]
}
