package ru.nsu.babich.crm.presentation.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
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
import ru.nsu.babich.crm.presentation.dto.seller.CreateSellerRequest
import ru.nsu.babich.crm.presentation.dto.seller.SellerResponse
import ru.nsu.babich.crm.presentation.dto.seller.UpdateSellerRequest
import ru.nsu.babich.crm.presentation.mapper.toDto
import ru.nsu.babich.crm.presentation.mapper.toResponse

@RestController
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
    fun create(
        @Valid
        @RequestBody
        request: CreateSellerRequest,
    ): SellerResponse =
        createSellerUseCase(
            request.toDto(),
        ).toResponse()

    @GetMapping
    fun getAll(): List<SellerResponse> = getAllSellersUseCase().map { it.toResponse() }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable
        id: Long,
    ): SellerResponse = getSellerUseCase(id).toResponse()

    @PutMapping("/{id}")
    fun update(
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
    fun delete(
        @PathVariable
        id: Long,
    ) {
        deleteSellerUseCase(id)
    }
}
