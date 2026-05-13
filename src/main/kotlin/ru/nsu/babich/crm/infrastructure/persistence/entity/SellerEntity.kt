package ru.nsu.babich.crm.infrastructure.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@Entity
@Table(name = "sellers")
@NoArgsConstructor
@AllArgsConstructor
class SellerEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @Column(nullable = false)
    var name: String,
    @Column(nullable = false)
    var contactInfo: String,
    @Column(nullable = false)
    var registrationDate: LocalDateTime,
    var deletedAt: LocalDateTime? = null,
)
