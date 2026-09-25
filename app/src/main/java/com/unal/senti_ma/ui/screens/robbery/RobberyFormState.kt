package com.unal.senti_ma.ui.screens.robbery

import com.unal.senti_ma.domain.model.Coordinates

data class RobberyFormState(
    val type: String? = null,
    val fromTimestamp: Long? = null,
    val toTimestamp: Long? = null,
    val address: String = "",
    val location: Coordinates? = null
)
