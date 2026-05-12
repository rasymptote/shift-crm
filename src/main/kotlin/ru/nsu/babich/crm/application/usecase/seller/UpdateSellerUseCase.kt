package ru.nsu.babich.crm.application.usecase.seller

import ru.nsu.babich.crm.application.dto.UpdateSellerDto
import ru.nsu.babich.crm.domain.exception.SellerNotFoundException
import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.port.repository.SellerRepository

class UpdateSellerUseCase(
    private val sellerRepository: SellerRepository,
) {
    operator fun invoke(dto: UpdateSellerDto): Seller {
        val existingSeller =
            sellerRepository.findActiveById(dto.id)
                ?: throw SellerNotFoundException(dto.id)
        val updatedSeller =
            existingSeller.copy(
                name = dto.name,
                contactInfo = dto.contactInfo,
            )
        return sellerRepository.save(updatedSeller)
    }
}
