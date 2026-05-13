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
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.nsu.babich.crm.application.usecase.seller.CreateSellerUseCase
import ru.nsu.babich.crm.application.usecase.seller.DeleteSellerUseCase
import ru.nsu.babich.crm.application.usecase.seller.GetAllSellersUseCase
import ru.nsu.babich.crm.application.usecase.seller.GetSellerUseCase
import ru.nsu.babich.crm.application.usecase.seller.UpdateSellerUseCase
import ru.nsu.babich.crm.presentation.dto.CreateSellerRequest
import ru.nsu.babich.crm.presentation.dto.SellerResponse
import ru.nsu.babich.crm.presentation.dto.UpdateSellerRequest
import ru.nsu.babich.crm.presentation.mapper.toDto
import ru.nsu.babich.crm.presentation.mapper.toResponse

@RestController
@Tag(name = "Sellers", description = "Управление продавцами")
@RequestMapping("/sellers")
class SellerController(
    private val createSellerUseCase: CreateSellerUseCase,
    private val updateSellerUseCase: UpdateSellerUseCase,
    private val deleteSellerUseCase: DeleteSellerUseCase,
    private val getAllSellersUseCase: GetAllSellersUseCase,
    private val getSellerUseCase: GetSellerUseCase,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать продавца", description = "Создает нового продавца.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Продавец создан",
                content = [Content(schema = Schema(implementation = SellerResponse::class))],
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
        request: CreateSellerRequest,
    ): SellerResponse =
        createSellerUseCase(
            request.toDto(),
        ).toResponse()

    @GetMapping
    @Operation(summary = "Получить всех продавцов", description = "Возвращает список всех продавцов.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Список продавцов",
                content = [Content(array = ArraySchema(schema = Schema(implementation = SellerResponse::class)))],
            ),
        ],
    )
    fun getAll(): List<SellerResponse> = getAllSellersUseCase().map { it.toResponse() }

    @GetMapping("/{id}")
    @Operation(summary = "Получить продавца", description = "Возвращает продавца по идентификатору.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Продавец найден",
                content = [Content(schema = Schema(implementation = SellerResponse::class))],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Продавец не найден",
                content = [Content(schema = Schema(implementation = ProblemDetail::class))],
            ),
        ],
    )
    fun getById(
        @Parameter(description = "ID продавца", example = "1")
        @PathVariable
        id: Long,
    ): SellerResponse = getSellerUseCase(id).toResponse()

    @PutMapping("/{id}")
    @Operation(summary = "Обновить продавца", description = "Обновляет данные продавца по идентификатору.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Продавец обновлен",
                content = [Content(schema = Schema(implementation = SellerResponse::class))],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Ошибка валидации",
                content = [Content(schema = Schema(implementation = ProblemDetail::class))],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Продавец не найден",
                content = [Content(schema = Schema(implementation = ProblemDetail::class))],
            ),
        ],
    )
    fun update(
        @Parameter(description = "ID продавца", example = "1")
        @PathVariable
        id: Long,
        @Valid
        @RequestBody
        request: UpdateSellerRequest,
    ): SellerResponse =
        updateSellerUseCase(
            request.toDto(id),
        ).toResponse()

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить продавца", description = "Удаляет продавца по идентификатору.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "Продавец удален",
            ),
            ApiResponse(
                responseCode = "404",
                description = "Продавец не найден",
                content = [Content(schema = Schema(implementation = ProblemDetail::class))],
            ),
        ],
    )
    fun delete(
        @Parameter(description = "ID продавца", example = "1")
        @PathVariable
        id: Long,
    ) {
        deleteSellerUseCase(id)
    }
}
