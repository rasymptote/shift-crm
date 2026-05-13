package ru.nsu.babich.crm.presentation.mapper

import ru.nsu.babich.crm.application.dto.CreateTransactionDto
import ru.nsu.babich.crm.domain.model.Transaction
import ru.nsu.babich.crm.presentation.dto.CreateTransactionRequest
import ru.nsu.babich.crm.presentation.dto.TransactionResponse

fun CreateTransactionRequest.toDto() =
    CreateTransactionDto(
        sellerId = sellerId,
        amount = amount,
        paymentType = paymentType,
    )

fun Transaction.toResponse(): TransactionResponse =
    TransactionResponse(
        id = requireNotNull(id),
        sellerId = seller.id!!,
        amount = amount,
        paymentType = paymentType,
        transactionDate = transactionDate,
    )
