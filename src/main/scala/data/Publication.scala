package app.data

case class PublicationUrl(pdf: String, publisher:String, extended: Option[String])

case class Publication(
  title: String,
  authors: List[String],
  year: Int,
  month: Int,
  url: PublicationUrl,
  journal: Option[String],
  venue: String,
  venueAbbrev: Option[String]
)
