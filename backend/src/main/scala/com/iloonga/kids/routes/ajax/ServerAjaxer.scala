package com.iloonga.kids.routes.ajax

import akka.http.scaladsl.server.{Directives, Route}
import com.iloonga.kids.AppServer
import com.iloonga.kids.pages.utils.http.HeaderLogger
import sharedstuff.api.{AjaxApi, AuthRouteAjaxApi}
import slogging.LazyLogging
import upickle.default._
import upickle.default.{Reader, Writer, _}
import sharedstuff.serverresponses.ServerResponseImplicits._

import scala.concurrent.{ExecutionContext, Future}

/**
 * Lets the server respond to Ajax requests from the client.
 */
object ServerAjaxer extends Directives with LazyLogging {

  /** Creates a [[Route]] that responds to an Ajax request. The response is not encoded. */
  def respond(implicit ec: ExecutionContext): Route =
    post(
      path(AjaxApi.ajaxRootPath / Segments)(methodToCall =>
        decodeRequest(
          entity(as[String])(requestData => {
            HeaderLogger.logAjax(methodToCall)(
              // We don't encode since encoding a small (~100 B) response makes it a few bytes larger
              complete(handleAjaxRequest(methodToCall, requestData))
            )
          }
          )
        )
      )
    )

  /**
   * Used for Ajax calls from the client.
   */
  private object AjaxRouter extends autowire.Server[String, Reader, Writer] {

    // There must be a Reader[Result] implicitly available
    def read[Result: Reader](string: String): Result = {
      upickle.default.read[Result](string)
    }

    // There must be a Writer[Result] implicitly available
    def write[Result: Writer](result: Result): String = {
      upickle.default.write(result)
    }
  }

  /**
   * Handles an Ajax request sent from the client by calling a method on the [[AppServer]].
   *
   * @param methodToCall
   *                    contains the package, the class, and the name of the API method to call on the [[AppServer]]
   * @param requestData the request data from the client, such as JSON
   * @return the future answer to the request
   */
  private def handleAjaxRequest(methodToCall: List[String], requestData: String
                               )(implicit ec: ExecutionContext): Future[String] = {
    // Turn the JSON inside the request into a map
    val requestDataAsMap = upickle.default.read[Map[String, String]](requestData)
    val request = autowire.Core.Request(methodToCall, requestDataAsMap)
    // Make the server respond to the request
    AjaxRouter.route[AuthRouteAjaxApi](AppServer)(request)
  }
}
