package com.iloonga.kids.pages.utils.http

import akka.http.scaladsl.model.{StatusCodes, _}
import akka.http.scaladsl.server.{Directives, Route}
import com.iloonga.kids.pages.Page

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Helps with creating HTTP responses.
 */
object HttpHelper extends Directives {

  /**
   * Logs the headers of a client that requested a resource and responds with the given `responseBody`
   * and `statusCode`.
   */
  def logHeadersAndRespondWith(responseBody: ResponseEntity,
                               statusCode: StatusCode,
                               resourceName: String): Route =

    HeaderLogger.logResourceRequest(resourceName,
      complete(HttpResponse(status = statusCode, entity = responseBody)
      )
    )

  /**
   * Logs the headers of a client that requested a `page` and responds with that page and status code 200 (OK) if no other code is given.
   *
   * If the client accepts encoded responses, we will encode the respond using gzip or deflate.
   **/
  def logHeadersAndRespondWithEncoded(page: Page, statusCode: StatusCode = StatusCodes.OK): Route =
    HeaderLogger.logPageRequest(page.pageId, respondEncoded(page, statusCode))

  /**
   * Responds with an encoded `page` and include the given `statusCode` in the response.
   **/
  private def respondEncoded(page: Page, statusCode: StatusCode): Route =
    encodeResponse(
      complete(
        // Creates an HTTP entity for the client with the page content
        page.mkHtml.map(pageContent =>
          HttpResponse(statusCode, entity = HttpEntity(ContentTypes.`text/html(UTF-8)`, pageContent)))
      )
    )
}
