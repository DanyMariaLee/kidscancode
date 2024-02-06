package sharedstuff.pages.elements

/**
 * The ID of an HTML element.
 */
case class HtmlId(value: String, fieldNameForFrontend: String) {
  override def toString: String = value
}
