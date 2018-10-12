package library.domain.projections

data class Book(
    val id: String,
    val isbn: String,
    val title: String,
    var borrowed: Boolean,
    var borrower: String?
)