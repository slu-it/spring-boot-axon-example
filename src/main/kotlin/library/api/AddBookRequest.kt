package library.api

data class AddBookRequest(
    val isbn: String,
    val title: String
)