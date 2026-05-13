package ru.nsu.babich.crm.presentation.mapper

import ru.nsu.babich.crm.application.dto.CreateSellerDto
import ru.nsu.babich.crm.application.dto.UpdateSellerDto
import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.presentation.dto.seller.CreateSellerRequest
import ru.nsu.babich.crm.presentation.dto.seller.SellerResponse
import ru.nsu.babich.crm.presentation.dto.seller.UpdateSellerRequest

fun CreateSellerRequest.toDto() =
    CreateSellerDto(
        name = name,
        contactInfo = contactInfo,
    )

fun UpdateSellerRequest.toDto(id: Long) =
    UpdateSellerDto(
        id = id,
        name = name,
        contactInfo = contactInfo,
    )

fun Seller.toResponse(): SellerResponse =
    SellerResponse(
        id = requireNotNull(id),
        name = name,
        contactInfo = contactInfo,
        registrationDate = registrationDate,
    )
