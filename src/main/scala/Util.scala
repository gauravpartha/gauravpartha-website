package app 

import org.commonmark.node.*
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import app.data.*

import scalatags.Text.all._

object Util {

  given upickle.default.ReadWriter[PublicationUrl] = upickle.default.macroRW
  given upickle.default.ReadWriter[Publication] = upickle.default.macroRW

  given upickle.default.ReadWriter[TalkUrl] = upickle.default.macroRW
  given upickle.default.ReadWriter[Talk] = upickle.default.macroRW

  def markdownToHtml(name: String) : Option[String] = {
    val path = os.pwd / "resources" / "md" / s"$name.md"

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
    val json = os.read(pathToJson)
    val publications = upickle.default.read[Seq[Publication]](json)

    val pubsInDiv = 
      for (pub <- publications) yield  {
        
        val yearAndVenueText = 
          (Seq(pub.year.toString(), pub.venue) ++
          pub.venueAbbrev.fold[Seq[String]](Nil)(abbrev => Seq(s"($abbrev)"))).mkString(" ")
        
        div(cls := "mb-2",
          div(b(pub.title)),
          authorsToHtml(pub.authors),
          div(yearAndVenueText),
          publicationUrlToHtml(pub.url)
        )
      }

    div(pubsInDiv)
  }

  private def authorsToHtml(authors: List[String]) : scalatags.Text.TypedTag[String] = {
    val authorsHtmlList : List[scalatags.Text.TypedTag[String]] = 
      authors.zipWithIndex.map(
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

    div(authorsHtmlList)
  }

  private def publicationUrlToHtml(pubUrl: PublicationUrl) : scalatags.Text.TypedTag[String] = {
    val urlList : Seq[(String, String)] = 
      Seq((pubUrl.pdf, "pdf"), (pubUrl.publisher, "publisher")) ++
      (pubUrl.extended match {
        case None => Nil
        case Some(extendedUrl) => Seq((extendedUrl, "extended version"))
      })
    
    div(urlList.flatMap{ (url, name) => Seq(a(href := url, s"[$name]"), frag(" ")) }.dropRight(1))
  }

  def talksToHtml() : scalatags.Text.TypedTag[String] = {
    val pathToJson = os.pwd / "resources" / "json" / "talks.json"
    val json = os.read(pathToJson)
    val talks = upickle.default.read[Seq[Talk]](json)
    
    val talksInDiv = 
      for (talk <- talks) yield  {
        val locationText = 
          if(talk.location.toLowerCase() == "virtual") {
            "virtually"
          } else {
            s"in ${talk.location}"
          }

        div(cls := "mb-2",
            div(b(talk.title)),
            div(
              Seq(span(s"${talk.year.toString()}  "), 
                    a(href := talk.url.event, talk.venue), 
                    span(s" $locationText")),
              talkUrlToHtml(talk.url)
            )
        )
      }
      
    div(talksInDiv)
  }

  def talkUrlToHtml(url: TalkUrl) = {
    val urlList : Seq[(String, String)] = 
      url.slides.fold(Seq.empty)(r => Seq((r, "slides"))) ++
      url.recording.fold(Seq.empty)(r => Seq((r, "recording")))

    div(urlList.flatMap{ (url, name) => Seq(a(href := url, s"[$name]"), frag(" ")) }.dropRight(1))
  }
  
}