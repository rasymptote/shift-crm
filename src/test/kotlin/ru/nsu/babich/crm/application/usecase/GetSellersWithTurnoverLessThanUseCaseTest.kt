package ru.nsu.babich.crm.application.usecase

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.nsu.babich.crm.application.dto.GetSellersWithTurnoverLessThanDto
import ru.nsu.babich.crm.application.usecase.analytics.GetSellersWithTurnoverLessThanUseCase
import ru.nsu.babich.crm.domain.model.Period
import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.port.repository.SellerAnalyticsRepository
import java.math.BigDecimal
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class GetSellersWithTurnoverLessThanUseCaseTest {
    @MockK
    lateinit var sellerAnalyticsRepository: SellerAnalyticsRepository

    private lateinit var useCase: GetSellersWithTurnoverLessThanUseCase

    private val period =
        Period(
            from = LocalDateTime.of(2026, 1, 1, 0, 0),
            to = LocalDateTime.of(2026, 1, 31, 23, 59),
        )

    private val threshold = BigDecimal("1000.00")

    private val dto =
        GetSellersWithTurnoverLessThanDto(
            threshold = threshold,
            period = period,
        )

    private val sellers =
        listOf(
            Seller(
                id = 1L,
                name = "John Doe",
                contactInfo = "john@example.com",
                registrationDate = LocalDateTime.of(2025, 1, 1, 0, 0),
            ),
            Seller(
                id = 2L,
                name = "Jane Smith",
                contactInfo = "jane@example.com",
                registrationDate = LocalDateTime.of(2025, 2, 1, 0, 0),
            ),
        )

    @Test
    fun `should return list of sellers with turnover less than threshold`() {
        every { sellerAnalyticsRepository.findSellersWithTurnoverLessThan(threshold, period) } returns sellers

        useCase = GetSellersWithTurnoverLessThanUseCase(sellerAnalyticsRepository)

        val result = useCase(dto)

        assertEquals(sellers, result)

        verify(exactly = 1) { sellerAnalyticsRepository.findSellersWithTurnoverLessThan(threshold, period) }
    }

    @Test
    fun `should return empty list when no sellers found`() {
        every { sellerAnalyticsRepository.findSellersWithTurnoverLessThan(threshold, period) } returns emptyList()

        useCase = GetSellersWithTurnoverLessThanUseCase(sellerAnalyticsRepository)

        val result = useCase(dto)

        assertEquals(emptyList<Seller>(), result)

        verify(exactly = 1) { sellerAnalyticsRepository.findSellersWithTurnoverLessThan(threshold, period) }
    }
}
