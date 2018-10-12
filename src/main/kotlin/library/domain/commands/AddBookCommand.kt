package library.domain.commands

data class AddBookCommand(
    val id: String,
    val isbn: String,
    val title: String
)