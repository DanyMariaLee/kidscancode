import sbt.SettingKey

/**
 * Configures the logging behavior for this project.
 */
object LogConfig {

  val logDirKey = SettingKey[String]("log-dir", "Defines the location of our log file")
  val logDir = "/logs"

}