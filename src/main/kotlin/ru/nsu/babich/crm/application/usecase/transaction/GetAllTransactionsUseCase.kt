package ru.nsu.babich.crm.application.usecase.transaction

import ru.nsu.babich.crm.domain.model.Transaction
import ru.nsu.babich.crm.domain.port.repository.TransactionRepository

class GetAllTransactionsUseCase(
    private val transactionRepository: TransactionRepository,
) {
    operator fun invoke(): List<Transaction> = transactionRepository.findAll()
}
