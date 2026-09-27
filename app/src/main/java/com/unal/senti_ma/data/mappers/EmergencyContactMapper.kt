package com.unal.senti_ma.data.mappers

import com.unal.senti_ma.data.remote.dto.EmergencyContactDto
import com.unal.senti_ma.domain.model.EmergencyContact

fun EmergencyContactDto.toDomain(): EmergencyContact {
    return EmergencyContact(
        uid = uid,
        name = name,
        phoneNumber = phoneNumber,
        relationship = relationship
    )
}
