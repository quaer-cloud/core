package cloud.quaer.product.impl

import akka.cluster.sharding.typed.scaladsl.Entity
import cloud.quaer.product.api.ProductService
import cloud.quaer.product.impl.repository.ProductReportProcessor
import cloud.quaer.product.impl.repository.ProductReportRepository
import com.lightbend.lagom.scaladsl.akka.discovery.AkkaDiscoveryComponents
import com.lightbend.lagom.scaladsl.api.Descriptor
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.slick.SlickPersistenceComponents
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire.wire
import play.api.db.HikariCPComponents
import play.api.libs.ws.ahc.AhcWSComponents

import scala.concurrent.ExecutionContext

class ProductLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new ProductApplication(context) with AkkaDiscoveryComponents

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new ProductApplication(context) with LagomDevModeComponents

  override def describeService: Option[Descriptor] = Some(readDescriptor[ProductService])
}

trait ProductComponents
    extends LagomServerComponents
    with SlickPersistenceComponents
    with HikariCPComponents
    with AhcWSComponents {

  implicit def executionContext: ExecutionContext

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[ProductService](wire[ProductServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry: JsonSerializerRegistry = ProductSerializerRegistry

  lazy val reportRepository: ProductReportRepository =
    wire[ProductReportRepository]
  readSide.register(wire[ProductReportProcessor])

  clusterSharding.init(
    Entity(ProductState.typeKey) { entityContext => ProductState(entityContext) }
  )
}

abstract class ProductApplication(context: LagomApplicationContext)
    extends LagomApplication(context)
    with ProductComponents
    with LagomKafkaComponents {}
