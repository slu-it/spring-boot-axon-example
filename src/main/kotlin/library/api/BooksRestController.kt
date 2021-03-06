package library.api

import library.domain.commands.AddBookCommand
import library.domain.commands.BorrowBookCommand
import library.domain.commands.ReturnBookCommand
import library.domain.projections.Book
import library.domain.projections.queries.FetchAllBooksQuery
import library.domain.projections.queries.FindByIdQuery
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.commandhandling.model.AggregateNotFoundException
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.queryhandling.responsetypes.ResponseType
import org.axonframework.queryhandling.responsetypes.ResponseTypes
import org.springframework.hateoas.Resources
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

@RestController
@RequestMapping("/books")
class BooksRestController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway,
    private val resourceAssembler: BookResourceAssembler
) {

    private companion object {
        val singleType: ResponseType<Book> = ResponseTypes.instanceOf(Book::class.java)
        val listType: ResponseType<List<Book>> = ResponseTypes.multipleInstancesOf(Book::class.java)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun post(ucb: UriComponentsBuilder, @RequestBody request: AddBookRequest): ResponseEntity<BookResource> {
        val id = commandGateway.sendAndWait<String>(
            AddBookCommand(
                id = UUID.randomUUID().toString(),
                isbn = request.isbn,
                title = request.title
            )
        )
        val book = findById(id)
        return ResponseEntity.created(ucb.path("/books/$id").build().toUri()).body(book)
    }

    @GetMapping
    fun get(): Resources<BookResource> {
        val books = queryGateway.query(FetchAllBooksQuery, listType).get()
        return Resources(resourceAssembler.toResources(books))
    }

    @GetMapping("/{id}")
    fun findById(
        @PathVariable id: String
    ): BookResource {
        val book = queryGateway.query(FindByIdQuery(id), singleType).get()
            ?: throw AggregateNotFoundException(id, "book with ID [$id] not found")
        return resourceAssembler.toResource(book)
    }

    @PostMapping("/{id}/borrow")
    fun postForIdBorrow(
        @PathVariable id: String,
        @RequestBody request: BorrowBookRequest
    ): BookResource {
        commandGateway.sendAndWait<String>(BorrowBookCommand(id, request.borrower))
        return findById(id)
    }

    @PostMapping("/{id}/return")
    fun postForIdReturn(@PathVariable id: String): BookResource {
        commandGateway.sendAndWait<String>(ReturnBookCommand(id))
        return findById(id)
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(AggregateNotFoundException::class)
    fun handle(e: AggregateNotFoundException) {

    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(IllegalStateException::class)
    fun handle(e: IllegalStateException) {

    }

}
