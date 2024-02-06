package sharedstuff.pages.elements.classes

/**
 * The class of an HTML element.
 */
case class HtmlClass(value: String) extends AnyVal {
  override def toString: String = value
}
