package app

import scalatags.Text.all._

object Website extends cask.MainRoutes {

  val bootstrap = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
  lazy val aboutHtmlOption = Util.markdownToHtml("about")

  @cask.get("/")
  def aboutPage() = {
    val aboutHtml =
      aboutHtmlOption match {
        case Some(aboutHtml) =>
          raw(aboutHtml)
        case None =>
          p("error: About page missing")
      }

    doctype("html")(
      html(
        head(
          meta(charset := "UTF-8"),
          link(rel := "stylesheet", href := bootstrap),
        ),
        body(
          div(cls := "container")(
            div(cls := "m-5")(
              aboutHtml
            )
          )
        )
      )
    )
  }

  initialize()

}