package ru.nsu.babich.crm.domain.port.repository

import ru.nsu.babich.crm.domain.model.Period
import ru.nsu.babich.crm.domain.model.Seller
import java.math.BigDecimal

interface SellerAnalyticsRepository {
    fun findTopSeller(period: Period): Seller?

    fun findSellersWithTurnoverLessThan(
        threshold: BigDecimal,
        period: Period,
    ): List<Seller>
}
