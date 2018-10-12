package library.api

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import org.springframework.hateoas.ResourceSupport

@JsonInclude(NON_NULL)
data class BookResource(
    val isbn: String,
    val title: String,
    val borrowed: Boolean,
    val borrower: String?
) : ResourceSupport()