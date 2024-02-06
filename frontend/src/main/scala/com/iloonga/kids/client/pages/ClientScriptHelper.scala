package com.iloonga.kids.client.pages

import com.iloonga.kids.client.utils.ElementClasses
import org.scalajs.dom._
import scalatags.JsDom.all._
import sharedstuff.pages.elements.HtmlId
import sharedstuff.pages.elements.classes.SharedClasses

import scala.scalajs.js

/**
 * This trait contains helper methods for the code that is run in the client's browser.
 */
trait ClientScriptHelper {

  def enable(elementId: HtmlId): Unit = ElementClasses.removeClass(elementId, SharedClasses.disabled)

  def show(element: js.Any): Unit = ElementClasses.removeClass(element, SharedClasses.hidden)
  def hide(element: js.Any): Unit = ElementClasses.addClass(element, SharedClasses.hidden)
  def hide(elementId: HtmlId): Unit = ElementClasses.addClass(elementId, SharedClasses.hidden)

  /**
   * Appends a `nodeToAppend` to the page's document body.
   *
   * @param nodeToAppend we append this [[Node]] to the body of the document
   * @return the appended node
   */
  def bodyAppend(nodeToAppend: Node): Node = document.body.appendChild(nodeToAppend)

  /**
   * Appends a `tagToAppend` to the page's document body.
   *
   * @param tagToAppend we convert this [[HtmlTag]] to a [[Node]] and append it to the body of the document
   * @return the appended node
   */
  def bodyAppend(tagToAppend: HtmlTag): Node = bodyAppend(tagToAppend.render)

}