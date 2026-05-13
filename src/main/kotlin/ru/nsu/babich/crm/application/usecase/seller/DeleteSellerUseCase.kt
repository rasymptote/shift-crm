package ru.nsu.babich.crm.application.usecase.seller

import ru.nsu.babich.crm.domain.exception.SellerNotFoundException
import ru.nsu.babich.crm.domain.port.repository.SellerRepository
import java.time.LocalDateTime

class DeleteSellerUseCase(
    private val sellerRepository: SellerRepository,
) {
    operator fun invoke(sellerId: Long) {
        val seller =
            sellerRepository.findActiveById(sellerId)
                ?: throw SellerNotFoundException(sellerId)

        val deletedSeller = seller.copy(deletedAt = LocalDateTime.now())
        sellerRepository.save(deletedSeller)
    }
}
