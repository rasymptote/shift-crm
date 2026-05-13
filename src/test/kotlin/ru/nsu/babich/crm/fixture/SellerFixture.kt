package ru.nsu.babich.crm.fixture

import ru.nsu.babich.crm.domain.model.Seller
import java.time.LocalDateTime

object SellerFixture {
    fun seller(
        name: String = "John Doe",
        deletedAt: LocalDateTime? = null,
    ) = Seller(
        id = null,
        name = name,
        contactInfo = "john@example.com",
        registrationDate = LocalDateTime.now(),
        deletedAt = deletedAt,
    )
}
