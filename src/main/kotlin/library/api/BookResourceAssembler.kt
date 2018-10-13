package library.api

import library.domain.projections.Book
import org.springframework.hateoas.mvc.ControllerLinkBuilder
import org.springframework.hateoas.mvc.ControllerLinkBuilder.*
import org.springframework.hateoas.mvc.ResourceAssemblerSupport
import org.springframework.stereotype.Component

@Component
class BookResourceAssembler :
    ResourceAssemblerSupport<Book, BookResource>(BooksRestController::class.java, BookResource::class.java) {

    override fun toResource(book: Book): BookResource {
        val resource = createResourceWithId(book.id, book)

        if (book.borrowed) {
            resource.add(linkTo(BooksRestController::class.java).slash(book.id).slash("return").withRel("return"))
        } else {
            resource.add(linkTo(BooksRestController::class.java).slash(book.id).slash("borrow").withRel("borrow"))
        }

        return resource
    }

    override fun instantiateResource(book: Book) = BookResource(
        isbn = book.isbn,
        title = book.title,
        borrower = book.borrower
    )
}