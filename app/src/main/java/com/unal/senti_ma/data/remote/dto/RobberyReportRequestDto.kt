package com.unal.senti_ma.data.remote.dto

data class RobberyReportRequestDto(
    val type: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long
)
