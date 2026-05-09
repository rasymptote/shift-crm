package ru.nsu.babich.crm.application.usecase.seller

import ru.nsu.babich.crm.domain.exception.SellerNotFoundException
import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.port.TimeProvider
import ru.nsu.babich.crm.domain.port.repository.SellerRepository

class DeleteSellerUseCase(
    private val sellerRepository: SellerRepository,
    private val timeProvider: TimeProvider,
) {
    fun execute(sellerId: Long): Seller {
        val seller =
            sellerRepository.findActiveById(sellerId)
                ?: throw SellerNotFoundException(sellerId)

        val deletedSeller = seller.copy(deletedAt = timeProvider.now())
        return sellerRepository.save(deletedSeller)
    }
}
