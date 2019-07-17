package base

import com.typesafe.config.Config

import scala.concurrent.duration.{Duration, FiniteDuration}

/**
  * Thiago Carreira - tcanascimento@gmail.com
  * Apache 2
  */

case class SimulationSpecs(config: Config)  {

  lazy val sceneName: String = config.getConfig("simulation").getString("sceneName")
  lazy val requestName: String = config.getConfig("simulation").getString("requestName")
  lazy val initialUsers: Int = config.getConfig("simulation").getInt("initialUsers")
  lazy val rampUpRate1: Int = config.getConfig("simulation").getInt("rampUpRate1")
  lazy val rampUpRate2: Int = config.getConfig("simulation").getInt("rampUpRate2")
  lazy val duration: FiniteDuration = Duration.fromNanos(config.getConfig("simulation").getDuration("duration").toNanos)
  lazy val duration2: FiniteDuration = Duration.fromNanos(config.getConfig("simulation").getDuration("duration2").toNanos)
  lazy val failedPercentRequests: Int = config.getConfig("simulation").getInt("failedRequestPercent")

  override def toString: String = "\ninitial users: " + initialUsers + "\nramp up rate 1: " + rampUpRate1 + "\nramp up rate 2: " + rampUpRate2 + "\nduration: " + duration + "\nduration2: " + duration2

}
