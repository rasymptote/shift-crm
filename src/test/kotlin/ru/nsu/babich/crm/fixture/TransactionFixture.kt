package ru.nsu.babich.crm.fixture

import ru.nsu.babich.crm.domain.model.PaymentType
import ru.nsu.babich.crm.domain.model.Seller
import ru.nsu.babich.crm.domain.model.Transaction
import java.math.BigDecimal
import java.time.LocalDateTime

object TransactionFixture {
    fun transaction(
        seller: Seller,
        amount: String = "100.00",
        paymentType: PaymentType = PaymentType.CARD,
    ) = Transaction(
        id = null,
        seller = seller,
        amount = BigDecimal(amount),
        paymentType = paymentType,
        transactionDate = LocalDateTime.now(),
    )
}
