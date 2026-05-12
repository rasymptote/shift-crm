package ru.nsu.babich.crm.application.usecase.analytics

import ru.nsu.babich.crm.application.dto.GetSellersWithTurnoverLessThanDto
import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.port.repository.SellerAnalyticsRepository

class GetSellersWithTurnoverLessThanUseCase(
    private val analyticsRepository: SellerAnalyticsRepository,
) {
    operator fun invoke(dto: GetSellersWithTurnoverLessThanDto): List<Seller> =
        analyticsRepository.findSellersWithTurnoverLessThan(dto.threshold, dto.period)
}
