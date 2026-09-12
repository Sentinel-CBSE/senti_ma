package com.example.senti_ma.data.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CreatePasswordResponse
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import com.example.senti_ma.R
import com.example.senti_ma.data.mappers.toDomain
import com.example.senti_ma.domain.model.AuthResult
import com.example.senti_ma.domain.model.User
import com.example.senti_ma.domain.repository.AuthRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Firebase-backed implementation of [AuthRepository].
 * This is the only place in the app that knows about Firebase or CredentialManager.
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val credentialManager: CredentialManager,
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {

    override val authState: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.toDomain())
        }
        firebaseAuth.addAuthStateListener(listener)
        awaitClose { firebaseAuth.removeAuthStateListener(listener) }
    }

    override fun getCurrentUser(): User? {
        return firebaseAuth.currentUser?.toDomain()
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
    }

    override suspend fun sendPasswordResetEmail(email: String): AuthResult<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            AuthResult.Success(Unit)
        } catch (e: Exception) {
            AuthResult.Error(context.getString(R.string.text_error_send_email))
        }
    }

    override suspend fun createUserWithEmailAndPassword(
        name: String,
        email: String,
        password: String,
        activity: Activity
    ): AuthResult<User> {
        return try {
            val firebaseUser = firebaseAuth.createUserWithEmailAndPassword(email, password).await().user!!
            firebaseUser.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build()).await()
            firebaseUser.sendEmailVerification().await()

            try {
                val request = CreatePasswordRequest(email, password)
                credentialManager.createCredential(activity, request) as CreatePasswordResponse
            } catch (e: Exception) {
                Log.d("Dev", context.getString(R.string.text_error_smart_lock) + "${e.message}")
            }
            AuthResult.Success(firebaseUser.toDomain())
        } catch (e: FirebaseAuthUserCollisionException) {
            AuthResult.Error(context.getString(R.string.text_error_email_already_exists))
        } catch (e: Exception) {
            AuthResult.Error(context.getString(R.string.text_error_sign_up))
        }
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult<User> {
        return try {
            val firebaseUser = firebaseAuth.signInWithEmailAndPassword(email, password).await().user!!

            if (firebaseUser.isEmailVerified) AuthResult.Success(firebaseUser.toDomain())
            else AuthResult.Error(context.getString(R.string.text_error_email_not_verified))
        } catch (e: Exception) {
            AuthResult.Error(context.getString(R.string.text_error_sign_in) + e.message)
        }
    }

    override suspend fun signInWithSavedCredentials(activity: Activity): AuthResult<User> {
        return try {
            val request = getSignInRequest()
            val result = credentialManager.getCredential(request = request, context = activity)
            handleAuthResult(result)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: context.getString(R.string.text_error_sign_in))
        }
    }

    override suspend fun signInWithGoogle(activity: Activity): AuthResult<User> {
        return try {
            val request = getGoogleRequest()
            val result = credentialManager.getCredential(request = request, context = activity)
            handleAuthResult(result)
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: context.getString(R.string.text_error_sign_in))
        }
    }

    override suspend fun signInAnonymously(): AuthResult<User> {
        return try {
            val firebaseUser = firebaseAuth.signInAnonymously().await().user!!
            AuthResult.Success(firebaseUser.toDomain())
        } catch (e: Exception) {
            AuthResult.Error(context.getString(R.string.text_error_sign_in))
        }
    }

    private fun getSignInRequest(): GetCredentialRequest {
        val getPasswordOption = GetPasswordOption()
        val getGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId("18125175753-rrju41lblttrndipuqa7gea1cp9gmv5g.apps.googleusercontent.com")
            .setAutoSelectEnabled(true)
            .setNonce(UUID.randomUUID().toString())
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(getPasswordOption)
            .addCredentialOption(getGoogleIdOption)
            .build()
    }

    private fun getGoogleRequest(): GetCredentialRequest {
        val signInRequestOptions = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("18125175753-rrju41lblttrndipuqa7gea1cp9gmv5g.apps.googleusercontent.com")
            .setAutoSelectEnabled(false)
            .setNonce(UUID.randomUUID().toString())
            .build()
        return GetCredentialRequest.Builder()
            .addCredentialOption(signInRequestOptions)
            .build()
    }

    private suspend fun handleAuthResult(result: GetCredentialResponse): AuthResult<User> {
        return try {
            when (val credential = result.credential) {
                is CustomCredential -> {
                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        val googleCredentials = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                        val firebaseUser = firebaseAuth.signInWithCredential(googleCredentials).await().user!!
                        AuthResult.Success(firebaseUser.toDomain())
                    } else {
                        AuthResult.Error(context.getString(R.string.text_error_sign_in))
                    }
                }
                is PasswordCredential -> {
                    signInWithEmailAndPassword(credential.id, credential.password)
                }
                else -> AuthResult.Error(context.getString(R.string.text_error_sign_in))
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: context.getString(R.string.text_error_sign_in))
        }
    }
}
