package ru.nsu.babich.crm.presentation.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import ru.nsu.babich.crm.domain.model.PaymentType
import java.math.BigDecimal

@Schema(description = "Данные для создания транзакции")
data class CreateTransactionRequest(
    @field:Positive
    @field:Schema(description = "ID продавца", example = "1")
    val sellerId: Long,
    @field:Positive
    @field:Schema(description = "Сумма транзакции", example = "150.00")
    val amount: BigDecimal,
    @field:NotNull
    @field:Schema(description = "Тип оплаты", example = "CARD")
    val paymentType: PaymentType,
)
