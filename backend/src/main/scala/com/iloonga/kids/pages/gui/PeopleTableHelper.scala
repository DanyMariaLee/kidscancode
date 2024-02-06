package com.iloonga.kids.pages.gui

import scalatags.Text.TypedTag
import scalatags.Text.all._

/**
 * Helps building tables that can be used to enter or show data about people.
 */
object PeopleTableHelper {
  def head: TypedTag[String] = thead(tr(th("Name"), th("Age"), th("Occupation")))
}

