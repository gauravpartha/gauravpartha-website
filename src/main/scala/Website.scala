package app

import scalatags.Text.all._

object Website extends cask.MainRoutes {

  val bootstrap = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"

  lazy val aboutPageHtml = {
    val mainTextHtml =
      Util.markdownToHtml("about") match {
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
              mainTextHtml
            ),
            div(cls := "m-5")(
              h2("Research Publications"),
              Util.publicationsToHtml()
            )
          )
        )
      )
    )
  }

  // Save the HTML content when the server initializes
  println(os.pwd / "index.html")
  os.write(os.pwd / "index.html", aboutPageHtml.render)


  @cask.get("/")
  def aboutPage() = {
    aboutPageHtml
  }

  initialize()

}