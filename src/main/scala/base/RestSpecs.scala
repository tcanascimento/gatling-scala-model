package base

import com.typesafe.config.Config

import scala.collection.JavaConverters._

/**
  * Thiago Carreira - tcanascimento@gmail.com
  * Apache 2
  */

case class RestSpecs(config: Config) {

  lazy val baseUrl: String = config.getConfig("specs").getString("baseUrl")
//  lazy val endpoint: String = config.getConfig("specs").getString("endpoint")
  lazy val endpoint: String = setEndpoint(config)
  lazy val headers: Map[String, String] = config.getConfig("specs").getObject("headers").unwrapped().asScala.toMap.mapValues(_.toString)
  lazy val queryParams: Map[String, String] = config.getConfig("specs").getObject("queryParams").unwrapped().asScala.toMap.mapValues(_.toString)
  lazy val pathParams: Map[String, String] = config.getConfig("specs").getObject("pathParams").unwrapped().asScala.toMap.mapValues(_.toString)
  lazy val fileBody: String = config.getConfig("specs").getString("fileBody")
  lazy val statusCode: Int = config.getConfig("specs").getInt("statusCode")


  def setPathParams(endpoint: String, pars: Map[String,String]): String = {
    var temp: String = endpoint
    pars.keySet.foreach(f = k => temp = temp.replaceFirst("\\{".concat(k).concat("\\}"), pars(k)))
    temp
  }

  private def setEndpoint(endpoint: Config): String = {
    var tempEndpoint: String = endpoint.getConfig("specs").getString("endpoint")
    var pathParams: Map[String, String] = config.getConfig("specs").getObject("pathParams").unwrapped().asScala.toMap.mapValues(_.toString)

    if(pathParams.size < 1 || pathParams == null) tempEndpoint else setPathParams(tempEndpoint, pathParams)
  }

  override def toString: String = "\nbaseURL: " + baseUrl + "\nendpoint: " + endpoint + "\nheaders: " + headers + "\nfile body location: " + fileBody + "\nstadus code: " + statusCode

}

