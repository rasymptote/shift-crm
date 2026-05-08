package ru.nsu.babich.crm.domain.port

import java.time.LocalDateTime

interface TimeProvider {
    fun now(): LocalDateTime
}
