package ru.nsu.babich.crm.application.usecase

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.nsu.babich.crm.application.dto.CreateSellerDto
import ru.nsu.babich.crm.application.usecase.seller.CreateSellerUseCase
import ru.nsu.babich.crm.domain.port.TimeProvider
import ru.nsu.babich.crm.domain.port.repository.SellerRepository
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class CreateSellerUseCaseTest {
    @MockK
    lateinit var sellerRepository: SellerRepository

    @MockK
    lateinit var timeProvider: TimeProvider

    private lateinit var useCase: CreateSellerUseCase

    private val fixedDateTime = LocalDateTime.of(2026, 1, 1, 0, 0)
    private val dto =
        CreateSellerDto(
            name = "John",
            contactInfo = "john@example.com",
        )

    @BeforeEach
    fun setUp() {
        useCase = CreateSellerUseCase(sellerRepository, timeProvider)
    }

    @Test
    fun `should create and save seller`() {
        every { timeProvider.now() } returns fixedDateTime
        every { sellerRepository.save(any()) } returnsArgument 0

        val result = useCase.execute(dto)

        assertAll(
            { assertEquals(dto.name, result.name) },
            { assertEquals(dto.contactInfo, result.contactInfo) },
            { assertEquals(fixedDateTime, result.registrationDate) },
        )

        verify(exactly = 1) {
            sellerRepository.save(
                match {
                    it.name == dto.name &&
                        it.contactInfo == dto.contactInfo &&
                        it.registrationDate == fixedDateTime
                },
            )
        }
    }
}
