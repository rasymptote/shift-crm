package ru.nsu.babich.crm.presentation.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Данные продавца")
data class SellerResponse(
    @field:Schema(description = "ID продавца", example = "1")
    val id: Long,
    @field:Schema(description = "Имя продавца", example = "Ivan")
    val name: String,
    @field:Schema(description = "Контактная информация", example = "+7(999)000-00-00")
    val contactInfo: String,
    @field:Schema(description = "Дата регистрации", example = "2025-10-11T06:20:00")
    val registrationDate: LocalDateTime,
)
