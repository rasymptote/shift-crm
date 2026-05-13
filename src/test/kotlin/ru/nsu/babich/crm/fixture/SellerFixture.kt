package ru.nsu.babich.crm.fixture

import ru.nsu.babich.crm.domain.model.Seller
import java.time.LocalDateTime

object SellerFixture {
    fun seller(
        id: Long? = null,
        name: String = "John Doe",
        contactInfo: String = "john@example.com",
        registrationDate: LocalDateTime =
            LocalDateTime.of(2024, 1, 1, 0, 0),
        deletedAt: LocalDateTime? = null,
    ) = Seller(
        id = id,
        name = name,
        contactInfo = contactInfo,
        registrationDate = registrationDate,
        deletedAt = deletedAt,
    )
}
