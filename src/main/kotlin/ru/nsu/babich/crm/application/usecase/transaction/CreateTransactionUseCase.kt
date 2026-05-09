package ru.nsu.babich.crm.application.usecase.transaction

import ru.nsu.babich.crm.application.dto.CreateTransactionDto
import ru.nsu.babich.crm.domain.exception.InvalidTransactionAmountException
import ru.nsu.babich.crm.domain.exception.SellerNotFoundException
import ru.nsu.babich.crm.domain.model.Transaction
import ru.nsu.babich.crm.domain.port.TimeProvider
import ru.nsu.babich.crm.domain.port.repository.SellerRepository
import ru.nsu.babich.crm.domain.port.repository.TransactionRepository
import java.math.BigDecimal

class CreateTransactionUseCase(
    private val transactionRepository: TransactionRepository,
    private val sellerRepository: SellerRepository,
    private val timeProvider: TimeProvider,
) {
    fun execute(dto: CreateTransactionDto): Transaction {
        val seller =
            sellerRepository.findActiveById(dto.sellerId)
                ?: throw SellerNotFoundException(dto.sellerId)

        if (dto.amount <= BigDecimal.ZERO) {
            throw InvalidTransactionAmountException()
        }

        val transaction =
            Transaction(
                null,
                seller = seller,
                amount = dto.amount,
                paymentType = dto.paymentType,
                transactionDate = timeProvider.now(),
            )
        return transactionRepository.save(transaction)
    }
}
