package ru.nsu.babich.crm.application.usecase.transaction

import ru.nsu.babich.crm.domain.model.Transaction
import ru.nsu.babich.crm.domain.port.repository.TransactionRepository

class GetTransactionsBySellerUseCase(
    private val transactionRepository: TransactionRepository,
) {
    operator fun invoke(sellerId: Long): List<Transaction> = transactionRepository.findAllBySellerId(sellerId)
}
