package com.example.senti_ma.domain.model

import android.net.Uri

/**
 * Domain-level representation of an authenticated user, decoupled from any auth provider.
 */
data class User(
    val uid: String,
    val displayName: String?,
    val email: String?,
    val photoUrl: Uri?,
    val isAnonymous: Boolean,
    val isEmailVerified: Boolean
)
