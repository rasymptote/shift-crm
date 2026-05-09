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
class GetTransactionsBySellerUseCaseTest {
    @MockK
    lateinit var transactionRepository: TransactionRepository

    private lateinit var getTransactionsBySellerUseCase: GetTransactionsBySellerUseCase

    private val referenceDateTime = LocalDateTime.of(2026, 5, 9, 12, 0)
    private val testSellerId = 1L
    private val testSeller =
        Seller(
            id = testSellerId,
            name = "John",
            contactInfo = "john@example.com",
            registrationDate = referenceDateTime.minusDays(1),
        )

    @BeforeEach
    fun setUp() {
        getTransactionsBySellerUseCase = GetTransactionsBySellerUseCase(transactionRepository)
    }

    @Test
    fun `should return transactions for given seller`() {
        val firstTransaction =
            Transaction(
                id = 1,
                seller = testSeller,
                amount = BigDecimal("10.0"),
                paymentType = PaymentType.CASH,
                transactionDate = referenceDateTime,
            )
        val secondTransaction =
            Transaction(
                id = 2,
                seller = testSeller,
                amount = BigDecimal("20.0"),
                paymentType = PaymentType.CARD,
                transactionDate = referenceDateTime,
            )

        every { transactionRepository.findAllBySellerId(testSellerId) } returns
            listOf(firstTransaction, secondTransaction)

        val transactions = getTransactionsBySellerUseCase.execute(testSellerId)

        assertEquals(listOf(firstTransaction, secondTransaction), transactions)
        verify(exactly = 1) { transactionRepository.findAllBySellerId(testSellerId) }
    }

    @Test
    fun `should return empty list when seller has no transactions`() {
        every { transactionRepository.findAllBySellerId(2L) } returns emptyList()

        val transactions = getTransactionsBySellerUseCase.execute(2L)

        assertEquals(emptyList<Transaction>(), transactions)
        verify(exactly = 1) { transactionRepository.findAllBySellerId(2L) }
    }
}
