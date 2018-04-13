package config

import com.typesafe.config._
import com.typesafe.scalalogging.LazyLogging
import domain.oauth._

import scala.concurrent.duration._
import scala.util._

/**
  * Helper object that loads configuration
  * @throws scala.RuntimeException if required configuration
  *                          not specified(FACEBOOK_CLIENT_ID, FACEBOOK_REDIRECT_URI, FACEBOOK_APP_SECRET)
  */
object FacebookConfig extends ConfigurationDetector with LazyLogging {

  val config = ConfigFactory.load

  val version = FacebookVersion("2.10")

  val accessToken = FacebookAccessToken(
    tokenValue = tokenValueOf("FACEBOOK_ACCESS_TOKEN", "facebook.accessToken"),
    tokenType  = UserAccessToken("bearer", 60.days)
  )

  logger.info(s"Client id - ${version.value}")

  private def tokenValueOf(envStr: String, typesafeStr: String) = TokenValue(envVarOrConfig(envStr, typesafeStr))

}

/**
  * Trait that implements configuration detection
  */
trait ConfigurationDetector {

  /**
    * Config file
    * @return type safe config
    */
  def config: Config

  /**
    * @param envVar OS environment variable key
    * @param configName Type safe configuration key
    * @return configuration value
    */
  def envVarOrConfig(envVar: String, configName: String): String = {
    Try(environmentVariable(envVar) getOrElse configuration(configName)) match {
      case Success(s)        => s
      case Failure(e) =>
        val msg = s"[facebook4s] configuration missing: " +
          s"Environment variable $envVar or configuration $configName not found."
        throw new RuntimeException(msg)
    }
  }

  def envVarOrConfigOptional(envVar: String, configName: String): Option[String] = {
    Try(environmentVariable(envVar) getOrElse configuration(configName)).toOption
  }


  /**
    * @param name OS environment variable key
    * @return Optional environment variable value
    */
  def environmentVariable(name: String): Option[String] = Properties.envOrNone(name)

  /**
    * @param path Path at type safe config
    * @return the { @code Enum} value at the requested path
    *                     of the requested enum class
    * @throws com.typesafe.config.ConfigException.Missing
    * if value is absent or null
    * @throws com.typesafe.config.ConfigException.WrongType
    * if value is not convertible to an Enum
    */
  def configuration(path: String): String = config.getString(path)

}