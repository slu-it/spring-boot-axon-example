package library.domain.aggregates

import library.domain.commands.AddBookCommand
import library.domain.commands.BorrowBookCommand
import library.domain.commands.ReturnBookCommand
import library.domain.events.BookAddedEvent
import library.domain.events.BookBorrowedEvent
import library.domain.events.BookReturnedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.model.AggregateIdentifier
import org.axonframework.commandhandling.model.AggregateLifecycle
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class Book {

    @AggregateIdentifier
    private lateinit var id: String
    private lateinit var isbn: String
    private lateinit var title: String

    private var borrowed: Boolean = false
    private var borrower: String? = null

    constructor()

    @CommandHandler
    constructor(command: AddBookCommand) {
        // TODO: check validity
        AggregateLifecycle.apply(
            BookAddedEvent(
                command.id,
                command.isbn,
                command.title
            )
        )
    }

    @EventSourcingHandler
    fun handle(event: BookAddedEvent) {
        id = event.id
        isbn = event.isbn
        title = event.title
    }

    @CommandHandler
    fun on(command: BorrowBookCommand) {
        check(!borrowed) { "book is already borrowed" }
        AggregateLifecycle.apply(
            BookBorrowedEvent(
                id,
                command.borrower
            )
        )
    }

    @EventSourcingHandler
    fun handle(event: BookBorrowedEvent) {
        borrowed = true
        borrower = event.borrower
    }

    @CommandHandler
    fun on(command: ReturnBookCommand) {
        check(borrowed) { "book is not borrowed" }
        AggregateLifecycle.apply(BookReturnedEvent(id))
    }

    @EventSourcingHandler
    fun handle(event: BookReturnedEvent) {
        borrowed = false
        borrower = null
    }

}