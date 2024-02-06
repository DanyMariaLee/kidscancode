package com.iloonga.kids

import java.io.InputStream
import java.security.spec.ECGenParameterSpec
import java.security.{KeyFactory, KeyPair, KeyPairGenerator, KeyStore, SecureRandom, Security}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, RejectionHandler}
import cats.effect.IO
import com.iloonga.kids.iloongaConfig._
import com.iloonga.kids.routes.{AuthRoutes, GeneralRoutes}
import com.iloonga.kids.routes.ajax.ServerAjaxer
import com.typesafe.config.{Config, ConfigFactory}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import com.iloonga.kids.service.{LearningServiceImpl, ProblemService, ProblemServiceImpl}

object AppServer extends JsonProtocols with AuthRoutes {

  val config: Config = ConfigFactory.load().getConfig("jwt")
  val cfg: Config = ConfigFactory.load() //.getConfig("akka")

  implicit val actorSystem = ActorSystem("iloonga-api-system")
  sys.addShutdownHook(actorSystem.terminate())

  implicit val ec = actorSystem.dispatcher //s.lookup("my-blocking-dispatcher")
  implicit val cs = IO.contextShift(ec)

  val problemService = new ProblemServiceImpl()
  val problems = problemService.allProblems

  val learningService = new LearningServiceImpl(problems, ec)
  val generalRoutes = new GeneralRoutes(learningService).generalRoutes
  val corsSettings = CorsSettings.defaultSettings.withAllowedMethods(Seq(GET, PUT, POST, DELETE))

  val routes =
    cors(corsSettings)(
      handleRejections(rejectionHandler) {
        get {
          path("frontend-jsdeps.js")(getFromResource("assets/js/frontend-jsdeps.js")) ~
            path("frontend-jsdeps.min.js")(getFromResource("assets/js/frontend-jsdeps.min.js")) ~
            path("frontend-fastopt.js")(getFromResource("assets/js/frontend-fastopt.js")) ~
            path("frontend-fastopt.js.map")(getFromResource("assets/js/frontend-fastopt.js.map")) ~
            path("manifest.webmanifest")(getFromResource("manifest.webmanifest")) ~
            path("manifest.json")(getFromResource("manifest.json"))
        } ~
          get(path("about.html")(getFromResource("about.html"))) ~
          get(path("about")(getFromResource("about.html"))) ~
          get(pathSingleSlash(getFromResource("index.html"))) ~
          get(path("index.html")(getFromResource("index.html"))) ~
          get(path("learn")(getFromResource("learn.html"))) ~
          get(path("page_not_found")(getFromResource("page_not_found.html"))) ~
          generalRoutes ~
          ServerAjaxer.respond(ec)
      }
    )


  /** Handles the case when the client tries to visit a page of this application that doesn't exist. More generally,
   * we send this respond for every HTTP request that can't be handled by the server's routes. */
  private def rejectionHandler =
    RejectionHandler.newBuilder()
      .handleNotFound(
        getFromResource("page_not_found.html")
        //HttpHelper.logHeadersAndRespondWithEncoded(NotFoundPage, StatusCodes.NotFound)
      )
      .result()

  implicit def myExceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case th =>
        extractUri { uri =>
          println(s"Request to $uri could not be handled normally")
          complete(HttpResponse(InternalServerError, entity = th.getMessage))
        }
    }

  def up(): Unit = {

    def runHTTP() = {
      Http().bindAndHandle(routes, apiHost, apiPort)

      println(System.getProperty("user.dir") + "/images/")
      println("apiHost " + apiHost)
      println("apiPort " + apiPort)
      println(s"API online at http://$apiHost:$apiPort/\n" +
        "\nPress RETURN to stop...")
    }

    //runHTTPS()
    runHTTP()
  }


}
