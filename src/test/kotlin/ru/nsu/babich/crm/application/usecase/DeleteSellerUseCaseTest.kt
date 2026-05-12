package ru.nsu.babich.crm.application.usecase

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import ru.nsu.babich.crm.application.usecase.seller.DeleteSellerUseCase
import ru.nsu.babich.crm.domain.exception.SellerNotFoundException
import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.port.repository.SellerRepository
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class DeleteSellerUseCaseTest {
    @MockK
    lateinit var sellerRepository: SellerRepository

    private lateinit var useCase: DeleteSellerUseCase

    private val fixedDateTime = LocalDateTime.of(2026, 1, 1, 0, 0)
    private val seller =
        Seller(
            id = 1,
            name = "John",
            contactInfo = "john@example.com",
            registrationDate = fixedDateTime.minusDays(1),
        )

    @BeforeEach
    fun setUp() {
        mockkStatic(LocalDateTime::class)
        useCase = DeleteSellerUseCase(sellerRepository)
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(LocalDateTime::class)
    }

    @Test
    fun `should soft delete seller`() {
        every { sellerRepository.findActiveById(seller.id!!) } returns seller
        every { LocalDateTime.now() } returns fixedDateTime
        every { sellerRepository.save(any()) } returnsArgument 0

        useCase.execute(seller.id!!)

        verify {
            sellerRepository.save(
                match {
                    it.deletedAt == fixedDateTime
                },
            )
        }
    }

    @Test
    fun `should throw exception when seller not found`() {
        every { sellerRepository.findActiveById(seller.id!!) } returns null

        assertThrows<SellerNotFoundException> {
            useCase.execute(seller.id!!)
        }

        verify(exactly = 0) {
            sellerRepository.save(any())
        }
    }
}
