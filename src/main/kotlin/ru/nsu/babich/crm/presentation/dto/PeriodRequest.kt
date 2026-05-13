package ru.nsu.babich.crm.presentation.dto

import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class PeriodRequest(
    @field:NotNull
    val from: LocalDateTime,
    @field:NotNull
    val to: LocalDateTime,
)
