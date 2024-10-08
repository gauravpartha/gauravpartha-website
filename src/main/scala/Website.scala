package app

import scalatags.Text.all._

object Website extends cask.MainRoutes {

  val bootstrap = "https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"

  lazy val aboutPageHtml = {
    val divContentClass = "m-5"
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
            div(cls := divContentClass)(
              mainTextHtml
            ),
            div(cls := divContentClass)(
              h2("Research Publications"),
              Util.publicationsToHtml()
            )
          )
        )
      )
    )
  }

  // Save the HTML content when the server initializes
  def storeHtml() = {
    val targetPath = os.pwd / "index.html"
    println(s"Writing HTML file to $targetPath")
    os.write.over(targetPath, aboutPageHtml.render)
  }

  @cask.get("/")
  def aboutPage() = {
    aboutPageHtml
  }

  initialize()

  storeHtml()

  override def main(args: Array[String]): Unit = {
    if (args.contains("--start-server")) {
      println("Starting server...")
      super.main(args)
    } else {
      println("Server not started, just generating and saving html. Provide '--start-server' argument to start.")
    }
  }

}