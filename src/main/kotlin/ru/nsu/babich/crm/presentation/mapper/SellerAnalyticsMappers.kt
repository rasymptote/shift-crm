package ru.nsu.babich.crm.presentation.mapper

import ru.nsu.babich.crm.application.dto.GetSellersWithTurnoverLessThanDto
import ru.nsu.babich.crm.domain.model.Period
import ru.nsu.babich.crm.presentation.dto.PeriodRequest
import ru.nsu.babich.crm.presentation.dto.SellersWithTurnoverLessThanRequest

fun PeriodRequest.toPeriod(): Period =
    Period(
        from = from,
        to = to,
    )

fun SellersWithTurnoverLessThanRequest.toDto(): GetSellersWithTurnoverLessThanDto =
    GetSellersWithTurnoverLessThanDto(
        threshold = threshold,
        period = period.toPeriod(),
    )
