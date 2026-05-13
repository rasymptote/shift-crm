package ru.nsu.babich.crm.presentation.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

@Schema(description = "Период времени")
data class PeriodRequest(
    @field:NotNull
    @field:Schema(description = "Начало периода", example = "2024-01-01T00:00:00")
    val from: LocalDateTime,
    @field:NotNull
    @field:Schema(description = "Конец периода", example = "2024-01-31T23:59:59")
    val to: LocalDateTime,
)
