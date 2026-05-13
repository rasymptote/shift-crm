package ru.nsu.babich.crm.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import ru.nsu.babich.crm.application.dto.CreateSellerDto
import ru.nsu.babich.crm.application.dto.UpdateSellerDto
import ru.nsu.babich.crm.application.usecase.seller.CreateSellerUseCase
import ru.nsu.babich.crm.application.usecase.seller.DeleteSellerUseCase
import ru.nsu.babich.crm.application.usecase.seller.GetAllSellersUseCase
import ru.nsu.babich.crm.application.usecase.seller.GetSellerUseCase
import ru.nsu.babich.crm.application.usecase.seller.UpdateSellerUseCase
import ru.nsu.babich.crm.domain.exception.SellerNotFoundException
import ru.nsu.babich.crm.fixture.SellerFixture
import ru.nsu.babich.crm.presentation.dto.seller.CreateSellerRequest
import ru.nsu.babich.crm.presentation.dto.seller.UpdateSellerRequest
import ru.nsu.babich.crm.presentation.exception.GlobalExceptionHandler
import java.time.LocalDateTime

@WebMvcTest(controllers = [SellerController::class])
@Import(GlobalExceptionHandler::class)
class SellerControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockkBean
    lateinit var createSellerUseCase: CreateSellerUseCase

    @MockkBean
    lateinit var getSellerUseCase: GetSellerUseCase

    @MockkBean
    lateinit var getAllSellersUseCase: GetAllSellersUseCase

    @MockkBean
    lateinit var updateSellerUseCase: UpdateSellerUseCase

    @MockkBean
    lateinit var deleteSellerUseCase: DeleteSellerUseCase

    @Test
    fun `create returns 201 with body`() {
        val seller =
            SellerFixture.seller(
                id = 1L,
                name = "Alice",
                contactInfo = "alice@example.com",
                registrationDate = LocalDateTime.of(2024, 1, 2, 3, 4, 5),
            )

        val dtoSlot = slot<CreateSellerDto>()

        every { createSellerUseCase(capture(dtoSlot)) } returns seller

        val request =
            CreateSellerRequest(
                name = "Alice",
                contactInfo = "alice@example.com",
            )

        mockMvc
            .post("/sellers") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isCreated() }
                jsonPath("$.id") { value(1) }
                jsonPath("$.name") { value("Alice") }
                jsonPath("$.contactInfo") { value("alice@example.com") }
                jsonPath("$.registrationDate") {
                    value("2024-01-02T03:04:05")
                }
            }

        assertThat(dtoSlot.captured.name).isEqualTo("Alice")
        assertThat(dtoSlot.captured.contactInfo)
            .isEqualTo("alice@example.com")

        verify(exactly = 1) {
            createSellerUseCase(any())
        }
    }

    @Test
    fun `create returns 400 with validation errors`() {
        val body =
            """
            {
              "name": "",
              "contactInfo": ""
            }
            """.trimIndent()

        mockMvc
            .post("/sellers") {
                contentType = MediaType.APPLICATION_JSON
                content = body
            }.andExpect {
                status { isBadRequest() }
                jsonPath("$.detail") { value("Validation failed") }
                jsonPath("$.errors.name") {
                    value("must not be blank")
                }
                jsonPath("$.errors.contactInfo") {
                    value("must not be blank")
                }
            }
    }

    @Test
    fun `getById returns 200 with seller`() {
        val seller =
            SellerFixture.seller(
                id = 1L,
                name = "Alice",
                contactInfo = "alice@example.com",
                registrationDate = LocalDateTime.of(2024, 1, 1, 1, 1),
            )

        every { getSellerUseCase(1L) } returns seller

        mockMvc
            .get("/sellers/1")
            .andExpect {
                status { isOk() }
                jsonPath("$.id") { value(1) }
                jsonPath("$.name") { value("Alice") }
                jsonPath("$.contactInfo") {
                    value("alice@example.com")
                }
            }

        verify(exactly = 1) {
            getSellerUseCase(1L)
        }
    }

    @Test
    fun `getById returns 404 when seller not found`() {
        every {
            getSellerUseCase(42L)
        } throws SellerNotFoundException(42L)

        mockMvc
            .get("/sellers/42")
            .andExpect {
                status { isNotFound() }
                jsonPath("$.status") { value(404) }
                jsonPath("$.detail") {
                    value("Seller with id 42 not found")
                }
            }
    }

    @Test
    fun `getAll returns 200 with list`() {
        val sellers =
            listOf(
                SellerFixture.seller(
                    id = 2L,
                    name = "Bob",
                    contactInfo = "bob@example.com",
                    registrationDate =
                        LocalDateTime.of(
                            2024,
                            2,
                            3,
                            4,
                            5,
                            6,
                        ),
                ),
            )

        every { getAllSellersUseCase() } returns sellers

        mockMvc
            .get("/sellers")
            .andExpect {
                status { isOk() }
                jsonPath("$[0].id") { value(2) }
                jsonPath("$[0].name") { value("Bob") }
                jsonPath("$[0].contactInfo") {
                    value("bob@example.com")
                }
                jsonPath("$[0].registrationDate") {
                    value("2024-02-03T04:05:06")
                }
            }

        verify(exactly = 1) {
            getAllSellersUseCase()
        }
    }

    @Test
    fun `update returns 200 with body`() {
        val seller =
            SellerFixture.seller(
                id = 3L,
                name = "Carol",
                contactInfo = "carol@example.com",
                registrationDate =
                    LocalDateTime.of(
                        2024,
                        3,
                        4,
                        5,
                        6,
                        7,
                    ),
            )

        val dtoSlot = slot<UpdateSellerDto>()

        every {
            updateSellerUseCase(capture(dtoSlot))
        } returns seller

        val request =
            UpdateSellerRequest(
                name = "Carol",
                contactInfo = "carol@example.com",
            )

        mockMvc
            .put("/sellers/3") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }.andExpect {
                status { isOk() }
                jsonPath("$.id") { value(3) }
                jsonPath("$.name") { value("Carol") }
                jsonPath("$.contactInfo") {
                    value("carol@example.com")
                }
                jsonPath("$.registrationDate") {
                    value("2024-03-04T05:06:07")
                }
            }

        assertThat(dtoSlot.captured.id).isEqualTo(3L)
        assertThat(dtoSlot.captured.name).isEqualTo("Carol")
        assertThat(dtoSlot.captured.contactInfo)
            .isEqualTo("carol@example.com")

        verify(exactly = 1) {
            updateSellerUseCase(any())
        }
    }

    @Test
    fun `delete returns 204`() {
        justRun { deleteSellerUseCase(4L) }

        mockMvc
            .delete("/sellers/4")
            .andExpect {
                status { isNoContent() }
            }

        verify(exactly = 1) {
            deleteSellerUseCase(4L)
        }
    }
}
