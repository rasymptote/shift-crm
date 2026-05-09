package ru.nsu.babich.crm.application.usecase

import ru.nsu.babich.crm.application.dto.UpdateSellerDto
import ru.nsu.babich.crm.domain.exception.SellerNotFoundException
import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.port.repository.SellerRepository

class UpdateSellerUseCase(
    private val sellerRepository: SellerRepository,
) {
    fun execute(dto: UpdateSellerDto): Seller {
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
