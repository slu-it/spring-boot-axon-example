package library.domain.commands

import org.axonframework.commandhandling.TargetAggregateIdentifier

data class ReturnBookCommand(
    @TargetAggregateIdentifier val id: String
)