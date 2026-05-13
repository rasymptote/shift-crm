package ru.nsu.babich.crm.presentation.dto

import ru.nsu.babich.crm.domain.model.PaymentType
import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionResponse(
    val id: Long,
    val sellerId: Long,
    val amount: BigDecimal,
    val paymentType: PaymentType,
    val transactionDate: LocalDateTime,
)
