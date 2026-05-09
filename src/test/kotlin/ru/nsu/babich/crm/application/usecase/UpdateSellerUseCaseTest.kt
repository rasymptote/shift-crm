package ru.nsu.babich.crm.application.usecase

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import ru.nsu.babich.crm.application.dto.UpdateSellerDto
import ru.nsu.babich.crm.domain.exception.SellerNotFoundException
import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.port.repository.SellerRepository
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class UpdateSellerUseCaseTest {
    @MockK
    lateinit var sellerRepository: SellerRepository

    private lateinit var useCase: UpdateSellerUseCase

    private val fixedDateTime = LocalDateTime.of(2026, 1, 1, 0, 0)
    private val existingSeller =
        Seller(
            id = 1,
            name = "John",
            contactInfo = "john@example.com",
            registrationDate = fixedDateTime.minusDays(1),
        )

    @BeforeEach
    fun setUp() {
        useCase = UpdateSellerUseCase(sellerRepository)
    }

    @Test
    fun `should update and save seller`() {
        val dto =
            UpdateSellerDto(
                id = existingSeller.id!!,
                name = "Johnny",
                contactInfo = "johnny@example.com",
            )

        every { sellerRepository.findActiveById(dto.id) } returns existingSeller
        every { sellerRepository.save(any()) } returnsArgument 0

        val result = useCase.execute(dto)

        assertEquals(dto.name, result.name)
        assertEquals(dto.contactInfo, result.contactInfo)
        assertEquals(existingSeller.id, result.id)

        verify(exactly = 1) {
            sellerRepository.save(
                match {
                    it.id == existingSeller.id && it.name == dto.name && it.contactInfo == dto.contactInfo
                },
            )
        }
    }

    @Test
    fun `should throw when seller not found`() {
        val dto =
            UpdateSellerDto(
                id = 2,
                name = "NoOne",
                contactInfo = "noone@example.com",
            )

        every { sellerRepository.findActiveById(dto.id) } returns null

        assertThrows<SellerNotFoundException> {
            useCase.execute(dto)
        }

        verify(exactly = 0) {
            sellerRepository.save(any())
        }
    }
}
