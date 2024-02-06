package com.iloonga.kids.pages.styles

/**
 * Provides functions to create CSS values.
 */
object CssValues {


  /**
   * Creates the "rgba" value for a color in CSS.
   *
   * @param red   the red part of the color
   * @param green the green part of the color
   * @param blue  the blue part of the color
   * @param alpha the transparent part of the color
   * @return the "rgba" value for a color in CSS
   */
  def rgba(red: Int, green: Int, blue: Int, alpha: Double): String = s"rgba($red,$green,$blue,$alpha)"

  /**
   * Creates the "rgb" value for a color in CSS.
   *
   * @param red   the red part of the color
   * @param green the green part of the color
   * @param blue  the blue part of the color
   * @return the "rgb" value for a color in CSS
   */
  def rgb(red: Int, green: Int, blue: Int): String = s"rgb($red,$green,$blue)"

  /**
   * Creates the CSS value for using an image via a URL by wrapping the `value` in `url(...)`.
   *
   * @param value this is the URL that we'll convert to a CSS value
   * @return the `value` wrapped in `url(..)`
   */
  def url(value: String) = s"url($value)"

  /**
   * Creates the "em" value in CSS, representing the element's calculated font size.
   * <p>
   * See also: https://developer.mozilla.org/en-US/docs/Web/CSS/length#em
   * @param value the value prepended to "em"
   * @return the value concatenated with "em"
   */
  def em(value: Double): String = s"${value}em"

}
