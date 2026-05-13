package ru.nsu.babich.crm.infrastructure.persistence.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import ru.nsu.babich.crm.fixture.SellerFixture
import ru.nsu.babich.crm.infrastructure.persistence.mapper.toEntity
import java.time.LocalDateTime

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceSellerRepository::class)
class PersistenceSellerRepositoryTest : AbstractPostgresIntegrationTest() {
    @Autowired
    private lateinit var persistenceSellerRepository: PersistenceSellerRepository

    @Autowired
    private lateinit var jpaSellerRepository: JpaSellerRepository

    @Test
    fun `should find all active sellers`() {
        val activeSeller = SellerFixture.seller(name = "John Doe")
        val deletedSeller =
            SellerFixture
                .seller(name = "Jane Doe", deletedAt = LocalDateTime.now())
                .copy(contactInfo = "jane@example.com")
        jpaSellerRepository.save(activeSeller.toEntity())
        jpaSellerRepository.save(deletedSeller.toEntity())

        val sellers = persistenceSellerRepository.findAll()

        assertEquals(1, sellers.size)
        assertEquals("John Doe", sellers[0].name)
        assertTrue(sellers[0].isActive)
    }

    @Test
    fun `should find active seller by id`() {
        val savedSeller = jpaSellerRepository.save(SellerFixture.seller(name = "John Doe").toEntity())

        val seller = persistenceSellerRepository.findActiveById(savedSeller.id)

        assertNotNull(seller)
        assertEquals("John Doe", seller?.name)
        assertTrue(seller?.isActive == true)
    }

    @Test
    fun `should not find deleted seller by id`() {
        val savedSeller =
            jpaSellerRepository.save(
                SellerFixture.seller(name = "John Doe", deletedAt = LocalDateTime.now()).toEntity(),
            )

        val seller = persistenceSellerRepository.findActiveById(savedSeller.id)

        assertNull(seller)
    }

    @Test
    fun `should save seller`() {
        val seller = SellerFixture.seller(name = "John Doe")

        val savedSeller = persistenceSellerRepository.save(seller)

        assertNotNull(savedSeller.id)
        assertEquals("John Doe", savedSeller.name)
        assertTrue(savedSeller.isActive)
    }
}
