package ru.nsu.babich.crm.application.usecase

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import ru.nsu.babich.crm.domain.exception.SellerNotFoundException
import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.port.repository.SellerRepository
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class GetSellerUseCaseTest {
    @MockK
    lateinit var sellerRepository: SellerRepository

    private lateinit var useCase: GetSellerUseCase

    private val fixedDateTime = LocalDateTime.of(2026, 1, 1, 0, 0)
    private val seller =
        Seller(
            id = 1,
            name = "John",
            contactInfo = "john@example.com",
            registrationDate = fixedDateTime,
        )

    @BeforeEach
    fun setUp() {
        useCase = GetSellerUseCase(sellerRepository)
    }

    @Test
    fun `should return seller when found`() {
        every { sellerRepository.findActiveById(seller.id!!) } returns seller

        val result = useCase.execute(seller.id!!)

        assertEquals(seller, result)
    }

    @Test
    fun `should throw exception when not found`() {
        every { sellerRepository.findActiveById(2) } returns null

        assertThrows<SellerNotFoundException> {
            useCase.execute(2)
        }
    }
}
