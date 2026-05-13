package ru.nsu.babich.crm.infrastructure.persistence.repository

import org.springframework.stereotype.Repository
import ru.nsu.babich.crm.domain.model.Transaction
import ru.nsu.babich.crm.domain.port.repository.TransactionRepository
import ru.nsu.babich.crm.infrastructure.persistence.mapper.toDomain
import ru.nsu.babich.crm.infrastructure.persistence.mapper.toEntity

@Repository
class PersistenceTransactionRepository(
    private val jpaTransactionRepository: JpaTransactionRepository,
) : TransactionRepository {
    override fun findAll() = jpaTransactionRepository.findAll().map { it.toDomain() }

    override fun findAllBySellerId(sellerId: Long) =
        jpaTransactionRepository
            .findAllBySellerIdAndSellerDeletedAtIsNull(sellerId)
            .map { it.toDomain() }

    override fun findById(id: Long): Transaction? =
        jpaTransactionRepository
            .findById(id)
            .map { it.toDomain() }
            .orElse(null)

    override fun save(transaction: Transaction) = jpaTransactionRepository.save(transaction.toEntity()).toDomain()
}
