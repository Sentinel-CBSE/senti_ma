package com.unal.senti_ma.data.mappers

import com.unal.senti_ma.data.remote.dto.RobberyPointDto
import com.unal.senti_ma.domain.model.RobberyPoint

fun RobberyPointDto.toDomain(): RobberyPoint = RobberyPoint(
    id = id,
    latitude = latitude,
    longitude = longitude,
    timestamp = timestamp,
    type = type
)
