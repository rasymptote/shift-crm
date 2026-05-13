package ru.nsu.babich.crm.infrastructure.persistence.repository

import org.springframework.stereotype.Repository
import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.port.repository.SellerRepository
import ru.nsu.babich.crm.infrastructure.persistence.mapper.toDomain
import ru.nsu.babich.crm.infrastructure.persistence.mapper.toEntity

@Repository
class PersistenceSellerRepository(
    private val jpaSellerRepository: JpaSellerRepository,
) : SellerRepository {
    override fun findAll() =
        jpaSellerRepository
            .findAll()
            .filter { it.deletedAt == null }
            .map { it.toDomain() }

    override fun findActiveById(id: Long): Seller? =
        jpaSellerRepository
            .findById(id)
            .filter { it.deletedAt == null }
            .map { it.toDomain() }
            .orElse(null)

    override fun save(seller: Seller): Seller = jpaSellerRepository.save(seller.toEntity()).toDomain()
}
