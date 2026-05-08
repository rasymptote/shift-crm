package ru.nsu.babich.crm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CrmApplication

fun main(args: Array<String>) {
    @Suppress("SpreadOperator")
    runApplication<CrmApplication>(*args)
}
