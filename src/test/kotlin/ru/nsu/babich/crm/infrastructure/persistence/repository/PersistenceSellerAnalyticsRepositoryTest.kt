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
import ru.nsu.babich.crm.domain.model.Period
import ru.nsu.babich.crm.fixture.SellerFixture
import ru.nsu.babich.crm.fixture.TransactionFixture
import ru.nsu.babich.crm.infrastructure.persistence.mapper.toDomain
import ru.nsu.babich.crm.infrastructure.persistence.mapper.toEntity
import java.math.BigDecimal
import java.time.LocalDateTime

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceSellerAnalyticsRepository::class)
class PersistenceSellerAnalyticsRepositoryTest : AbstractPostgresIntegrationTest() {
    @Autowired
    private lateinit var persistenceSellerAnalyticsRepository: PersistenceSellerAnalyticsRepository

    @Autowired
    private lateinit var jpaSellerRepository: JpaSellerRepository

    @Autowired
    private lateinit var jpaTransactionRepository: JpaTransactionRepository

    @Test
    fun `should find top seller by period`() {
        val sellerJohn = jpaSellerRepository.save(SellerFixture.seller(name = "John Doe").toEntity())
        val sellerJane =
            jpaSellerRepository.save(
                SellerFixture
                    .seller(name = "Jane Doe")
                    .copy(contactInfo = "jane@example.com")
                    .toEntity(),
            )

        val period =
            Period(
                from = LocalDateTime.now().minusDays(1),
                to = LocalDateTime.now().plusDays(1),
            )

        val johnTransaction1 =
            TransactionFixture.transaction(
                seller = sellerJohn.toDomain(),
                amount = "100.00",
            )
        val johnTransaction2 =
            TransactionFixture.transaction(
                seller = sellerJohn.toDomain(),
                amount = "200.00",
                paymentType = PaymentType.CASH,
            )
        val janeTransaction =
            TransactionFixture.transaction(
                seller = sellerJane.toDomain(),
                amount = "150.00",
            )

        jpaTransactionRepository.save(johnTransaction1.toEntity())
        jpaTransactionRepository.save(johnTransaction2.toEntity())
        jpaTransactionRepository.save(janeTransaction.toEntity())

        val topSeller = persistenceSellerAnalyticsRepository.findTopSeller(period)

        assertNotNull(topSeller)
        assertEquals("John Doe", topSeller?.name)
    }

    @Test
    fun `should return null when no transactions in period`() {
        val period =
            Period(
                from = LocalDateTime.now().minusDays(10),
                to = LocalDateTime.now().minusDays(5),
            )

        val topSeller = persistenceSellerAnalyticsRepository.findTopSeller(period)

        assertNull(topSeller)
    }

    @Test
    fun `should not include deleted sellers in analytics`() {
        val deletedSeller =
            jpaSellerRepository.save(
                SellerFixture
                    .seller(name = "Deleted Seller", deletedAt = LocalDateTime.now())
                    .copy(contactInfo = "deleted@example.com")
                    .toEntity(),
            )

        val period =
            Period(
                from = LocalDateTime.now().minusDays(1),
                to = LocalDateTime.now().plusDays(1),
            )

        val transaction =
            TransactionFixture.transaction(
                seller = deletedSeller.toDomain(),
                amount = "100.00",
            )
        jpaTransactionRepository.save(transaction.toEntity())

        val topSeller = persistenceSellerAnalyticsRepository.findTopSeller(period)
        val lowTurnoverSellers =
            persistenceSellerAnalyticsRepository.findSellersWithTurnoverLessThan(
                BigDecimal("200.00"),
                period,
            )

        assertNull(topSeller)
        assertEquals(0, lowTurnoverSellers.size)
    }

    @Test
    fun `should find sellers with turnover less than threshold`() {
        val sellerJohn = jpaSellerRepository.save(SellerFixture.seller(name = "John Doe").toEntity())
        val sellerJane =
            jpaSellerRepository.save(
                SellerFixture
                    .seller(name = "Jane Doe")
                    .copy(contactInfo = "jane@example.com")
                    .toEntity(),
            )
        val sellerBob =
            jpaSellerRepository.save(
                SellerFixture
                    .seller(name = "Bob Smith")
                    .copy(contactInfo = "bob@example.com")
                    .toEntity(),
            )

        val period =
            Period(
                from = LocalDateTime.now().minusDays(1),
                to = LocalDateTime.now().plusDays(1),
            )

        val johnTransaction =
            TransactionFixture.transaction(
                seller = sellerJohn.toDomain(),
                amount = "100.00",
            )
        val janeTransaction =
            TransactionFixture.transaction(
                seller = sellerJane.toDomain(),
                amount = "250.00",
                paymentType = PaymentType.CASH,
            )
        val bobTransaction =
            TransactionFixture.transaction(
                seller = sellerBob.toDomain(),
                amount = "50.00",
            )

        jpaTransactionRepository.save(johnTransaction.toEntity())
        jpaTransactionRepository.save(janeTransaction.toEntity())
        jpaTransactionRepository.save(bobTransaction.toEntity())

        val sellers =
            persistenceSellerAnalyticsRepository.findSellersWithTurnoverLessThan(
                BigDecimal("200.00"),
                period,
            )

        assertEquals(2, sellers.size)
        val names = sellers.map { it.name }.toSet()
        assertEquals(setOf("John Doe", "Bob Smith"), names)
    }
}
