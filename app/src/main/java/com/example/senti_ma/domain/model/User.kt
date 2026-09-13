package com.example.senti_ma.domain.model

import android.net.Uri

data class User(
    val uid: String,
    val email: String?,
    val photoUrl: Uri?,
    val displayName: String?,
    val isAnonymous: Boolean,
    val isEmailVerified: Boolean
)
