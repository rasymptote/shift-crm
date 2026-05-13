package ru.nsu.babich.crm.presentation.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.nsu.babich.crm.application.usecase.analytics.GetSellersWithTurnoverLessThanUseCase
import ru.nsu.babich.crm.application.usecase.analytics.GetTopSellerUseCase
import ru.nsu.babich.crm.presentation.dto.SellerResponse
import ru.nsu.babich.crm.presentation.dto.SellersWithTurnoverLessThanRequest
import ru.nsu.babich.crm.presentation.dto.TopSellerRequest
import ru.nsu.babich.crm.presentation.mapper.toDto
import ru.nsu.babich.crm.presentation.mapper.toPeriod
import ru.nsu.babich.crm.presentation.mapper.toResponse

@RestController
@Tag(name = "Seller Analytics", description = "Аналитика по продавцам")
@RequestMapping("/analytics/sellers")
class SellerAnalyticsController(
    private val getTopSellerUseCase: GetTopSellerUseCase,
    private val getSellersWithTurnoverLessThanUseCase: GetSellersWithTurnoverLessThanUseCase,
) {
    @PostMapping("/top")
    @Operation(
        summary = "Самый продуктивный продавец",
        description = "Возвращает самого продуктивного продавца за период.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Топ продавец",
                content = [Content(schema = Schema(implementation = SellerResponse::class))],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Ошибка валидации",
                content = [Content(schema = Schema(implementation = ProblemDetail::class))],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Продавцы не найдены",
                content = [Content(schema = Schema(implementation = ProblemDetail::class))],
            ),
        ],
    )
    fun getTopSeller(
        @Valid
        @RequestBody
        request: TopSellerRequest,
    ): SellerResponse = getTopSellerUseCase(request.period.toPeriod()).toResponse()

    @PostMapping("/turnover-less-than")
    @Operation(
        summary = "Продавцы с оборотом ниже порога",
        description = "Возвращает продавцов с суммарным оборотом меньше указанного.",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Список продавцов",
                content = [Content(array = ArraySchema(schema = Schema(implementation = SellerResponse::class)))],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Ошибка валидации",
                content = [Content(schema = Schema(implementation = ProblemDetail::class))],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Продавцы не найдены",
                content = [Content(schema = Schema(implementation = ProblemDetail::class))],
            ),
        ],
    )
    fun getSellersWithTurnoverLessThan(
        @Valid
        @RequestBody
        request: SellersWithTurnoverLessThanRequest,
    ): List<SellerResponse> = getSellersWithTurnoverLessThanUseCase(request.toDto()).map { it.toResponse() }
}
