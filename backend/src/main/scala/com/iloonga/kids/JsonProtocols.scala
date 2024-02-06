package com.iloonga.kids

import java.nio.charset.StandardCharsets
import java.util.Base64

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import sharedstuff.domain.MyTimeZone
import sharedstuff.serverresponses.ServerResponse
import spray.json._

trait JsonProtocols extends SprayJsonSupport with DefaultJsonProtocol {

  lazy val jsonFormatterLogger: Logger = Logger(LoggerFactory.getLogger(classOf[JsonProtocols]))

  implicit val intFormatter = IntJsonFormat

  implicit object MyTimeZoneJsonFormat extends RootJsonFormat[MyTimeZone] {
    def write(a: MyTimeZone) = a.toString.toJson

    def read(value: JsValue) = MyTimeZone(StringJsonFormat.read(value))
  }

  implicit object srFormat extends RootJsonFormat[ServerResponse] {
    def write(a: ServerResponse) = a.toString.toJson

    def read(value: JsValue): ServerResponse = {
      val fs = value.asJsObject.fields
      ServerResponse(
        fs("statusCode").convertTo[Int],
        fs("msg").convertTo[String]
      )
    }
  }

  // TODO FIX IT !! writes ok in DB but returns undecodable crap
  implicit object ArrByteJsonFormat extends RootJsonFormat[Array[Byte]] {
    def write(a: Array[Byte]) =
      new String(Base64.getEncoder.encode(a), StandardCharsets.UTF_8).toJson

    def read(value: JsValue) =
      Base64.getDecoder.decode(StringJsonFormat.read(value))
  }

  implicit val serverResponseFormat = jsonFormat2(ServerResponse)

}
