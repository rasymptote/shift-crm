package ru.nsu.babich.crm.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction(
    val id: Long,
    val seller: Seller,
    val amount: BigDecimal,
    val paymentType: PaymentType,
    val transactionDate: LocalDateTime,
)
