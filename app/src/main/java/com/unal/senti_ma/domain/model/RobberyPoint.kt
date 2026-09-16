package com.unal.senti_ma.domain.model

data class RobberyPoint(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long? = null,
    val type: String? = null
)
