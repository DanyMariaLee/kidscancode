package com.iloonga.kids.pages.utils

import java.io.StringWriter
import java.nio.charset.StandardCharsets
import java.util.Locale

import com.iloonga.kids.pages.gui.PageTitle
import com.iloonga.kids.pages.styles.SharedStyles
import slogging.LazyLogging
import scalatags.{Escaping, Text}
import scalatags.Text.TypedTag
import scalatags.Text.all._
import scalatags.Text.tags2.title
import scalatags.Text.tags2.style
import scalatags.stylesheet.StyleSheet
import sharedstuff.pages.PageId

/**
 * Helps building web pages which the servers sends to the client.
 */
trait PageUtils extends LazyLogging {

  // The application name as defined in build.sbt in lowercase
  private val appNameLower = "iloonga".toLowerCase(Locale.ROOT)// BuildInfo.name.toLowerCase(Locale.ROOT)
  // The client code we've written in Scala, translated to JavaScript
  val jsApp: String = "/" + appNameLower + ".js"

  // Client-side dependencies such as jQuery
  val jsDependencies = s"/$appNameLower-jsdeps.min.js"

  // Used for styling our web page
  val pureCss = "https://cdn.jsdelivr.net/pure/0.6.1/pure-min.css"

  // Defines which character encoding our web page uses
  val utf8: String = StandardCharsets.UTF_8.displayName

  /**
   * Adds "<!DOCTYPE html>" and then creates an HTML element containing the given `typedTags`.
   * <p>
   * The document type lets browsers know that we send them an HTML document.
   *
   * @param typedTags the content of the HTML element
   * @return a string with the HTML document type and the HTML element containing the given `typedTags`
   */
  def htmlWithDocType(typedTags: TypedTag[_]*): String = "<!DOCTYPE html>" + html(typedTags)

  /**
   * Creates an HTML comment with the given `content`.
   *
   * @param content the text of the HTML comment. Special characters like `<`, `&`, or `"` are escaped.
   * @return an HTML with the escaped `content`
   */
  def comment(content: String): Text.RawFrag = {
    val escapedContent = new StringWriter(content.length)
    // The method escapes the content and puts it into the StringBuilder
    Escaping.escape(content, escapedContent)
    raw(s"<!-- $escapedContent -->")
  }


  /**
   * Creates the default HTML head for pages of this application.
   * The head includes the encoding of the page, the client code for this application, and optionally the style sheet
   * for the page.
   *
   * @param pageId     used to identify this page and execute the correct JavaScript/Scala code at the client
   * @param pageTitle  the title of this page, appears in the browser's tab
   * @param styleSheet we add the contents of this [[StyleSheet]] to the head. Defaults to an empty style sheet
   * @return the default head for a page of this application
   */
  def defaultHead(pageId: PageId, pageTitle: PageTitle, styleSheet: StyleSheet = SharedStyles.emptyStyleSheet): TypedTag[String] =
    head(id := pageId.value)(
      meta(charset := utf8),
      title(pageTitle.value),
      // Include our application (the Scala code translated to JavaScript)
      script(src := jsApp),
      // Include the dependencies such as jQuery
      script(src := jsDependencies),
      // We credit Subtle Patterns since we use their content: https://www.toptal.com/designers/subtlepatterns/faq/
      comment("Background patterns from Subtle Patterns: https://www.toptal.com/designers/subtlepatterns"),

      link(rel := "stylesheet", href := pureCss),
      // Add the content of our custom style sheet for the page after the Pure CSS file so our styles get precedence
      // over properties defined in Pure CSS
      if (styleSheet != SharedStyles.emptyStyleSheet)
        style(`type` := "text/css")(styleSheet.styleSheetText)
      else raw("")
    )
}
