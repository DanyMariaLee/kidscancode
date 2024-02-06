package com.iloonga.kids.client.utils

/**
 * An error message used for when something unexpected happened in this application.
 */
case class ErrorMsg(value: String) extends AnyVal {
  override def toString: String = value
}
