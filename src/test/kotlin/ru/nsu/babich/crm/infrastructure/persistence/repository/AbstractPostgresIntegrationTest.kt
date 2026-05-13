package ru.nsu.babich.crm.infrastructure.persistence.repository

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Suppress("UtilityClassWithPublicConstructor")
@Testcontainers
abstract class AbstractPostgresIntegrationTest {
    companion object {
        @Container
        val postgres =
            PostgreSQLContainer("postgres:15-alpine")
                .apply {
                    withDatabaseName("testdb")
                    withUsername("test")
                    withPassword("test")
                }

        @DynamicPropertySource
        @JvmStatic
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add(
                "spring.datasource.url",
                postgres::getJdbcUrl,
            )
            registry.add(
                "spring.datasource.username",
                postgres::getUsername,
            )
            registry.add(
                "spring.datasource.password",
                postgres::getPassword,
            )
        }
    }
}
