package ru.nsu.babich.crm.domain.exception

sealed class DomainException(
    message: String,
) : RuntimeException(message)
