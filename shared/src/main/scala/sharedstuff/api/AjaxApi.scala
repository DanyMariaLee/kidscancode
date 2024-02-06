package sharedstuff.api

import sharedstuff.serverresponses.ServerResponse

import scala.concurrent.Future

/**
 * Defines the Ajax calls the client can make to the server for the person service.
 * Macro is building the request url and we need def names to be exact values of akka http API
 * <p>
 */
trait AuthRouteAjaxApi {
  def register: Future[ServerResponse]
}

trait GeneralRoutAjaxApi {
  def heartbeat: Future[ServerResponse]
  def checkSolution(nameContains: String, id: Int): Future[ServerResponse]
}

/**
 * Provides paths for the Ajax API.
 */
object AjaxApi {
  // Used for performing Ajax calls from the client to the server
  val ajaxRootPath = "/"
}