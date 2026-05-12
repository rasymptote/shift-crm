package ru.nsu.babich.crm.application.usecase.transaction

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import ru.nsu.babich.crm.domain.exception.TransactionNotFoundException
import ru.nsu.babich.crm.domain.model.PaymentType
import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.model.Transaction
import ru.nsu.babich.crm.domain.port.repository.TransactionRepository
import java.math.BigDecimal
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class GetTransactionUseCaseTest {
    @MockK
    lateinit var transactionRepository: TransactionRepository

    private lateinit var useCase: GetTransactionUseCase

    private val fixedDateTime = LocalDateTime.of(2026, 5, 9, 12, 0)
    private val seller =
        Seller(
            id = 1,
            name = "John",
            contactInfo = "john@example.com",
            registrationDate = fixedDateTime.minusDays(1),
        )
    private val transaction =
        Transaction(
            id = 10,
            seller = seller,
            amount = BigDecimal("250.75"),
            paymentType = PaymentType.CARD,
            transactionDate = fixedDateTime,
        )

    @BeforeEach
    fun setUp() {
        useCase = GetTransactionUseCase(transactionRepository)
    }

    @Test
    fun `should return transaction when found`() {
        every { transactionRepository.findById(transaction.id!!) } returns transaction

        val result = useCase(transaction.id!!)

        assertEquals(transaction, result)
        verify(exactly = 1) {
            transactionRepository.findById(transaction.id)
        }
    }

    @Test
    fun `should throw exception when transaction not found`() {
        val missingId = 999L
        every { transactionRepository.findById(missingId) } returns null

        val exception =
            assertThrows<TransactionNotFoundException> {
                useCase(missingId)
            }

        assertEquals("Transaction with id $missingId not found", exception.message)
        verify(exactly = 1) {
            transactionRepository.findById(missingId)
        }
    }
}
