package com.iloonga.kids.client.utils

import org.scalajs.dom.raw.{HTMLCollection, HTMLElement, HTMLInputElement}
import org.scalajs.dom.{Element, Node, document}
import sharedstuff.pages.elements.HtmlId
import slogging.LazyLogging

import scala.reflect.ClassTag

/**
 * Helps with accessing and creating DOM elements.
 */
object Elements extends LazyLogging {

  def getValueOfInput(inputId: HtmlId): Either[ErrorMsg, String] =
    get[HTMLInputElement](inputId).map(inputElement => inputElement.value)

  /**
   * Gets an element of type `T` by an `elementId`. Returns either the element if found
   * or an [[ErrorMsg]].
   *
   * Usage:
   * {{{
   * Elements.get[Canvas](Ids.canvasId).fold(
   *   errorMsg => logger.warn("Could not find canvas. Error is {}", errorMsg),
   *   canvas => logger.info("Got canvas {}", canvas)
   * )
   * }}}
   */
  def get[T: ClassTag](elementId: HtmlId): Either[ErrorMsg, T] =
    document.querySelector(s"#$elementId") match {
      case elem: T => Right(elem)
      case other => Left(ErrorMsg(s"Element with ID $elementId is $other"))
    }

  def setChild(parent: HTMLElement, newChild: HTMLElement): Node = {
    removeAllChildren(parent)
    parent.appendChild(newChild)
  }

  def removeAllChildren(parent: HTMLElement): Unit =
    forEachElement(parent.children) { child => parent.removeChild(child) }

  def forEachElement(htmlCollection: HTMLCollection)(action: (Element => Unit)): Unit =
    (0 until htmlCollection.length)
      .map(htmlCollection.item)
      .foreach(action)
}
