package ru.nsu.babich.crm.presentation.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "Данные для создания продавца")
data class CreateSellerRequest(
    @field:NotBlank
    @field:Schema(description = "Имя продавца", example = "Ivan")
    val name: String,
    @field:NotBlank
    @field:Schema(description = "Контактная информация", example = "+7(999)000-00-00")
    val contactInfo: String,
)
