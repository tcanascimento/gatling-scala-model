import sbt._

object Dependencies {

  lazy val gatling: Seq[ModuleID] = Seq(
    "io.gatling.highcharts" % "gatling-charts-highcharts",
    "io.gatling" % "gatling-test-framework",
  ).map(_ % "3.1.1")

  lazy val ALL: Seq[sbt.ModuleID] = gatling :+ "org.zeroturnaround" % "zt-zip" % "1.13" :+ "com.google.cloud" % "google-cloud-storage" % "1.82.0"
}