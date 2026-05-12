package ru.nsu.babich.crm.application.usecase.seller

import ru.nsu.babich.crm.application.dto.CreateSellerDto
import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.port.repository.SellerRepository
import java.time.LocalDateTime

class CreateSellerUseCase(
    private val sellerRepository: SellerRepository,
) {
    fun execute(dto: CreateSellerDto): Seller {
        val seller =
            Seller(
                id = null,
                name = dto.name,
                contactInfo = dto.contactInfo,
                registrationDate = LocalDateTime.now(),
            )

        return sellerRepository.save(seller)
    }
}
