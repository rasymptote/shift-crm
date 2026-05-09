package ru.nsu.babich.crm.domain.port.repository

import ru.nsu.babich.crm.domain.model.Transaction

interface TransactionRepository {
    fun findAll(): List<Transaction>

    fun findAllBySellerId(sellerId: Long): List<Transaction>

    fun findById(id: Long): Transaction?

    fun save(transaction: Transaction): Transaction
}
