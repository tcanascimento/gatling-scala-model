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

class MultipleRequestSpecs extends Simulation with SpecTrait {

  lazy val configFile: String = Properties.envOrElse("configFile", "contabil-multiple-core.conf")
  lazy val specs: base.RestSpecs = Specs.restSpecs.apply(ConfigFactory.load(configFile))
  lazy val simulSpecs: base.SimulationSpecs = Specs.simulationsSpecs.apply(ConfigFactory.load(configFile))

}

/**
  * class MySimulation extends Simulation {
  *
  * val userIdsData = csv(userIdsCSV).queue
  *
  *
  *
  * ...
  *
  *
  *
  * val scenario1 = scenario("Scenario 1")
  *
  * .feed(userIdsData)
  *
  * .get(...)
  *
  *
  *
  * val scenario2 = scenario("Scenario 2")
  *
  * .feed(userIdsData)
  *
  * .get(...)
  *
  * .post(...)
  *
  *
  *
  * setUp(scenario1.inject(rampUsers(30) over (ramp seconds))
  *
  * .protocols(HttpConfig.value(baseURL)),
  *
  * scenario2.inject(rampUsers(70) over (ramp seconds))
  *
  * .protocols(HttpConfig.value(baseURL))
  *
  * )
  *
  * }
  */
