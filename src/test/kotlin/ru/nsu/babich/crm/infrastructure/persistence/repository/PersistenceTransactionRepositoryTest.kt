package ru.nsu.babich.crm.infrastructure.persistence.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import ru.nsu.babich.crm.domain.model.PaymentType
import ru.nsu.babich.crm.fixture.SellerFixture
import ru.nsu.babich.crm.fixture.TransactionFixture
import ru.nsu.babich.crm.infrastructure.persistence.mapper.toDomain
import ru.nsu.babich.crm.infrastructure.persistence.mapper.toEntity
import java.math.BigDecimal
import java.time.LocalDateTime

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceTransactionRepository::class)
class PersistenceTransactionRepositoryTest : AbstractPostgresIntegrationTest() {
    @Autowired
    private lateinit var persistenceTransactionRepository: PersistenceTransactionRepository

    @Autowired
    private lateinit var jpaSellerRepository: JpaSellerRepository

    @Autowired
    private lateinit var jpaTransactionRepository: JpaTransactionRepository

    @Test
    fun `should find all transactions`() {
        val savedSeller = jpaSellerRepository.save(SellerFixture.seller().toEntity())

        jpaTransactionRepository.save(TransactionFixture.transaction(savedSeller.toDomain()).toEntity())

        val transactions = persistenceTransactionRepository.findAll()

        assertEquals(1, transactions.size)
        assertEquals(BigDecimal("100.00"), transactions[0].amount)
    }

    @Test
    fun `should find all transactions by seller id for active seller`() {
        val savedSeller = jpaSellerRepository.save(SellerFixture.seller().toEntity())

        jpaTransactionRepository.save(TransactionFixture.transaction(savedSeller.toDomain()).toEntity())
        jpaTransactionRepository.save(
            TransactionFixture
                .transaction(savedSeller.toDomain(), "200.00", PaymentType.CASH)
                .toEntity(),
        )

        val transactions = persistenceTransactionRepository.findAllBySellerId(savedSeller.id)

        assertEquals(2, transactions.size)
        assertEquals(BigDecimal("100.00"), transactions[0].amount)
        assertEquals(BigDecimal("200.00"), transactions[1].amount)
    }

    @Test
    fun `should not find transactions by seller id for deleted seller`() {
        val savedSeller =
            jpaSellerRepository.save(
                SellerFixture.seller(deletedAt = LocalDateTime.now()).toEntity(),
            )

        jpaTransactionRepository.save(TransactionFixture.transaction(savedSeller.toDomain()).toEntity())

        val transactions = persistenceTransactionRepository.findAllBySellerId(savedSeller.id)

        assertEquals(0, transactions.size)
    }

    @Test
    fun `should find transaction by id`() {
        val savedSeller = jpaSellerRepository.save(SellerFixture.seller().toEntity())

        val savedTransaction =
            jpaTransactionRepository.save(
                TransactionFixture.transaction(savedSeller.toDomain()).toEntity(),
            )

        val transaction = persistenceTransactionRepository.findById(savedTransaction.id)

        assertNotNull(transaction)
        assertEquals(BigDecimal("100.00"), transaction?.amount)
    }

    @Test
    fun `should return null for non-existent transaction id`() {
        val transaction = persistenceTransactionRepository.findById(999L)

        assertNull(transaction)
    }

    @Test
    fun `should save transaction`() {
        val seller = jpaSellerRepository.save(SellerFixture.seller().toEntity())

        val transaction =
            TransactionFixture.transaction(
                seller = seller.toDomain(),
                amount = "150.00",
                paymentType = PaymentType.CASH,
            )

        val savedTransaction = persistenceTransactionRepository.save(transaction)

        assertNotNull(savedTransaction.id)
        assertEquals(BigDecimal("150.00"), savedTransaction.amount)
    }
}
