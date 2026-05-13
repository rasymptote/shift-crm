package ru.nsu.babich.crm.infrastructure.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.nsu.babich.crm.infrastructure.persistence.entity.SellerEntity

interface JpaSellerRepository : JpaRepository<SellerEntity, Long>
