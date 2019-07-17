package traits

import base.{RestSpecs, SimulationSpecs}
import com.typesafe.config.Config

/**
  * Thiago Carreira - tcanascimento@gmail.com
  * Apache 2
  */

trait SpecTrait {

  object Specs {

    lazy val simulationsSpecs: Config => SimulationSpecs = (config: Config) => SimulationSpecs.apply(config)
    lazy val restSpecs: Config => RestSpecs = (config: Config) => RestSpecs.apply(config)
  }

  object Helpers {

  }

}
