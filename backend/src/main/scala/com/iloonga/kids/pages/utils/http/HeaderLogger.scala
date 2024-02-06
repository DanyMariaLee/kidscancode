package com.iloonga.kids.pages.utils.http

import akka.http.scaladsl.model.HttpHeader
import akka.http.scaladsl.server.{Directives, Route}
import sharedstuff.pages.PageId
import slogging.LazyLogging

/**
 * Logs [[HttpHeader]]s of clients that connect to our server.
 */
object HeaderLogger extends Directives with LazyLogging {

  /**
   * Logs the headers of the client who performed an Ajax request on info log level and returns the `innerRoute`.
   *
   * @param methodToCall the method to call for the Ajax request as a list of package names, object or class,
   *                     and finally the method name
   * @param innerRoute   we return this [[Route]] after having logged the client's header
   * @return the `innerRoute`
   */
  def logAjax(methodToCall: Seq[String])(innerRoute: Route): Route = {
    val formattedMethodToCall = methodToCall.mkString(".")
    val logHeaders = (headers: Seq[HttpHeader]) =>
      logger.info("Ajax request to '{}' from client with these headers: {}", formattedMethodToCall, format(headers))

    processHeaders(logHeaders, innerRoute)
  }

  /** Applies a function to a sequence of `HttpHeader`s and invokes a `Route` afterwards */
  private def processHeaders(headerFunc: (Seq[HttpHeader] => Unit), innerRoute: Route): Route =
  /* To get the IP address of the requesting machine, we need to have this piece of configuration in resources/application.conf:
      server {
      remote-address-header = on
      ...
   */
    extract(_.request.headers) { headers =>
      headerFunc(headers)
      innerRoute
    }

  /** Logs the headers of the client that requested a page and invokes the `innerRoute`. */
  def logPageRequest(pageId: PageId, innerRoute: Route): Route = {
    val logHeaders = (headers: Seq[HttpHeader]) =>
      logger.info("Request for page '{}' from client with these headers: {}", pageId, format(headers))

    processHeaders(logHeaders, innerRoute)
  }

  /** Logs the headers of the client that requested a resource and invokes the `innerRoute`. */
  def logResourceRequest(resourceName: String, innerRoute: Route): Route = {
    val logHeaders = (headers: Seq[HttpHeader]) =>
      logger.info("Request for resource '{}' from client with these headers: {}", resourceName, format(headers))

    processHeaders(logHeaders, innerRoute)
  }


  private def format(headers: Seq[HttpHeader]) = headers.map(header => s"${header.name}: ${header.value}").mkString(", ")

}
