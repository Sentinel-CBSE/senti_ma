package com.unal.senti_ma.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val COLOMBIAN_LOCALE = Locale.forLanguageTag("es-CO")

private val DATE_TIME_FORMATTER = DateTimeFormatter
    .ofPattern("d MMM yyyy, HH:mm", COLOMBIAN_LOCALE)
    .withZone(ZoneId.systemDefault())

private val DATE_FORMATTER = DateTimeFormatter
    .ofPattern("d MMM yyyy", COLOMBIAN_LOCALE)
    .withZone(ZoneId.systemDefault())

fun formatTimestamp(timestamp: Long): String =
    DATE_TIME_FORMATTER.format(
        Instant.ofEpochMilli(timestamp)
    )

fun formatDate(timestamp: Long): String =
    DATE_FORMATTER.format(
        Instant.ofEpochMilli(timestamp)
    )
