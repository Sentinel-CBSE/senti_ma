package com.unal.senti_ma.data.mappers

import android.location.Location
import com.unal.senti_ma.domain.model.Coordinates

fun Location.toDomain(): Coordinates = Coordinates(
    latitude = latitude,
    longitude = longitude
)
