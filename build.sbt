// Change here with your organization details
organization in ThisBuild := "cloud.quaer"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.13.0"
val macwire   = "com.softwaremill.macwire" %% "macros"    % "2.3.3" % "provided"
val scalaTest = "org.scalatest"            %% "scalatest" % "3.1.1" % Test

lazy val `core` = (project in file("."))
  .aggregate(`product-api`, `product-impl`)

lazy val `product-api` = (project in file("product-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `product-impl` = (project in file("product-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings)
  .dependsOn(`product-api`)
