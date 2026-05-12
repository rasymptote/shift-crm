package ru.nsu.babich.crm.application.dto

import ru.nsu.babich.crm.domain.model.Period
import java.math.BigDecimal

data class GetSellersWithTurnoverLessThanDto(
    val threshold: BigDecimal,
    val period: Period,
)
