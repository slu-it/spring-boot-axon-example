package library.domain.events

data class BookBorrowedEvent(
    val id: String,
    val borrower: String
)