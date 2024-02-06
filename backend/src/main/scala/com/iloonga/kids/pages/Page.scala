package com.iloonga.kids.pages

import com.iloonga.kids.pages.utils.PageUtils
import sharedstuff.pages.PageId

import scala.concurrent.Future

/**
 * All the web pages in this application share this trait.
 */
trait Page extends PageUtils {

  // Every page has an ID that we use for logging and in the page's head to execute the JavaScript associated with it
  def pageId: PageId

  // Creating the HTML on the server might involve a lengthy process such as querying the database. Thus, the pages
  // don't need to provide their HTML right away but can finish it in the future
  def mkHtml: Future[String]
}