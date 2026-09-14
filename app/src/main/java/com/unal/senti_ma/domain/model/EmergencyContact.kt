package com.unal.senti_ma.domain.model

data class EmergencyContact(
    val uid: String,
    val name: String,
    val phoneNumber: String,
    val relationship: String
)
