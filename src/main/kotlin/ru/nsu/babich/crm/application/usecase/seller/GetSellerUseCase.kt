package ru.nsu.babich.crm.application.usecase.seller

import ru.nsu.babich.crm.domain.exception.SellerNotFoundException
import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.port.repository.SellerRepository

class GetSellerUseCase(
    private val sellerRepository: SellerRepository,
) {
    fun execute(sellerId: Long): Seller =
        sellerRepository.findActiveById(sellerId)
            ?: throw SellerNotFoundException(sellerId)
}
