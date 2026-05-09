package ru.nsu.babich.crm.domain.exception

class TransactionNotFoundException(
    transactionId: Long,
) : DomainException(
        "Transaction with id $transactionId not found",
    )
