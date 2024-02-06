package com.iloonga.kids.client.pages

import com.iloonga.kids.client.ajax.CheckSolutionAjaxer
import com.iloonga.kids.client.utils.Elements
import scalatags.JsDom.all._
import org.scalajs.dom
import org.scalajs.dom._
import org.scalajs.dom.ext.AjaxException
import org.scalajs.dom.raw.{HTMLAnchorElement, HTMLButtonElement, HTMLDivElement, HTMLImageElement, HTMLInputElement, HTMLLabelElement, HTMLParagraphElement, HTMLTextAreaElement}
import org.scalajs.dom.{console, document, window}
import scalatags.JsDom.all.{s, span}
import sharedstuff.pages.elements.HtmlId
import sharedstuff.serverresponses.ServerResponse
import slogging.LazyLogging

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.scalajs.js.{Date, URIUtils}
import scala.util.{Failure, Success, Try}
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object LearnPageScript extends LazyLogging with ClientScriptHelper {

  def execute(): Unit = window.onload = _ => {
    onPageLoaded()
  }

  private def onPageLoaded(): Unit = {
    addButtonListeners()
  }

  val nameContainsId = HtmlId("nameContains", "name contains")

  private def addButtonListeners(): Unit = {

    val problemId = HtmlId("problemId", "problem id")

    val searchButtonId = HtmlId("search_button", "search button")
    val searchResultsContainerId = HtmlId("search_results", "search results")
    val serverResponseInfoBoxId = HtmlId("server-response-info", "server response info")

    val problemIdElement = dom.document.getElementById(problemId.value)
      .asInstanceOf[HTMLLabelElement]

    val searchStringInputField = dom.document.getElementById(nameContainsId.value)
      .asInstanceOf[HTMLInputElement]

    searchStringInputField.style =
      """border: solid 1px; overflow: auto; outline: none; box-shadow: none; resize: none;
        |color: rgb(0,0,0); font-family: 'Anonymous Pro', monospace; font-size: 4vh;
        |max-height: 10vh; background-color: rgba(255, 255, 255);""".stripMargin

    val searchButton = dom.document.getElementById(searchButtonId.value).asInstanceOf[HTMLButtonElement]

    searchButton.onclick = _ => runSearch(searchResultsContainerId, serverResponseInfoBoxId, problemIdElement)

    searchStringInputField.focus()
    searchStringInputField.onkeypress = { (event: KeyboardEvent) =>
      if (event.keyCode == 13) {
        searchButton.click()
        event.preventDefault()
      }
    }
  }

  def runSearch(searchResultContainerId: HtmlId, serverResponseBox: HtmlId, problemIdElement: HTMLLabelElement) = {
    val fakeParagraphId = HtmlId("fake_parapraph_id", "fake_parapraph_id")
    dom.console.log("problemId innerHTML " + problemIdElement.innerHTML)
    dom.console.log("problemId innerText " + problemIdElement.innerText)
    val problemId = problemIdElement.innerText.toInt

    clearTextInfoBox(serverResponseBox)
    val searchResultContainer = dom.document.getElementById(searchResultContainerId.value)
    searchResultContainer.innerHTML = ""
    val fakeDiv = document.createElement("p").asInstanceOf[HTMLParagraphElement]
    fakeDiv.id = fakeParagraphId.value
    fakeDiv.style = "margin-top:45px;"
    searchResultContainer.appendChild(fakeDiv)

    val searchStringInputField = dom.document.getElementById(nameContainsId.value)
      .asInstanceOf[HTMLInputElement]
    val stringInput = searchStringInputField.value

    CheckSolutionAjaxer.checkSolution(stringInput, problemId)
      .map {
        case ServerResponse(0, result) =>
          document.getElementById(fakeParagraphId.value)
            .asInstanceOf[HTMLParagraphElement].style.marginTop = "0px;"
          setTextInInfoBox(serverResponseBox, result)

        case ServerResponse(_, errorMsg) =>
          document.getElementById(fakeParagraphId.value)
            .asInstanceOf[HTMLParagraphElement].style.marginTop = "0px;"
          setTextInInfoBox(serverResponseBox, errorMsg)
      }
      .recover { th =>
        val err = toInfoAboutFailedCallToServer(th)
        document.getElementById(fakeParagraphId.value)
          .asInstanceOf[HTMLParagraphElement].style.marginTop = "0px;"
        setTextInInfoBox(serverResponseBox, err)
      }
  }

  def clearTextInfoBox(boxId: HtmlId): Unit =
    Try(dom.document.getElementById(boxId.value).asInstanceOf[HTMLParagraphElement]) match {
      case Success(infoBox) =>
        Elements.setChild(infoBox, span("").render)
        hide(infoBox)

      case Failure(error) => logger.warn("Couldn't find info box to clear and hide: {}", error)
    }

  def setTextInInfoBox(boxId: HtmlId, infoText: String, withTime: Boolean = true): Unit =
    Try(dom.document.getElementById(boxId.value).asInstanceOf[HTMLParagraphElement]) match {
      case Success(infoBox) =>
        val currentTime = new Date().toLocaleTimeString()
        val text = if (withTime) s"$currentTime: $infoText" else s"$infoBox"
        Elements.setChild(infoBox, span(text).render)
        show(infoBox)

      case Failure(error) => logger.warn("Couldn't find info box to show server response: {}", error)
    }

  def toInfoAboutFailedCallToServer: PartialFunction[Throwable, String] = {
    case AjaxException(request) =>
      dom.console.log("Ajax call failed. Status:" + request.responseText + "\n" + request.response)
      if (request.responseText.contains(
        "The server was not able to produce a timely response to your request")) request.responseText
      else "Ajax call failed" + request
    case otherError =>
      dom.console.log(s"Could not contact server. Exception: $otherError. Message: ${otherError.getMessage}.")
      "Could not contact server"
  }
}
