package ru.nsu.babich.crm.infrastructure.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.nsu.babich.crm.application.usecase.seller.CreateSellerUseCase
import ru.nsu.babich.crm.application.usecase.seller.DeleteSellerUseCase
import ru.nsu.babich.crm.application.usecase.seller.GetAllSellersUseCase
import ru.nsu.babich.crm.application.usecase.seller.GetSellerUseCase
import ru.nsu.babich.crm.application.usecase.seller.UpdateSellerUseCase
import ru.nsu.babich.crm.domain.port.repository.SellerRepository

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
}
