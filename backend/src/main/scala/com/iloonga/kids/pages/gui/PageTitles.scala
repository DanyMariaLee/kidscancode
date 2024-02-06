package com.iloonga.kids.pages.gui

/**
 * Provides the titles of web pages.
 */
object PageTitles {

  val welcome = PageTitle("Hello there")
  val examplesOverview = PageTitle("Examples")
  val form = PageTitle("Person form")
  val peopleList = PageTitle("Saved people")
  val drawing = PageTitle("Drawing")
  val uploadFile = PageTitle("Upload file")

  val notFound = PageTitle("Page not found")
}

case class PageTitle(value: String) extends AnyVal {
  override def toString: String = value
}