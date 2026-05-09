package ru.nsu.babich.crm.domain.exception

class SellerNotFoundException(
    sellerId: Long,
) : DomainException(
        "Seller with id $sellerId not found",
    )
