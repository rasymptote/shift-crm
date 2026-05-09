package ru.nsu.babich.crm.application.usecase

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.nsu.babich.crm.application.usecase.seller.GetAllSellersUseCase
import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.port.repository.SellerRepository
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class GetAllSellersUseCaseTest {
    @MockK
    lateinit var sellerRepository: SellerRepository

    private lateinit var useCase: GetAllSellersUseCase

    private val fixedDateTime = LocalDateTime.of(2026, 1, 1, 0, 0)
    private val activeSeller =
        Seller(
            id = 1,
            name = "John",
            contactInfo = "john@example.com",
            registrationDate = fixedDateTime,
        )
    private val deletedSeller =
        Seller(
            id = 2,
            name = "Jane",
            contactInfo = "jane@example.com",
            registrationDate = fixedDateTime,
            deletedAt = fixedDateTime,
        )

    @BeforeEach
    fun setUp() {
        useCase = GetAllSellersUseCase(sellerRepository)
    }

    @Test
    fun `should return only active sellers`() {
        every { sellerRepository.findAll() } returns listOf(activeSeller, deletedSeller)

        val result = useCase.execute()

        assertEquals(listOf(activeSeller), result)

        verify(exactly = 1) {
            sellerRepository.findAll()
        }
    }

    @Test
    fun `should return empty list when repository has no sellers`() {
        every { sellerRepository.findAll() } returns emptyList()

        val result = useCase.execute()

        assertEquals(emptyList<Seller>(), result)

        verify(exactly = 1) {
            sellerRepository.findAll()
        }
    }
}
