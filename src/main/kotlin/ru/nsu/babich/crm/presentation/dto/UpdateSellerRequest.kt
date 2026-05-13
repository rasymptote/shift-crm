package ru.nsu.babich.crm.presentation.dto
import jakarta.validation.constraints.NotBlank

data class UpdateSellerRequest(
    @field:NotBlank
    val name: String,
    @field:NotBlank
    val contactInfo: String,
)
