package ru.nsu.babich.crm.application.dto

data class UpdateSellerDto(
    val id: Long,
    val name: String,
    val contactInfo: String,
)
