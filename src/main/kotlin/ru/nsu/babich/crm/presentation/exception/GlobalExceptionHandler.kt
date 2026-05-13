package ru.nsu.babich.crm.presentation.exception

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.nsu.babich.crm.domain.exception.DomainException
import ru.nsu.babich.crm.domain.exception.InvalidPeriodException
import ru.nsu.babich.crm.domain.exception.InvalidTransactionAmountException
import ru.nsu.babich.crm.domain.exception.NoSellersFound
import ru.nsu.babich.crm.domain.exception.SellerNotFoundException
import ru.nsu.babich.crm.domain.exception.TransactionNotFoundException
import java.net.URI

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(DomainException::class)
    fun handleDomainException(
        e: DomainException,
        request: HttpServletRequest,
    ): ResponseEntity<ProblemDetail> {
        val status =
            when (e) {
                is SellerNotFoundException,
                is TransactionNotFoundException,
                is NoSellersFound,
                -> HttpStatus.NOT_FOUND

                is InvalidTransactionAmountException,
                is InvalidPeriodException,
                -> HttpStatus.BAD_REQUEST
            }

        return problemDetail(status, e.message, request)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(
        e: MethodArgumentNotValidException,
        request: HttpServletRequest,
    ): ResponseEntity<ProblemDetail> {
        val errors =
            e.bindingResult.fieldErrors.associate { error ->
                error.field to (error.defaultMessage ?: "Invalid value")
            }

        val problemDetail =
            buildProblemDetail(
                status = HttpStatus.BAD_REQUEST,
                detail = "Validation failed",
                request = request,
            )
        problemDetail.setProperty("errors", errors)

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(
        e: ConstraintViolationException,
        request: HttpServletRequest,
    ): ResponseEntity<ProblemDetail> {
        val errors =
            e.constraintViolations.associate { violation ->
                violation.propertyPath.toString() to violation.message
            }

        val problemDetail =
            buildProblemDetail(
                status = HttpStatus.BAD_REQUEST,
                detail = "Validation failed",
                request = request,
            )
        problemDetail.setProperty("errors", errors)

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleMalformedBody(request: HttpServletRequest): ResponseEntity<ProblemDetail> =
        problemDetail(
            status = HttpStatus.BAD_REQUEST,
            detail = "Malformed request body",
            request = request,
        )

    @ExceptionHandler(Exception::class)
    fun handleGenericException(
        e: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<ProblemDetail> {
        log.error("Unhandled exception", e)
        return problemDetail(
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            detail = "Unexpected error",
            request = request,
        )
    }

    private fun problemDetail(
        status: HttpStatus,
        detail: String?,
        request: HttpServletRequest,
    ): ResponseEntity<ProblemDetail> =
        ResponseEntity.status(status).body(
            buildProblemDetail(
                status = status,
                detail = detail ?: status.reasonPhrase,
                request = request,
            ),
        )

    private fun buildProblemDetail(
        status: HttpStatus,
        detail: String,
        request: HttpServletRequest,
    ): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(status, detail)
        problemDetail.title = status.reasonPhrase
        problemDetail.instance = URI.create(request.requestURI)
        return problemDetail
    }
}
