package app

import scalatags.Text.all._

object Website {

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
            Seq(
              Seq(mainTextHtml),
              Seq(
                h3("Research Publications"),
                Util.publicationsToHtml()
              ),
              Seq(
                h3("Talks"),
                Util.talksToHtml()
              )
            ).map(div(cls := divContentClass)(_))
          )
        )
      )
    )
  }

  def storeHtml() = {
    val outputFolder = os.pwd / "output"
    if(os.exists(outputFolder)) {
      println("Removing existing output folder")
      os.remove.all(outputFolder)
    }

    println("Creating output folder")
    os.makeDir(outputFolder)
    val targetPath = outputFolder / "index.html"

    os.copy(os.pwd / "resources" / "papers", outputFolder / "papers")
    os.copy(os.pwd / "resources" / "slides", outputFolder / "slides")

    println(s"Writing HTML file to $targetPath")
    os.write.over(targetPath, aboutPageHtml.render)
  }

  def main(args: Array[String]): Unit = {
    storeHtml()
  }

}