package ru.nsu.babich.crm.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.nsu.babich.crm.application.usecase.analytics.GetSellersWithTurnoverLessThanUseCase
import ru.nsu.babich.crm.application.usecase.analytics.GetTopSellerUseCase
import ru.nsu.babich.crm.domain.exception.NoSellersFound
import ru.nsu.babich.crm.domain.model.Period
import ru.nsu.babich.crm.fixture.SellerFixture
import ru.nsu.babich.crm.presentation.dto.PeriodRequest
import ru.nsu.babich.crm.presentation.dto.SellersWithTurnoverLessThanRequest
import ru.nsu.babich.crm.presentation.dto.TopSellerRequest
import ru.nsu.babich.crm.presentation.exception.GlobalExceptionHandler
import java.math.BigDecimal
import java.time.LocalDateTime

@WebMvcTest(controllers = [SellerAnalyticsController::class])
@Import(GlobalExceptionHandler::class)
class SellerAnalyticsControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var getTopSellerUseCase: GetTopSellerUseCase

    @MockkBean
    lateinit var getSellersWithTurnoverLessThanUseCase: GetSellersWithTurnoverLessThanUseCase

    @Test
    fun `getTopSeller returns 200 with seller`() {
        val seller =
            SellerFixture.seller(
                id = 1L,
                name = "Alice",
                contactInfo = "alice@example.com",
                registrationDate = LocalDateTime.of(2024, 1, 1, 0, 0),
            )

        every { getTopSellerUseCase(any()) } returns seller

        val request =
            TopSellerRequest(
                period =
                    PeriodRequest(
                        from = LocalDateTime.of(2024, 1, 1, 0, 0),
                        to = LocalDateTime.of(2024, 1, 31, 23, 59, 59),
                    ),
            )

        mockMvc
            .post("/analytics/sellers/top") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isOk() }
                jsonPath("$.id") { value(1) }
                jsonPath("$.name") { value("Alice") }
                jsonPath("$.contactInfo") { value("alice@example.com") }
                jsonPath("$.registrationDate") { value("2024-01-01T00:00:00") }
            }

        verify(exactly = 1) {
            getTopSellerUseCase(any())
        }
    }

    @Test
    fun `getTopSeller returns 400 for invalid period`() {
        val body =
            """
            {
              "period": {
                "from": "2024-02-01T00:00:00",
                "to": "2024-01-01T00:00:00"
              }
            }
            """.trimIndent()

        mockMvc
            .post("/analytics/sellers/top") {
                contentType = MediaType.APPLICATION_JSON
                content = body
            }.andExpect {
                status { isBadRequest() }
                jsonPath("$.detail") { value("Start date must be before end date") }
            }
    }

    @Test
    fun `getTopSeller returns 404 when none found`() {
        val period =
            Period(
                from = LocalDateTime.of(2024, 1, 1, 0, 0),
                to = LocalDateTime.of(2024, 1, 31, 23, 59, 59),
            )

        every { getTopSellerUseCase(period) } throws NoSellersFound(period)

        val request =
            TopSellerRequest(
                period =
                    PeriodRequest(
                        from = LocalDateTime.of(2024, 1, 1, 0, 0),
                        to = LocalDateTime.of(2024, 1, 31, 23, 59, 59),
                    ),
            )

        mockMvc
            .post("/analytics/sellers/top") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isNotFound() }
                jsonPath("$.detail") { value("No sellers found for period $period") }
            }
    }

    @Test
    fun `getSellersWithTurnoverLessThan returns 200 with list`() {
        val sellers =
            listOf(
                SellerFixture.seller(
                    id = 2L,
                    name = "Bob",
                    contactInfo = "bob@example.com",
                    registrationDate = LocalDateTime.of(2024, 2, 1, 0, 0),
                ),
            )

        every { getSellersWithTurnoverLessThanUseCase(any()) } returns sellers

        val request =
            SellersWithTurnoverLessThanRequest(
                threshold = BigDecimal("100.00"),
                period =
                    PeriodRequest(
                        from = LocalDateTime.of(2024, 2, 1, 0, 0),
                        to = LocalDateTime.of(2024, 2, 28, 23, 59, 59),
                    ),
            )

        mockMvc
            .post("/analytics/sellers/turnover-less-than") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isOk() }
                jsonPath("$[0].id") { value(2) }
                jsonPath("$[0].name") { value("Bob") }
                jsonPath("$[0].contactInfo") { value("bob@example.com") }
                jsonPath("$[0].registrationDate") { value("2024-02-01T00:00:00") }
            }

        verify(exactly = 1) {
            getSellersWithTurnoverLessThanUseCase(any())
        }
    }

    @Test
    fun `getSellersWithTurnoverLessThan returns 400 with validation errors`() {
        val body =
            """
            {
              "threshold": -1,
              "period": {
                "from": "2024-02-01T00:00:00",
                "to": "2024-02-28T23:59:59"
              }
            }
            """.trimIndent()

        mockMvc
            .post("/analytics/sellers/turnover-less-than") {
                contentType = MediaType.APPLICATION_JSON
                content = body
            }.andExpect {
                status { isBadRequest() }
                jsonPath("$.detail") { value("Validation failed") }
                jsonPath("$.errors.threshold") { value("must be greater than 0") }
            }
    }
}
