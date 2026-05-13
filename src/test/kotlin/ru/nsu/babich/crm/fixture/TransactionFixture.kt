package ru.nsu.babich.crm.fixture

import ru.nsu.babich.crm.domain.model.PaymentType
import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.model.Transaction
import java.math.BigDecimal
import java.time.LocalDateTime

object TransactionFixture {
    fun transaction(
        id: Long? = null,
        seller: Seller = SellerFixture.seller(),
        amount: String = "100.00",
        paymentType: PaymentType = PaymentType.CARD,
        transactionDate: LocalDateTime =
            LocalDateTime.of(2024, 1, 1, 0, 0),
    ) = Transaction(
        id = id,
        seller = seller,
        amount = BigDecimal(amount),
        paymentType = paymentType,
        transactionDate = transactionDate,
    )
}
