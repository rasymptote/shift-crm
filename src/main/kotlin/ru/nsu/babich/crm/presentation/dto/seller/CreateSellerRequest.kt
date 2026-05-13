package ru.nsu.babich.crm.presentation.dto.seller

import jakarta.validation.constraints.NotBlank

data class CreateSellerRequest(
    @field:NotBlank
    val name: String,
    @field:NotBlank
    val contactInfo: String,
)
