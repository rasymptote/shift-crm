package ru.nsu.babich.crm.domain.model

import java.time.LocalDateTime

data class Seller(
    val id: Long?,
    val name: String,
    val contactInfo: String,
    val registrationDate: LocalDateTime,
    val deletedAt: LocalDateTime? = null,
)
