package ru.nsu.babich.crm.application.usecase.transaction

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import ru.nsu.babich.crm.application.dto.CreateTransactionDto
import ru.nsu.babich.crm.domain.exception.InvalidTransactionAmountException
import ru.nsu.babich.crm.domain.exception.SellerNotFoundException
import ru.nsu.babich.crm.domain.model.PaymentType
import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.port.repository.SellerRepository
import ru.nsu.babich.crm.domain.port.repository.TransactionRepository
import java.math.BigDecimal
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class CreateTransactionUseCaseTest {
    @MockK
    lateinit var transactionRepository: TransactionRepository

    @MockK
    lateinit var sellerRepository: SellerRepository

    private lateinit var useCase: CreateTransactionUseCase

    private val fixedDateTime = LocalDateTime.of(2026, 1, 1, 0, 0)
    private val seller =
        Seller(
            id = 1,
            name = "John",
            contactInfo = "john@example.com",
            registrationDate = fixedDateTime.minusDays(1),
        )
    private val dto =
        CreateTransactionDto(
            sellerId = seller.id!!,
            amount = BigDecimal("100.50"),
            paymentType = PaymentType.CARD,
        )

    @BeforeEach
    fun setUp() {
        mockkStatic(LocalDateTime::class)
        useCase = CreateTransactionUseCase(transactionRepository, sellerRepository)
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(LocalDateTime::class)
    }

    @Test
    fun `should create and save transaction when seller exists`() {
        every { sellerRepository.findActiveById(dto.sellerId) } returns seller
        every { LocalDateTime.now() } returns fixedDateTime
        every { transactionRepository.save(any()) } returnsArgument 0

        val result = useCase(dto)

        assertAll(
            { assertEquals(seller, result.seller) },
            { assertEquals(dto.amount, result.amount) },
            { assertEquals(dto.paymentType, result.paymentType) },
            { assertEquals(fixedDateTime, result.transactionDate) },
        )

        verify(exactly = 1) {
            sellerRepository.findActiveById(dto.sellerId)
        }
        verify(exactly = 1) {
            transactionRepository.save(
                match {
                    it.seller == seller &&
                        it.amount == dto.amount &&
                        it.paymentType == dto.paymentType &&
                        it.transactionDate == fixedDateTime
                },
            )
        }
    }

    @Test
    fun `should throw exception when seller not found`() {
        every { sellerRepository.findActiveById(dto.sellerId) } returns null

        val exception =
            assertThrows<SellerNotFoundException> {
                useCase(dto)
            }

        assertEquals("Seller with id ${dto.sellerId} not found", exception.message)

        verify(exactly = 1) {
            sellerRepository.findActiveById(dto.sellerId)
        }
        verify(exactly = 0) {
            transactionRepository.save(any())
        }
    }

    @Test
    fun `should use current time from TimeProvider for transaction date`() {
        val lazyDateTime = LocalDateTime.of(2026, 5, 9, 14, 30)
        every { sellerRepository.findActiveById(dto.sellerId) } returns seller
        every { LocalDateTime.now() } returns lazyDateTime
        every { transactionRepository.save(any()) } returnsArgument 0

        val result = useCase(dto)

        assertEquals(lazyDateTime, result.transactionDate)

        verify(exactly = 1) {
            LocalDateTime.now()
        }
    }

    @Test
    fun `should handle different payment types`() {
        listOf(PaymentType.CASH, PaymentType.CARD, PaymentType.TRANSFER).forEach { paymentType ->
            val testDto = dto.copy(paymentType = paymentType)
            every { sellerRepository.findActiveById(testDto.sellerId) } returns seller
            every { LocalDateTime.now() } returns fixedDateTime
            every { transactionRepository.save(any()) } returnsArgument 0

            val result = useCase(testDto)

            assertEquals(paymentType, result.paymentType)
        }
    }

    @Test
    fun `should preserve seller id in transaction`() {
        val sellerId = 42L
        val sellerWithDifferentId =
            seller.copy(id = sellerId)
        val dtoWithDifferentId = dto.copy(sellerId = sellerId)

        every { sellerRepository.findActiveById(dtoWithDifferentId.sellerId) } returns sellerWithDifferentId
        every { LocalDateTime.now() } returns fixedDateTime
        every { transactionRepository.save(any()) } returnsArgument 0

        val result = useCase(dtoWithDifferentId)

        assertEquals(sellerId, result.seller.id)
    }

    @Test
    fun `should set transaction id to null initially`() {
        every { sellerRepository.findActiveById(dto.sellerId) } returns seller
        every { LocalDateTime.now() } returns fixedDateTime
        every { transactionRepository.save(any()) } returnsArgument 0

        val result = useCase(dto)

        assertEquals(null, result.id)
    }

    @Test
    fun `should handle large decimal amounts`() {
        val largeAmount = BigDecimal("999999.99")
        val dtoWithLargeAmount = dto.copy(amount = largeAmount)

        every { sellerRepository.findActiveById(dtoWithLargeAmount.sellerId) } returns seller
        every { LocalDateTime.now() } returns fixedDateTime
        every { transactionRepository.save(any()) } returnsArgument 0

        val result = useCase(dtoWithLargeAmount)

        assertEquals(largeAmount, result.amount)
    }

    @Test
    fun `should handle very small decimal amounts`() {
        val smallAmount = BigDecimal("0.01")
        val dtoWithSmallAmount = dto.copy(amount = smallAmount)

        every { sellerRepository.findActiveById(dtoWithSmallAmount.sellerId) } returns seller
        every { LocalDateTime.now() } returns fixedDateTime
        every { transactionRepository.save(any()) } returnsArgument 0

        val result = useCase(dtoWithSmallAmount)

        assertEquals(smallAmount, result.amount)
    }

    @Test
    fun `should throw exception when amount is zero`() {
        val dtoWithZeroAmount = dto.copy(amount = BigDecimal.ZERO)

        every { sellerRepository.findActiveById(dtoWithZeroAmount.sellerId) } returns seller

        assertThrows<InvalidTransactionAmountException> {
            useCase(dtoWithZeroAmount)
        }

        verify(exactly = 0) {
            transactionRepository.save(any())
        }
    }

    @Test
    fun `should throw exception when amount is negative`() {
        val dtoWithNegativeAmount = dto.copy(amount = BigDecimal("-50.00"))

        every { sellerRepository.findActiveById(dtoWithNegativeAmount.sellerId) } returns seller

        assertThrows<InvalidTransactionAmountException> {
            useCase(dtoWithNegativeAmount)
        }

        verify(exactly = 0) {
            transactionRepository.save(any())
        }
    }
}
