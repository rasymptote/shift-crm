package ru.nsu.babich.crm.domain.exception

class InvalidTransactionAmountException :
    DomainException(
        "Transaction amount must be greater than zero",
    )
