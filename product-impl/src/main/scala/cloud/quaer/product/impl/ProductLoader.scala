package cloud.quaer.product.impl

import akka.cluster.sharding.typed.scaladsl.Entity
import cloud.quaer.product.api.ProductService
import com.lightbend.lagom.scaladsl.api.Descriptor
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.server.LagomApplication
import com.lightbend.lagom.scaladsl.server.LagomApplicationContext
import com.lightbend.lagom.scaladsl.server.LagomApplicationLoader
import com.lightbend.lagom.scaladsl.server.LagomServer
import com.softwaremill.macwire.wire
import play.api.libs.ws.ahc.AhcWSComponents

class ProductLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new ProductApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new ProductApplication(context) with LagomDevModeComponents

  override def describeService: Option[Descriptor] = Some(readDescriptor[ProductService])
}

abstract class ProductApplication(context: LagomApplicationContext)
    extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[ProductService](wire[ProductServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry: JsonSerializerRegistry = ProductSerializerRegistry

  // Initialize the sharding of the Aggregate. The following starts the aggregate Behavior under
  // a given sharding entity typeKey.
  clusterSharding.init(
    Entity(ProductState.typeKey)(entityContext => ProductBehavior.create(entityContext))
  )
}
