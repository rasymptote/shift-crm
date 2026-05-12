package ru.nsu.babich.crm.application.usecase.analytics

import ru.nsu.babich.crm.domain.exception.NoSellersFound
import ru.nsu.babich.crm.domain.model.Period
import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.port.repository.SellerAnalyticsRepository

class GetTopSellerUseCase(
    private val analyticsRepository: SellerAnalyticsRepository,
) {
    operator fun invoke(period: Period): Seller =
        analyticsRepository.findTopSeller(period)
            ?: throw NoSellersFound(period)
}
