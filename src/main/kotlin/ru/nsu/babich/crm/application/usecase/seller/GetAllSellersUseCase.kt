package ru.nsu.babich.crm.application.usecase.seller

import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.port.repository.SellerRepository

class GetAllSellersUseCase(
    private val sellerRepository: SellerRepository,
) {
    operator fun invoke(): List<Seller> = sellerRepository.findAll().filter { it.isActive }
}
