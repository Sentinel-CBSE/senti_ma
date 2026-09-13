package com.example.senti_ma.domain.model

data class RobberyPoint(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long? = null,
    val type: String? = null
)
