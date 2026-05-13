package ru.nsu.babich.crm.infrastructure.persistence.mapper

import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.model.Transaction
import ru.nsu.babich.crm.infrastructure.persistence.entity.SellerEntity
import ru.nsu.babich.crm.infrastructure.persistence.entity.TransactionEntity

fun SellerEntity.toDomain(): Seller =
    Seller(
        id = id,
        name = name,
        contactInfo = contactInfo,
        registrationDate = registrationDate,
        deletedAt = deletedAt,
    )

fun Seller.toEntity(): SellerEntity =
    SellerEntity(
        id = id ?: 0L,
        name = name,
        contactInfo = contactInfo,
        registrationDate = registrationDate,
        deletedAt = deletedAt,
    )

fun TransactionEntity.toDomain(): Transaction =
    Transaction(
        id = id,
        seller = seller.toDomain(),
        amount = amount,
        paymentType = paymentType,
        transactionDate = transactionDate,
    )

fun Transaction.toEntity(): TransactionEntity =
    TransactionEntity(
        id = id ?: 0L,
        seller = seller.toEntity(),
        amount = amount,
        paymentType = paymentType,
        transactionDate = transactionDate,
    )
