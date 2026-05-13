package ru.nsu.babich.crm.presentation.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.nsu.babich.crm.application.usecase.transaction.CreateTransactionUseCase
import ru.nsu.babich.crm.application.usecase.transaction.GetAllTransactionsUseCase
import ru.nsu.babich.crm.application.usecase.transaction.GetTransactionUseCase
import ru.nsu.babich.crm.application.usecase.transaction.GetTransactionsBySellerUseCase
import ru.nsu.babich.crm.presentation.dto.CreateTransactionRequest
import ru.nsu.babich.crm.presentation.dto.TransactionResponse
import ru.nsu.babich.crm.presentation.mapper.toDto
import ru.nsu.babich.crm.presentation.mapper.toResponse

@RestController
@Tag(name = "Transactions", description = "Операции с транзакциями")
@RequestMapping("/transactions")
class TransactionController(
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val getTransactionsBySellerUseCase: GetTransactionsBySellerUseCase,
    private val getTransactionUseCase: GetTransactionUseCase,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать транзакцию", description = "Создает новую транзакцию для продавца.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Транзакция создана",
                content = [Content(schema = Schema(implementation = TransactionResponse::class))],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Ошибка валидации",
                content = [Content(schema = Schema(implementation = ProblemDetail::class))],
            ),
        ],
    )
    fun create(
        @Valid
        @RequestBody
        request: CreateTransactionRequest,
    ): TransactionResponse =
        createTransactionUseCase(
            request.toDto(),
        ).toResponse()

    @GetMapping
    @Operation(summary = "Получить все транзакции", description = "Возвращает список всех транзакций.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Список транзакций",
                content = [Content(array = ArraySchema(schema = Schema(implementation = TransactionResponse::class)))],
            ),
        ],
    )
    fun getAll(): List<TransactionResponse> = getAllTransactionsUseCase().map { it.toResponse() }

    @GetMapping("/seller/{id}")
    @Operation(
        summary = "Получить транзакции продавца",
        description = "Возвращает список транзакций по идентификатору продавца.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Список транзакций продавца",
                content = [Content(array = ArraySchema(schema = Schema(implementation = TransactionResponse::class)))],
            ),
        ],
    )
    fun getBySellerId(
        @Parameter(description = "ID продавца", example = "1")
        @PathVariable id: Long,
    ): List<TransactionResponse> = getTransactionsBySellerUseCase(id).map { it.toResponse() }

    @GetMapping("/{id}")
    @Operation(summary = "Получить транзакцию", description = "Возвращает транзакцию по идентификатору.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Транзакция найдена",
                content = [Content(schema = Schema(implementation = TransactionResponse::class))],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Транзакция не найдена",
                content = [Content(schema = Schema(implementation = ProblemDetail::class))],
            ),
        ],
    )
    fun getById(
        @Parameter(description = "ID транзакции", example = "1")
        @PathVariable id: Long,
    ): TransactionResponse = getTransactionUseCase(id).toResponse()
}
