package sharedstuff.pages

/**
 * Identifies web pages of this application.
 * The client code uses this ID to know which page it got from the server and thus which JavaScript code
 * to execute.
 */
case class PageId(value: String) extends AnyVal {
  override def toString: String = value
}

/**
 * Contains the IDs of all our web pages.
 */
object PageIds {

  val mainPageId   = PageId("main_page_id")
  val learnPageId   = PageId("learn_page_id")

  val allPages: List[PageId] = List(
    mainPageId
  )
}