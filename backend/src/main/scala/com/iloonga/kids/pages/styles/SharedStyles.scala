package com.iloonga.kids.pages.styles

import scalatags.generic.StylePair
import scalatags.stylesheet.StyleSheet
import scalatags.text.Builder
import scalatags.Text.all._
import scalatags.Text.attrs
import scalatags.generic

/**
 * Provides styles shared between the pages of this application.
 */
trait SharedStyles extends StyleSheet {
  initStyleSheet()

  // A pair in CSS such as "cls: myClass"
  type AttributePair = generic.AttrPair[Builder, String]

  val pureButton: AttributePair = attrs.cls := "pure-button"
  val pureButtonPrimary: AttributePair = attrs.cls := "pure-button-primary"
  val pureForm: AttributePair = attrs.cls := "pure-form"
  val pureTable: AttributePair = attrs.cls := "pure-table"

  val centeredTable = cls(
    // Centers the table
    margin.auto,
  )

  val submitButton = cls(
    fontSize := "150%",
    marginTop := "1em"
  )
  val submitButtonClasses: String = s"${submitButton.name} ${pureButton.v} ${pureButtonPrimary.v}"

  val defaultPagePadding: StylePair[Builder, _] = padding := "1em"

  val centered = cls(
    textAlign.center
  )

  val emptyStyleSheet = new StyleSheet() {
    // We need to initialize the empty style sheet, otherwise an exception is thrown
    initStyleSheet()
  }
}

// This lets the shared styles be used in imports of other classes without the need for inheriting from them
object SharedStyles extends SharedStyles
