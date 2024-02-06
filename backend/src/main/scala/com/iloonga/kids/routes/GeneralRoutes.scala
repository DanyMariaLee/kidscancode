package com.iloonga.kids.routes

import java.nio.file.{Files, Paths}
import java.time.{Instant, ZoneId}

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{Uri, _}
import akka.http.scaladsl.model.headers.{CacheDirectives, `Cache-Control`}
import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives.{as => ahAs, _}
import com.iloonga.kids.JsonProtocols
import com.iloonga.kids.routes.GeneralRoutes._
import com.iloonga.kids.service.LearningService
import com.typesafe.scalalogging.LazyLogging
import sharedstuff.api.GeneralRoutAjaxApi
import sharedstuff.patterns.UnixTimeFormatter.{commentDateFormat, dateFormatted}
import sharedstuff.serverresponses.ServerResponse

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class GeneralRoutes(learningService: LearningService)(implicit actorSystem: ActorSystem, implicit val ec: ExecutionContext)
  extends GeneralRoutAjaxApi with JsonProtocols with LazyLogging {

  def heartbeat: Future[ServerResponse] = Future(ServerResponse(0, ""))
  def checkSolution(nameContains: String, id: Int): Future[ServerResponse] = Future(ServerResponse(0, ""))

  def generalRoutes: server.Route =
    pathPrefix("assets") {
      get {
        respondWithHeaders(Seq(
          new `Cache-Control`(
            Seq(
              CacheDirectives.public,
              CacheDirectives.`max-age`(31536000)))
        ))(getFromResourceDirectory("assets"))
      }
    } ~
      pathPrefix("images") {
        get {

          respondWithHeaders(Seq(
            new `Cache-Control`(
              Seq(
                CacheDirectives.public,
                CacheDirectives.`max-age`(31536000)))
          ))(getFromBrowseableDirectory("images"))
        }
      } ~
      ping ~
      checkSolution ~
      path(PngPathRex) { _ =>
        get {
          entity(ahAs[HttpRequest]) {
            case requestData if requestData.uri.path.toString.endsWith(".png") =>
              complete {
                val c = ContentType(MediaTypes.`image/png`)
                val byteArray = Files.readAllBytes(
                  Paths.get(WorkingDirectory + requestData.uri.path.toString))
                HttpResponse(StatusCodes.OK,
                  headers = Seq(
                    new `Cache-Control`(
                      Seq(
                        CacheDirectives.public,
                        CacheDirectives.`max-age`(31536000)))
                  ),
                  entity = HttpEntity(c, byteArray))
              }
          }
        }
      } ~
      path(JpegPathRex) { _ =>
        get {
          entity(ahAs[HttpRequest]) {
            case requestData if requestData.uri.path.toString.endsWith(".jpeg") =>
              complete {
                val c = ContentType(MediaTypes.`image/jpeg`)
                val byteArray = Files.readAllBytes(
                  Paths.get(WorkingDirectory + requestData.uri.path.toString))
                HttpResponse(StatusCodes.OK,
                  headers = Seq(
                    new `Cache-Control`(
                      Seq(
                        CacheDirectives.public,
                        CacheDirectives.`max-age`(31536000)))
                  ),
                  entity = HttpEntity(c, byteArray))
              }
          }
        }
      }

  def ping =
    path("ping") {
      get {
        parameter(Symbol("origin").?) { origin =>
          val now = dateFormatted(Instant.now().getEpochSecond, commentDateFormat, None)
          complete(ServerResponse(0, s"$now: I'm alive\n"))
        }
      }
    }

  def checkSolution =
    logRequestResult("check_solution") {
      path("check_solution") {
        get {
          parameter(Symbol("nameContains").as[String], Symbol("id").as[Int]) { (nameContains, id) =>
            if (nameContains.length < 3) {
              complete(ServerResponse(1, "implementation should be at least 3 chars long"))
            }
            else
              onComplete(learningService.checkSolution(nameContains, id)) {
                case Success(result) => complete(ServerResponse(0, result))
                case Failure(th) => logger.error(s"Failed to compile: $th")
                  complete(ServerResponse(1, th.getMessage))
              }
          }

        }
      }
    }

}

object GeneralRoutes {
  final private val WorkingDirectory = System.getProperty("user.dir") + "/images/"
  final private val PngPathRex = """\w*.png\b""".r
  final private val JpegPathRex = """\w*.jpeg\b""".r
}
