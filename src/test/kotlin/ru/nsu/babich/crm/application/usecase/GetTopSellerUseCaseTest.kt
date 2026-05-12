package ru.nsu.babich.crm.application.usecase

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.nsu.babich.crm.application.usecase.analytics.GetTopSellerUseCase
import ru.nsu.babich.crm.domain.exception.NoSellersFound
import ru.nsu.babich.crm.domain.model.Period
import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.port.repository.SellerAnalyticsRepository
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class GetTopSellerUseCaseTest {
    @MockK
    lateinit var sellerAnalyticsRepository: SellerAnalyticsRepository

    private lateinit var useCase: GetTopSellerUseCase

    private val period =
        Period(
            from = LocalDateTime.of(2026, 1, 1, 0, 0),
            to = LocalDateTime.of(2026, 1, 31, 23, 59),
        )

    private val seller =
        Seller(
            id = 1L,
            name = "John Doe",
            contactInfo = "john@example.com",
            registrationDate = LocalDateTime.of(2025, 1, 1, 0, 0),
        )

    @Test
    fun `should return top seller when found`() {
        every { sellerAnalyticsRepository.findTopSeller(period) } returns seller

        useCase = GetTopSellerUseCase(sellerAnalyticsRepository)

        val result = useCase(period)

        assertEquals(seller, result)

        verify(exactly = 1) { sellerAnalyticsRepository.findTopSeller(period) }
    }

    @Test
    fun `should throw exception when no seller found`() {
        every { sellerAnalyticsRepository.findTopSeller(period) } returns null

        useCase = GetTopSellerUseCase(sellerAnalyticsRepository)

        val exception =
            assertThrows(NoSellersFound::class.java) {
                useCase(period)
            }

        assertEquals(
            "No sellers found for period Period(from=2026-01-01T00:00, to=2026-01-31T23:59)",
            exception.message,
        )

        verify(exactly = 1) { sellerAnalyticsRepository.findTopSeller(period) }
    }
}
