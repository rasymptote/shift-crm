package ru.nsu.babich.crm.domain.port.repository

import ru.nsu.babich.crm.domain.model.Seller

interface SellerRepository {
    fun findAll(): List<Seller>

    fun findActiveById(id: Long): Seller?

    fun save(seller: Seller): Seller
}
