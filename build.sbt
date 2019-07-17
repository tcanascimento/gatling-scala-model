import Dependencies._

enablePlugins(GatlingPlugin)

resolvers += Resolver.jcenterRepo
//resolvers += Resolver.url("iheart-sbt-plugin-releases", url("https://dl.bintray.com/iheartradio/sbt-plugins"))

lazy val root = (project in file("."))
  .settings(assemblySettings)
  .settings(
    inThisBuild(List(
      organization := "tcanascimento",
      scalaVersion := "2.12.8",
      version := "0.1.0-SNAPSHOT"
    )),
    name := "load-testing",
    version := "0.1",
    libraryDependencies ++= ALL
  )

lazy val assemblySettings = Seq(
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
  },
  assemblyMergeStrategy in (IntegrationTest, assembly) := {
    case x if x.contains("io.netty.versions.properties")  => MergeStrategy.rename
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  },
  test in assembly := {},
  assemblyJarName in assembly := "load-test.jar",
  mainClass in assembly := Some("io.gatling.app.Gatling"),
)
