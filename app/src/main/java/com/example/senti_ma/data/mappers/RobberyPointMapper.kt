package com.example.senti_ma.data.mappers

import com.example.senti_ma.data.remote.dto.RobberyPointDto
import com.example.senti_ma.domain.model.RobberyPoint

fun RobberyPointDto.toDomain(): RobberyPoint = RobberyPoint(
    latitude = latitude,
    longitude = longitude,
    timestamp = timestamp,
    type = type
)
