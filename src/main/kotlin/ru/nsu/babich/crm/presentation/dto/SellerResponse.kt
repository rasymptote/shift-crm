package ru.nsu.babich.crm.presentation.dto
import java.time.LocalDateTime

data class SellerResponse(
    val id: Long,
    val name: String,
    val contactInfo: String,
    val registrationDate: LocalDateTime,
)
