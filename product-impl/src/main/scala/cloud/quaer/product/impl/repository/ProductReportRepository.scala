package cloud.quaer.product.impl.repository

import akka.Done
import cloud.quaer.product.api.models.Product
import slick.dbio.DBIO
import slick.jdbc.PostgresProfile.api._

import java.time.Instant
import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ProductReportRepository(database: Database) {
  val products = TableQuery[ProductTable]

  def createProduct(product: Product): DBIO[Done] =
    findByIdQuery(product.id)
      .flatMap {
        case None => products += product
        case _    => DBIO.successful(Done)
      }
      .map(_ => Done)
      .transactionally

  def createTable = products.schema.createIfNotExists

  def findById(id: UUID): Future[Option[Product]] = database.run(findByIdQuery(id))

  private def findByIdQuery(productId: UUID): DBIO[Option[Product]] =
    products.filter(_.productId === productId).result.headOption

  class ProductTable(tag: Tag) extends Table[Product](tag, "products") {
    def * = (productId, title, vendor, productType, bodyHtml, slug, updatedAt, publishedAt, status).mapTo[Product]

    def productId = column[UUID]("product_id", O.PrimaryKey)

    def title = column[String]("title", O.Unique)

    def vendor = column[String]("vendor")

    def productType = column[String]("product_type")

    def bodyHtml = column[String]("body")

    def slug = column[String]("slug", O.Unique)

    def updatedAt = column[Option[Instant]]("updated_at")

    def publishedAt = column[Instant]("published_at")

    def status = column[Option[Boolean]]("status")
  }
}
