package sharedstuff.pages.elements.classes

import sharedstuff.pages.elements.classes.HtmlClass

/**
 * HTML classes shared between client and server.
 */
object SharedClasses {

  // Pure CSS defines a style for this class: "display: none !important"
  val hidden = HtmlClass("hidden")

  val disabled = HtmlClass("pure-button-disabled")
}
