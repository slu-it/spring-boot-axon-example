package library.domain.projections

import library.domain.events.BookAddedEvent
import library.domain.events.BookBorrowedEvent
import library.domain.events.BookReturnedEvent
import library.domain.projections.queries.FetchAllBooksQuery
import library.domain.projections.queries.FindByIdQuery
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import java.util.concurrent.CopyOnWriteArrayList

@Component
class BookSummaryProjection {

    private val books: MutableList<Book> = CopyOnWriteArrayList()

    @EventHandler
    fun on(event: BookAddedEvent) {
        books.add(
            Book(
                id = event.id,
                isbn = event.isbn,
                title = event.title,
                borrowed = false,
                borrower = null
            )
        )
    }

    @EventHandler
    fun on(event: BookBorrowedEvent) {
        books.stream()
            .filter { it.id == event.id }
            .findFirst()
            .ifPresent {
                it.borrowed = true
                it.borrower = event.borrower
            }
    }

    @EventHandler
    fun on(event: BookReturnedEvent) {
        books.stream()
            .filter { it.id == event.id }
            .findFirst()
            .ifPresent {
                it.borrowed = false
                it.borrower = null
            }
    }

    @QueryHandler
    fun fetch(query: FetchAllBooksQuery): List<Book> {
        return books.toList()
    }

    @QueryHandler
    fun fetch(query: FindByIdQuery): Book? {
        return books.singleOrNull { it.id == query.id }
    }

}