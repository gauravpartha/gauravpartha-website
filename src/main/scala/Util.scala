package app 

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

object Util {

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

  def publicationsToHtml() : Option[String] = {
    val pathToJson = os.pwd / "resources" / "json" / "publications.json"
    if(os.exists(pathToJson)) {
      ???
    } else {
      None
    }
  }
  
}
