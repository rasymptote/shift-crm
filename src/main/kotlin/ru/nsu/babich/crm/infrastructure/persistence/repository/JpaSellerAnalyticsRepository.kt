package ru.nsu.babich.crm.infrastructure.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ru.nsu.babich.crm.infrastructure.persistence.entity.SellerEntity
import java.math.BigDecimal
import java.time.LocalDateTime

interface JpaSellerAnalyticsRepository : JpaRepository<SellerEntity, Long> {
    @Query(
        """
        SELECT s FROM SellerEntity s
        WHERE s.deletedAt IS NULL
        AND s.id = (
            SELECT t.seller.id
            FROM TransactionEntity t
            WHERE t.transactionDate BETWEEN :fromDate AND :toDate
            GROUP BY t.seller.id
            ORDER BY SUM(t.amount) DESC
            LIMIT 1
        )
        """,
    )
    fun findTopSellerByPeriod(
        @Param("fromDate") fromDate: LocalDateTime,
        @Param("toDate") toDate: LocalDateTime,
    ): SellerEntity?

    @Query(
        """
        SELECT s FROM SellerEntity s
        WHERE s.deletedAt IS NULL
        AND s.id IN (
            SELECT t.seller.id
            FROM TransactionEntity t
            WHERE t.transactionDate BETWEEN :fromDate AND :toDate
            GROUP BY t.seller.id
            HAVING SUM(t.amount) < :threshold
        )
        """,
    )
    fun findSellersWithTurnoverLessThan(
        @Param("threshold") threshold: BigDecimal,
        @Param("fromDate") fromDate: LocalDateTime,
        @Param("toDate") toDate: LocalDateTime,
    ): List<SellerEntity>
}
