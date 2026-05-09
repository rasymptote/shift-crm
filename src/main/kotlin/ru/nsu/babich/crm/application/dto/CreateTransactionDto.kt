package ru.nsu.babich.crm.application.dto

import ru.nsu.babich.crm.domain.model.PaymentType
import java.math.BigDecimal

data class CreateTransactionDto(
    val sellerId: Long,
    val amount: BigDecimal,
    val paymentType: PaymentType,
)
