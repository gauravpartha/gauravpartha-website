package app 

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import scalatags.Text.all._

object Util {

  given upickle.default.ReadWriter[Publication] = upickle.default.macroRW

  def markdownToHtml(name: String) : Option[String] = {
    val path = os.pwd / "resources" / "md" / s"$name.md"
    println(path)

    if(os.exists(path)) {
      val parser = Parser.builder().build()
      val document = parser.parse(os.read(path))
      val renderer = HtmlRenderer.builder().build()
      Some(renderer.render(document));
    } else {
      None
    }
  }

  def publicationsToHtml() : scalatags.Text.TypedTag[String] = {
    val pathToJson = os.pwd / "resources" / "json" / "publications.json"
    if(os.exists(pathToJson)) {
      val json = os.read(pathToJson)
      val publications = upickle.default.read[Seq[Publication]](json)

      val pubsInDiv = 
        for (pub <- publications) yield  {
          val authorsHtmlList : List[scalatags.Text.TypedTag[String]] = 
            pub.authors.zipWithIndex.map(
              (a,id) =>  {
                val aHtml = 
                  if(a == "Gaurav Parthasarathy") {
                    span(style := "color: blue;",
                      i(a)
                    )
                  } else {
                    span(a)
                  }
                
                if(id == 0) {
                  aHtml
                } else {
                  span(", ", aHtml)
                }
              }
            )
          
          val yearAndVenueText = 
            (Seq(pub.year.toString(), pub.venue) ++
            pub.venueAbbrev.fold[Seq[String]](Nil)(abbrev => Seq(s"($abbrev)"))).mkString(" ")

          Seq(div(b(pub.title)), div(authorsHtmlList))
          div(cls := "mb-2",
            div(b(pub.title)),
            div(authorsHtmlList),
            div(yearAndVenueText)
          )
        }

      div(pubsInDiv)
    } else {
      div(cls := "mb-2",
        "Error: Publications could not be retrieved"
      )
    }
  }
  
}

case class Publication(
  title: String,
  authors: List[String],
  year: Int,
  month: Int,
  publisherUrl: String,
  journal: Option[String],
  venue: String,
  venueAbbrev: Option[String]
)
