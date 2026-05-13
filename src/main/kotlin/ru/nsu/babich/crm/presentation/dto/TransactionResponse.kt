package ru.nsu.babich.crm.presentation.dto

import io.swagger.v3.oas.annotations.media.Schema
import ru.nsu.babich.crm.domain.model.PaymentType
import java.math.BigDecimal
import java.time.LocalDateTime

@Schema(description = "Данные транзакции")
data class TransactionResponse(
    @field:Schema(description = "ID транзакции", example = "1")
    val id: Long,
    @field:Schema(description = "ID продавца", example = "1")
    val sellerId: Long,
    @field:Schema(description = "Сумма транзакции", example = "150.00")
    val amount: BigDecimal,
    @field:Schema(description = "Тип оплаты", example = "CARD")
    val paymentType: PaymentType,
    @field:Schema(description = "Дата транзакции", example = "2025-10-11T10:15:00")
    val transactionDate: LocalDateTime,
)
