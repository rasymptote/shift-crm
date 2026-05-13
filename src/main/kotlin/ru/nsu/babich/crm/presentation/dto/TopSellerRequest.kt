package ru.nsu.babich.crm.presentation.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

data class TopSellerRequest(
    @field:Valid
    @field:NotNull
    val period: PeriodRequest,
)
