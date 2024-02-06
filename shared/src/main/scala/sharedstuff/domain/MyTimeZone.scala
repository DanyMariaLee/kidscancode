package sharedstuff.domain

import java.time.ZoneId
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

import scala.util.{Failure, Success, Try}

sealed case class MyTimeZone(zoneId: ZoneId) {
  override def toString: String = zoneId.getId
}

object MyTimeZone {
  def apply(zoneId: String): MyTimeZone = MyTimeZone(findZoneIdOrDefault(zoneId))

  // TODO devil is in the details https://facingissuesonit.com/2018/07/06/java-time-zone-zonerulesexception-unknown-time-zone-id-ist/
  private val fixTimeZoneMapping = Map(
    "-10:00" -> "HST",
    "-05:00" -> "EST",
    "-07:00" -> "MST",
    "Australia/Darwin" -> "ACT",
    "Australia/Sydney" -> "AET",
    "America/Argentina/Buenos_Aires" -> "AGT",
    "Africa/Cairo" -> "ART",
    "America/Anchorage" -> "AST",
    "America/Sao_Paulo" -> "BET",
    "Asia/Dhaka" -> "BST",
    "Africa/Harare" -> "CAT",
    "America/St_Johns" -> "CNT",
    "America/Chicago" -> "CST",
    "Asia/Shanghai" -> "CTT",
    "Africa/Addis_Ababa" -> "EAT",
    "Europe/Paris" -> "ECT",
    "America/Indiana/Indianapolis" -> "IET",
    "Asia/Kolkata" -> "IST",
    "Asia/Tokyo" -> "JST",
    "Pacific/Apia" -> "MIT",
    "Asia/Yerevan" -> "NET",
    "Pacific/Auckland" -> "NST",
    "Asia/Karachi" -> "PLT",
    "America/Phoenix" -> "PNT",
    "America/Puerto_Rico" -> "PRT",
    "America/Los_Angeles" -> "PST",
    "Pacific/Guadalcanal" -> "SST",
    "Asia/Ho_Chi_Minh" -> "VST"
  )

  def findZoneIdOrDefault(zoneId: String): ZoneId = Try(ZoneId.getAvailableZoneIds.toArray
    .find(z => z.toString.equalsIgnoreCase(zoneId)).map(_ => ZoneId.of(zoneId))) match {
    case Success(Some(zid)) => zid
    case _ => fixTimeZoneMapping.keySet
      .find(tzs => tzs.equalsIgnoreCase(zoneId) || tzs.contains(zoneId))
      .map(k => fixTimeZoneMapping(k))
      .flatMap(z => Try(ZoneId.of(z)).toOption)
      .getOrElse(ZoneId.of("UTC"))
  }
}