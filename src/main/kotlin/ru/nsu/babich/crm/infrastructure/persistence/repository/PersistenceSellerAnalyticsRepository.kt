package ru.nsu.babich.crm.infrastructure.persistence.repository

import org.springframework.stereotype.Repository
import ru.nsu.babich.crm.domain.model.Period
import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.port.repository.SellerAnalyticsRepository
import ru.nsu.babich.crm.infrastructure.persistence.mapper.toDomain
import java.math.BigDecimal

@Repository
class PersistenceSellerAnalyticsRepository(
    private val jpaSellerAnalyticsRepository: JpaSellerAnalyticsRepository,
) : SellerAnalyticsRepository {
    override fun findTopSeller(period: Period): Seller? =
        jpaSellerAnalyticsRepository
            .findTopSellerByPeriod(
                period.from,
                period.to,
            )?.toDomain()

    override fun findSellersWithTurnoverLessThan(
        threshold: BigDecimal,
        period: Period,
    ) = jpaSellerAnalyticsRepository
        .findSellersWithTurnoverLessThan(
            threshold,
            period.from,
            period.to,
        ).map { it.toDomain() }
}
