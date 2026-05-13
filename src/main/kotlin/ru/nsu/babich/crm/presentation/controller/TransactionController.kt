package ru.nsu.babich.crm.presentation.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
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
@RequestMapping("/transactions")
class TransactionController(
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val getTransactionsBySellerUseCase: GetTransactionsBySellerUseCase,
    private val getTransactionUseCase: GetTransactionUseCase,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid
        @RequestBody
        request: CreateTransactionRequest,
    ): TransactionResponse =
        createTransactionUseCase(
            request.toDto(),
        ).toResponse()

    @GetMapping
    fun getAll(): List<TransactionResponse> = getAllTransactionsUseCase().map { it.toResponse() }

    @GetMapping("/seller/{id}")
    fun getBySellerId(
        @PathVariable id: Long,
    ): List<TransactionResponse> = getTransactionsBySellerUseCase(id).map { it.toResponse() }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Long,
    ): TransactionResponse = getTransactionUseCase(id).toResponse()
}
