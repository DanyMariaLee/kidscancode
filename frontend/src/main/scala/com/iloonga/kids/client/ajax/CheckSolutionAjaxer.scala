package com.iloonga.kids.client.ajax

import sharedstuff.api.{AjaxApi, GeneralRoutAjaxApi}
import sharedstuff.serverresponses.ServerResponse
import org.scalajs.dom
import sharedstuff.serverresponses.ServerResponseImplicits.srRW
import slogging.LazyLogging
import autowire._
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import upickle.default._
import scala.scalajs.js.URIUtils

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object CheckSolutionAjaxer extends autowire.Client[String, upickle.default.Reader, upickle.default.Writer]
  with LazyLogging {

  def checkSolution(nameContains: String, id: Int): Future[ServerResponse] =
    CheckSolutionAjaxer[GeneralRoutAjaxApi].checkSolution(nameContains, id).call()

  override def doCall(req: Request): Future[String] = {
    val url = AjaxApi.ajaxRootPath + "check_solution"

    dom.console.log("req " + req.args)
    val inputString = req.args("nameContains").drop(1).dropRight(1)
    val id = req.args("id")

    val encoded = URIUtils.encodeURIComponent(inputString)
    val uri = url + "?nameContains=" + encoded + "&id=" + id

    dom.ext.Ajax.get(
      url = uri
    ).map(xmlHttpRequest => xmlHttpRequest.responseText)
  }

  // There must be a Reader[Result] implicitly available
  def read[Result: upickle.default.Reader](string: String): Result = {
    val sr = Try {
      val input = string
        .replaceAll("\"", "")
        .replaceAll("ServerResponse\\(", "")
        .dropRight(1)
        .split(",")
      ServerResponse(input.head.toInt, input.tail.mkString(",")).asInstanceOf[Result]
    }

    sr match {
      case Success(value) => value
      case Failure(exception) =>
        throw exception
    }
  }

  // There must be a Writer[Result] implicitly available
  def write[Result: upickle.default.Writer](result: Result): String = upickle.default.write(result)
}
