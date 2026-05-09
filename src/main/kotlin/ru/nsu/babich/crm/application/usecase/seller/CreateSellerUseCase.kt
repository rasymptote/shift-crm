package ru.nsu.babich.crm.application.usecase.seller

import ru.nsu.babich.crm.application.dto.CreateSellerDto
import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.port.TimeProvider
import ru.nsu.babich.crm.domain.port.repository.SellerRepository

class CreateSellerUseCase(
    private val sellerRepository: SellerRepository,
    private val timeProvider: TimeProvider,
) {
    fun execute(dto: CreateSellerDto): Seller {
        val seller =
            Seller(
                id = null,
                name = dto.name,
                contactInfo = dto.contactInfo,
                registrationDate = timeProvider.now(),
            )

        return sellerRepository.save(seller)
    }
}
