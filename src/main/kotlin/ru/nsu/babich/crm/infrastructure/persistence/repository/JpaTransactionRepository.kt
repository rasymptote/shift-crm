package ru.nsu.babich.crm.infrastructure.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.nsu.babich.crm.infrastructure.persistence.entity.TransactionEntity

interface JpaTransactionRepository : JpaRepository<TransactionEntity, Long> {
    fun findAllBySellerIdAndSellerDeletedAtIsNull(sellerId: Long): List<TransactionEntity>
}
