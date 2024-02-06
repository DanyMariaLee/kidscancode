package sharedstuff.patterns

import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.{Instant, ZoneId}
import java.util.Locale

import sharedstuff.domain.MyTimeZone

import scala.util.Try

object UnixTimeFormatter {

  val lhtFormat = "dd MMM HH:mm"
  val lhTFormat = "dd MMM HH:mm:SS yyyy"
  val commentDateFormat = "dd MMM yyyy HH:mm"
  val dateFormatWithDayOfWeek = "EEE dd MMM yyyy HH:mm:SS"

  def dateFormatted(unixTime: Long, format: String, zoneId: Option[String]): String = {
    val formatter = DateTimeFormatter.ofPattern(format, Locale.ENGLISH)
    val detectZoneId: ZoneId = getZoneId(zoneId)

    Instant.ofEpochSecond(unixTime)
      .atZone(detectZoneId)
      .format(formatter)
  }

  def getZoneId(maybeZoneId: Option[String]): ZoneId = maybeZoneId.find(z =>
    ZoneId.getAvailableZoneIds.toArray
      .exists(_.toString.equalsIgnoreCase(z)))
    .flatMap(zz => Try(ZoneId.of(zz)).toOption)
    //.getOrElse(ZoneId.systemDefault())
    // TODO WTF with time zones in java
    .getOrElse(MyTimeZone.findZoneIdOrDefault(""))

  def yearsPassed(since: Long): Try[Double] =
    Try(Instant.ofEpochSecond(since).until(Instant.now(), ChronoUnit.DAYS) / 365.0)
}
