package library.api

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.core.Relation

@Relation(
    value = "book",
    collectionRelation = "books"
)
@JsonInclude(NON_NULL)
data class BookResource(
    val isbn: String,
    val title: String,
    val borrower: String?
) : ResourceSupport()