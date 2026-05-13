package ru.nsu.babich.crm.presentation.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

@Schema(description = "Запрос для получения продавцов с оборотом ниже порога")
data class SellersWithTurnoverLessThanRequest(
    @field:Positive
    @field:Schema(description = "Порог оборота", example = "10000.00")
    val threshold: BigDecimal,
    @field:Valid
    @field:NotNull
    @field:Schema(description = "Период выборки")
    val period: PeriodRequest,
)
