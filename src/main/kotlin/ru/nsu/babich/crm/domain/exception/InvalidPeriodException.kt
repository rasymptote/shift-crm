package ru.nsu.babich.crm.domain.exception

class InvalidPeriodException :
    DomainException(
        "Start date must be before end date",
    )
