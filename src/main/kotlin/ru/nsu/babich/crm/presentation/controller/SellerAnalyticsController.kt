package ru.nsu.babich.crm.presentation.controller

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.nsu.babich.crm.application.usecase.analytics.GetSellersWithTurnoverLessThanUseCase
import ru.nsu.babich.crm.application.usecase.analytics.GetTopSellerUseCase
import ru.nsu.babich.crm.presentation.dto.SellerResponse
import ru.nsu.babich.crm.presentation.dto.SellersWithTurnoverLessThanRequest
import ru.nsu.babich.crm.presentation.dto.TopSellerRequest
import ru.nsu.babich.crm.presentation.mapper.toDto
import ru.nsu.babich.crm.presentation.mapper.toPeriod
import ru.nsu.babich.crm.presentation.mapper.toResponse

@RestController
@RequestMapping("/analytics/sellers")
class SellerAnalyticsController(
    private val getTopSellerUseCase: GetTopSellerUseCase,
    private val getSellersWithTurnoverLessThanUseCase: GetSellersWithTurnoverLessThanUseCase,
) {
    @PostMapping("/top")
    fun getTopSeller(
        @Valid
        @RequestBody
        request: TopSellerRequest,
    ): SellerResponse = getTopSellerUseCase(request.period.toPeriod()).toResponse()

    @PostMapping("/turnover-less-than")
    fun getSellersWithTurnoverLessThan(
        @Valid
        @RequestBody
        request: SellersWithTurnoverLessThanRequest,
    ): List<SellerResponse> = getSellersWithTurnoverLessThanUseCase(request.toDto()).map { it.toResponse() }
}
