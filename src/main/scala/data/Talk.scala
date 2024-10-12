package app.data

case class Talk(
  title: String,
  month: Int,
  year: Int,
  url: TalkUrl,
  location: String,
  venue: String
)

case class TalkUrl(
  slides: Option[String],
  recording: Option[String],
  event: String
)
