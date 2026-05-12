package ru.nsu.babich.crm.application.usecase.transaction

import ru.nsu.babich.crm.domain.exception.TransactionNotFoundException
import ru.nsu.babich.crm.domain.model.Transaction
import ru.nsu.babich.crm.domain.port.repository.TransactionRepository

class GetTransactionUseCase(
    private val transactionRepository: TransactionRepository,
) {
    operator fun invoke(transactionId: Long): Transaction =
        transactionRepository.findById(transactionId)
            ?: throw TransactionNotFoundException(transactionId)
}
