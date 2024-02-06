package com.iloonga.kids.client.utils

import org.scalajs.dom.Element
import org.scalajs.jquery.jQuery
import sharedstuff.pages.elements.HtmlId
import sharedstuff.pages.elements.classes.HtmlClass
import slogging.LazyLogging

import scala.scalajs.js

/**
 * Manipulates classes of HTML elements.
 */
object ElementClasses extends LazyLogging {

  def removeClass(elementId: HtmlId, classToRemove: HtmlClass): Unit =
    Elements.get[Element](elementId).fold(
      errorMsg => logger.warn("Can't remove class from element with ID {}. Error: {}", elementId, errorMsg),
      element => removeClass(element, classToRemove)
    )

  def removeClass(element: js.Any, classToRemove: HtmlClass): Unit = jQuery(element).removeClass(classToRemove.value)

  def addClass(elementId: HtmlId, classToAdd: HtmlClass): Unit =
    Elements.get[Element](elementId).fold(
      errorMsg => logger.warn("Can't add class to element with ID {}. Error: {}", elementId, errorMsg),
      element => addClass(element, classToAdd)
    )

  def addClass(element: js.Any, classToAdd: HtmlClass): Unit = jQuery(element).addClass(classToAdd.value)
}
