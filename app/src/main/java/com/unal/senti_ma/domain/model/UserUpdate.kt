package com.unal.senti_ma.domain.model

import com.unal.senti_ma.domain.enums.BloodTypeLetter
import com.unal.senti_ma.domain.enums.BloodTypeRh

data class UserUpdate(
    val displayName: String? = null,
    val bloodTypeLetter: BloodTypeLetter? = null,
    val bloodTypeRh: BloodTypeRh? = null,
    val eps: String? = null
)
