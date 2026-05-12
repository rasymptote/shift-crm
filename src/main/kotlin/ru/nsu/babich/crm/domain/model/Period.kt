package ru.nsu.babich.crm.domain.model

import ru.nsu.babich.crm.domain.exception.InvalidPeriodException
import java.time.LocalDateTime

data class Period(
    val from: LocalDateTime,
    val to: LocalDateTime,
) {
    init {
        if (to.isBefore(from)) {
            throw InvalidPeriodException()
        }
    }
}
