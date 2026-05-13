package ru.nsu.babich.crm.presentation.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class SellersWithTurnoverLessThanRequest(
    @field:Positive
    val threshold: BigDecimal,
    @field:Valid
    @field:NotNull
    val period: PeriodRequest,
)
