package com.iloonga.kids.client

import com.iloonga.kids.client.pages.{LearnPageScript, MainPageScript}
import org.scalajs.dom
import org.scalajs.dom._
import sharedstuff.pages.{PageId, PageIds}

object Client {

  def main(args: Array[String]): Unit = {
    PageId(document.head.id) match {
      case PageIds.mainPageId => MainPageScript.execute()
      case PageIds.learnPageId => LearnPageScript.execute()
      case otherPageId => dom.console.warn("There's no client code for a page with this head ID '{}'", otherPageId)
    }
  }
}