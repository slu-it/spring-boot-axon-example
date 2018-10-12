package library.domain.events

data class BookAddedEvent(
    val id: String,
    val isbn: String,
    val title: String
)