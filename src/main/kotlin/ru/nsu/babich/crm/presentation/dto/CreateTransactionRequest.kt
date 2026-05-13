package ru.nsu.babich.crm.presentation.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import ru.nsu.babich.crm.domain.model.PaymentType
import java.math.BigDecimal

data class CreateTransactionRequest(
    @field:Positive
    val sellerId: Long,
    @field:Positive
    val amount: BigDecimal,
    @field:NotNull
    val paymentType: PaymentType,
)
