package simulations

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import traits.SpecTrait

import scala.util.Properties

/**
  * Thiago Carreira - tcanascimento@gmail.com
  * Apache 2
  */

class TestRampUpPerSecDuringTimePost extends Simulation with SpecTrait {

  lazy val configFile: String = Properties.envOrElse("configFile", "config-file.conf")
  lazy val specs: base.RestSpecs = Specs.restSpecs.apply(ConfigFactory.load(configFile))
  lazy val simulSpecs: base.SimulationSpecs = Specs.simulationsSpecs.apply(ConfigFactory.load(configFile))

  lazy val scene: ScenarioBuilder = scenario(simulSpecs.sceneName)
    .exec(http(simulSpecs.requestName)
      .post(specs.endpoint)
      .headers(specs.headers)
      .queryParamMap(specs.queryParams)
      .body(ElFileBody(specs.fileBody)).asJson
      .check(status.is(200))
    )
    .exec { session2 =>
      println("session:" + session2.toString)
      session2
    }

  setUp(
    scene.inject(rampUsers(simulSpecs.rampUpRate2).during(simulSpecs.duration))protocols http.baseUrl(specs.baseUrl)
  ).assertions(forAll.failedRequests.percent.lte(simulSpecs.failedPercentRequests))
}
