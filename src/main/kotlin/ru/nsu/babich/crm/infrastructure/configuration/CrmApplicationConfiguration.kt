package ru.nsu.babich.crm.infrastructure.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.nsu.babich.crm.application.usecase.seller.CreateSellerUseCase
import ru.nsu.babich.crm.application.usecase.seller.DeleteSellerUseCase
import ru.nsu.babich.crm.application.usecase.seller.GetAllSellersUseCase
import ru.nsu.babich.crm.application.usecase.seller.GetSellerUseCase
import ru.nsu.babich.crm.application.usecase.seller.UpdateSellerUseCase
import ru.nsu.babich.crm.application.usecase.transaction.CreateTransactionUseCase
import ru.nsu.babich.crm.application.usecase.transaction.GetAllTransactionsUseCase
import ru.nsu.babich.crm.application.usecase.transaction.GetTransactionUseCase
import ru.nsu.babich.crm.application.usecase.transaction.GetTransactionsBySellerUseCase
import ru.nsu.babich.crm.domain.port.repository.SellerRepository
import ru.nsu.babich.crm.domain.port.repository.TransactionRepository

@Configuration
class CrmApplicationConfiguration {
    @Bean
    fun createSellerUseCase(repository: SellerRepository): CreateSellerUseCase = CreateSellerUseCase(repository)

    @Bean
    fun deleteSellerUseCase(repository: SellerRepository): DeleteSellerUseCase = DeleteSellerUseCase(repository)

    @Bean
    fun getSellerUseCase(repository: SellerRepository): GetSellerUseCase = GetSellerUseCase(repository)

    @Bean
    fun updateSellerUseCase(repository: SellerRepository): UpdateSellerUseCase = UpdateSellerUseCase(repository)

    @Bean
    fun getAllSellersUseCase(repository: SellerRepository): GetAllSellersUseCase = GetAllSellersUseCase(repository)

    @Bean
    fun createTransactionUseCase(
        transactionRepository: TransactionRepository,
        sellerRepository: SellerRepository,
    ): CreateTransactionUseCase =
        CreateTransactionUseCase(transactionRepository = transactionRepository, sellerRepository = sellerRepository)

    @Bean
    fun getAllTransactionsUseCase(repository: TransactionRepository): GetAllTransactionsUseCase = GetAllTransactionsUseCase(repository)

    @Bean
    fun getTransactionsBySellerUseCase(repository: TransactionRepository): GetTransactionsBySellerUseCase =
        GetTransactionsBySellerUseCase(repository)

    @Bean
    fun getTransactionUseCase(repository: TransactionRepository): GetTransactionUseCase = GetTransactionUseCase(repository)
}
