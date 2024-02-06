package sharedstuff.serverresponses

import io.circe._
import io.circe.generic.auto._
import io.circe.generic.semiauto.deriveDecoder
import io.circe.parser._
import io.circe.syntax._
import upickle.default._

case class ServerResponse(statusCode: Int, msg: String)

object ServerResponseImplicits {
  implicit val srRW: ReadWriter[ServerResponse] = macroRW[ServerResponse]
  implicit val jsonSRDecoder: Decoder[ServerResponse] = deriveDecoder[ServerResponse]
}