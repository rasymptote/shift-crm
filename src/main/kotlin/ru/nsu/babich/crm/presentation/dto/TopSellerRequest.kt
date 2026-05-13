package ru.nsu.babich.crm.presentation.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

@Schema(description = "Запрос для получения самого продуктивного продавца за период")
data class TopSellerRequest(
    @field:Valid
    @field:NotNull
    @field:Schema(description = "Период выборки")
    val period: PeriodRequest,
)
