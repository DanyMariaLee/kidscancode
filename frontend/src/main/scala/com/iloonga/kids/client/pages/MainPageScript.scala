package com.iloonga.kids.client.pages

import org.scalajs.dom
import org.scalajs.dom.raw.{HTMLAnchorElement, HTMLButtonElement, HTMLDivElement, HTMLImageElement, HTMLParagraphElement}
import org.scalajs.dom.{document, window}
import sharedstuff.domain.PopularTopic

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object MainPageScript {

  def execute(): Unit = window.onload = _ => {

  /*  val menu = document.getElementById(MainMenuIds.menu.value).asInstanceOf[HTMLDivElement]
    val suggestionContainer = document.getElementById(LogsElementIds.scrollableContent.value).asInstanceOf[HTMLDivElement]

    val fakeLastDiv = document.createElement("div").asInstanceOf[HTMLDivElement]
    fakeLastDiv.style = "height:30px;"
    fakeLastDiv.className = "row"

    SuggestionAjaxer.suggestions.map {
      case ServerResponseFindHacker(0, _, suggestedHackers) =>
        suggestedHackers.map(buildTrendingTopicsDiv).foreach(suggestionContainer.appendChild)
        suggestionContainer.appendChild(fakeLastDiv)
      case o => showErrorBox(toInfoAboutServerResponseFind(o))
    }
      .recover { th => setFlatlineHeartbeat }
      .map { _ =>
        val loadingPic = document.getElementById("loading").asInstanceOf[HTMLDivElement]
        loadingPic.style.display = "none"
      }

  }

  def buildTrendingTopicsDiv(pt: PopularTopic): HTMLDivElement = {
    val fhDiv = dom.document.createElement("div").asInstanceOf[HTMLDivElement]
    fhDiv.className = "row"
    fhDiv.style ="height:7vh;width:95vw;"

    val avatarDiv = dom.document.createElement("div").asInstanceOf[HTMLDivElement]
    avatarDiv.className = "col-md-4 col-xl-1 column"
    avatarDiv.style = if (isMobile) "width:13vw;" else "width:7vw;"
    val avatarAnchor = dom.document.createElement("a").asInstanceOf[HTMLAnchorElement]
    val href = s"""/hacker/${pt.name}"""
    avatarAnchor.href = href
    val avatar = dom.document.createElement("div").asInstanceOf[HTMLDivElement]
    avatar.style =
      s"""
     background-position: center;
     display: block;
     margin-left: auto;
     margin-right: auto;
     width: 6vh;
     height: 6vh;
     border-radius: 50%;
     border: 0.5px solid #40a855;
     background-image: url('${pt.picture}');
     background-size:     cover;
     background-repeat:   no-repeat;
     background-position: center center;"""

    avatarAnchor.appendChild(avatar)
    avatarDiv.appendChild(avatarAnchor)

    val nameDiv = dom.document.createElement("div").asInstanceOf[HTMLDivElement]
    nameDiv.className = "col-md-4"
    nameDiv.style = "max-width:40vw;overflow: hidden; font-family: 'Anonymous Pro', monospace;font-size: 2vh;padding: 8px;margin:10px;text-align:left;"

    nameDiv.innerHTML =
      s"""<a style="text-decoration: none;font-size:2vh;color: rgb(64, 168, 85);" href="$href">""" + fh.hackerName + """</a>"""

    val progLangDiv = dom.document.createElement("div").asInstanceOf[HTMLDivElement]
    progLangDiv.className = "col-md-4 col-xl-2"
    progLangDiv.style = "width:40vw;color: rgb(255, 255, 255); font-family: 'Anonymous Pro', monospace;font-size: 1.8vh;padding: 6px 0px 0px 0px; margin: 10px 0px 0px 0px;text-align:left;"

    progLangDiv.innerText = if (fh.progLang.exists(_.trim.isEmpty) || fh.progLang.isEmpty)
      "no lang picked" else fh.progLang.getOrElse("")

    val weightDiv = dom.document.createElement("div").asInstanceOf[HTMLDivElement]
    weightDiv.className = "col-md-4 col-xl-2"
    weightDiv.style = "max-width: 10vw; color: rgb(255, 255, 255); font-family: 'Anonymous Pro', monospace; font-size: 1.8vh; padding: 6px 0px 0px 0px; margin: 10px 0px 0px 0px;text-align:left;"

    weightDiv.innerText = fh.weight

    fhDiv.appendChild(avatarDiv)
    fhDiv.appendChild(nameDiv)

    if (isMobile) {} else {
      fhDiv.appendChild(progLangDiv)
    }

    fhDiv.appendChild(weightDiv)
    fhDiv*/
  }
}
