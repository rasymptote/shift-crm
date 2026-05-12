package ru.nsu.babich.crm.application.usecase.transaction

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.nsu.babich.crm.domain.model.PaymentType
import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.model.Transaction
import ru.nsu.babich.crm.domain.port.repository.TransactionRepository
import java.math.BigDecimal
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class GetAllTransactionsUseCaseTest {
    @MockK
    lateinit var transactionRepository: TransactionRepository

    private lateinit var getAllTransactionsUseCase: GetAllTransactionsUseCase

    private val referenceDateTime = LocalDateTime.of(2026, 5, 9, 12, 0)
    private val sampleSeller =
        Seller(
            id = 1,
            name = "John",
            contactInfo = "john@example.com",
            registrationDate = referenceDateTime.minusDays(1),
        )

    @BeforeEach
    fun setUp() {
        getAllTransactionsUseCase = GetAllTransactionsUseCase(transactionRepository)
    }

    @Test
    fun `should return all transactions from repository`() {
        val firstTransaction =
            Transaction(
                id = 1,
                seller = sampleSeller,
                amount = BigDecimal("10.0"),
                paymentType = PaymentType.CASH,
                transactionDate = referenceDateTime,
            )
        val secondTransaction =
            Transaction(
                id = 2,
                seller = sampleSeller,
                amount = BigDecimal("20.0"),
                paymentType = PaymentType.CARD,
                transactionDate = referenceDateTime,
            )

        every { transactionRepository.findAll() } returns listOf(firstTransaction, secondTransaction)

        val retrievedTransactions = getAllTransactionsUseCase()

        assertEquals(listOf(firstTransaction, secondTransaction), retrievedTransactions)
        verify(exactly = 1) { transactionRepository.findAll() }
    }

    @Test
    fun `should return empty list when repository has no transactions`() {
        every { transactionRepository.findAll() } returns emptyList()

        val retrievedTransactions = getAllTransactionsUseCase()

        assertEquals(emptyList<Transaction>(), retrievedTransactions)
        verify(exactly = 1) { transactionRepository.findAll() }
    }
}
