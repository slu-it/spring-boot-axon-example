package library.domain.commands

import org.axonframework.commandhandling.TargetAggregateIdentifier

data class BorrowBookCommand(
    @TargetAggregateIdentifier val id: String,
    val borrower: String
)