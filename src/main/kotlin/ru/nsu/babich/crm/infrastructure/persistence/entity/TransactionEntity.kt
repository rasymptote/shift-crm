package ru.nsu.babich.crm.infrastructure.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import ru.nsu.babich.crm.domain.model.PaymentType
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "transactions")
@NoArgsConstructor
@AllArgsConstructor
class TransactionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    var seller: SellerEntity,
    @Column(nullable = false)
    var amount: BigDecimal,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var paymentType: PaymentType,
    @Column(nullable = false)
    var transactionDate: LocalDateTime,
)
