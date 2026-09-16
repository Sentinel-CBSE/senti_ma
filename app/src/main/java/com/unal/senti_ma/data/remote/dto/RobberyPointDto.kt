package com.unal.senti_ma.data.remote.dto

data class RobberyPointDto(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long?,
    val type: String?
)
