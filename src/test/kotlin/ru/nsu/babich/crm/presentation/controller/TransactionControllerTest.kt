package ru.nsu.babich.crm.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import ru.nsu.babich.crm.application.dto.CreateTransactionDto
import ru.nsu.babich.crm.application.usecase.transaction.CreateTransactionUseCase
import ru.nsu.babich.crm.application.usecase.transaction.GetAllTransactionsUseCase
import ru.nsu.babich.crm.application.usecase.transaction.GetTransactionUseCase
import ru.nsu.babich.crm.application.usecase.transaction.GetTransactionsBySellerUseCase
import ru.nsu.babich.crm.domain.exception.TransactionNotFoundException
import ru.nsu.babich.crm.domain.model.PaymentType
import ru.nsu.babich.crm.fixture.SellerFixture
import ru.nsu.babich.crm.fixture.TransactionFixture
import ru.nsu.babich.crm.presentation.dto.CreateTransactionRequest
import ru.nsu.babich.crm.presentation.exception.GlobalExceptionHandler
import java.time.LocalDateTime

@WebMvcTest(controllers = [TransactionController::class])
@Import(GlobalExceptionHandler::class)
class TransactionControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var createTransactionUseCase: CreateTransactionUseCase

    @MockkBean
    lateinit var getAllTransactionsUseCase: GetAllTransactionsUseCase

    @MockkBean
    lateinit var getTransactionsBySellerUseCase: GetTransactionsBySellerUseCase

    @MockkBean
    lateinit var getTransactionUseCase: GetTransactionUseCase

    @Test
    fun `create returns 201 with body`() {
        val transaction =
            TransactionFixture.transaction(
                id = 10L,
                seller = SellerFixture.seller(id = 3L),
                amount = "150.50",
                paymentType = PaymentType.CARD,
                transactionDate = LocalDateTime.of(2024, 5, 6, 7, 8, 9),
            )

        val dtoSlot = slot<CreateTransactionDto>()

        every { createTransactionUseCase(capture(dtoSlot)) } returns transaction

        val request =
            CreateTransactionRequest(
                sellerId = 3L,
                amount = transaction.amount,
                paymentType = PaymentType.CARD,
            )

        mockMvc
            .post("/transactions") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isCreated() }
                jsonPath("$.id") { value(10) }
                jsonPath("$.sellerId") { value(3) }
                jsonPath("$.amount") { value(150.50) }
                jsonPath("$.paymentType") { value("CARD") }
                jsonPath("$.transactionDate") { value("2024-05-06T07:08:09") }
            }

        assertThat(dtoSlot.captured.sellerId).isEqualTo(3L)
        assertThat(dtoSlot.captured.amount).isEqualTo(transaction.amount)
        assertThat(dtoSlot.captured.paymentType).isEqualTo(PaymentType.CARD)

        verify(exactly = 1) {
            createTransactionUseCase(any())
        }
    }

    @Test
    fun `create returns 400 with validation errors`() {
        val body =
            """
            {
              "sellerId": 1,
              "amount": -1,
              "paymentType": "CARD"
            }
            """.trimIndent()

        mockMvc
            .post("/transactions") {
                contentType = MediaType.APPLICATION_JSON
                content = body
            }.andExpect {
                status { isBadRequest() }
                jsonPath("$.detail") { value("Validation failed") }
                jsonPath("$.errors.amount") { value("must be greater than 0") }
            }
    }

    @Test
    fun `getAll returns 200 with list`() {
        val transactions =
            listOf(
                TransactionFixture.transaction(
                    id = 1L,
                    seller = SellerFixture.seller(id = 5L),
                    amount = "10.00",
                    paymentType = PaymentType.CASH,
                    transactionDate = LocalDateTime.of(2024, 1, 2, 3, 4, 5),
                ),
            )

        every { getAllTransactionsUseCase() } returns transactions

        mockMvc
            .get("/transactions")
            .andExpect {
                status { isOk() }
                jsonPath("$[0].id") { value(1) }
                jsonPath("$[0].sellerId") { value(5) }
                jsonPath("$[0].amount") { value(10.00) }
                jsonPath("$[0].paymentType") { value("CASH") }
                jsonPath("$[0].transactionDate") { value("2024-01-02T03:04:05") }
            }

        verify(exactly = 1) {
            getAllTransactionsUseCase()
        }
    }

    @Test
    fun `getById returns 200 with transaction`() {
        val transaction =
            TransactionFixture.transaction(
                id = 7L,
                seller = SellerFixture.seller(id = 2L),
                amount = "55.00",
                paymentType = PaymentType.TRANSFER,
                transactionDate = LocalDateTime.of(2024, 2, 3, 4, 5, 6),
            )

        every { getTransactionUseCase(7L) } returns transaction

        mockMvc
            .get("/transactions/7")
            .andExpect {
                status { isOk() }
                jsonPath("$.id") { value(7) }
                jsonPath("$.sellerId") { value(2) }
                jsonPath("$.amount") { value(55.00) }
                jsonPath("$.paymentType") { value("TRANSFER") }
                jsonPath("$.transactionDate") { value("2024-02-03T04:05:06") }
            }

        verify(exactly = 1) {
            getTransactionUseCase(7L)
        }
    }

    @Test
    fun `getById returns 404 when transaction not found`() {
        every {
            getTransactionUseCase(99L)
        } throws TransactionNotFoundException(99L)

        mockMvc
            .get("/transactions/99")
            .andExpect {
                status { isNotFound() }
                jsonPath("$.status") { value(404) }
                jsonPath("$.detail") { value("Transaction with id 99 not found") }
            }
    }

    @Test
    fun `getBySellerId returns 200 with list`() {
        val transactions =
            listOf(
                TransactionFixture.transaction(
                    id = 11L,
                    seller = SellerFixture.seller(id = 4L),
                    amount = "75.00",
                    paymentType = PaymentType.CARD,
                    transactionDate = LocalDateTime.of(2024, 3, 4, 5, 6, 7),
                ),
            )

        every { getTransactionsBySellerUseCase(4L) } returns transactions

        mockMvc
            .get("/transactions/seller/4")
            .andExpect {
                status { isOk() }
                jsonPath("$[0].id") { value(11) }
                jsonPath("$[0].sellerId") { value(4) }
                jsonPath("$[0].amount") { value(75.00) }
                jsonPath("$[0].paymentType") { value("CARD") }
                jsonPath("$[0].transactionDate") { value("2024-03-04T05:06:07") }
            }

        verify(exactly = 1) {
            getTransactionsBySellerUseCase(4L)
        }
    }
}

