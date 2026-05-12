package ru.nsu.babich.crm.domain.exception

import ru.nsu.babich.crm.domain.model.Period

class NoSellersFound(
    period: Period,
) : DomainException(
        "No sellers found for period $period",
    )
