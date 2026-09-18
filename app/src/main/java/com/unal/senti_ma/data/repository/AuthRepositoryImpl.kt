package com.unal.senti_ma.data.repository

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
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.unal.senti_ma.R
import com.unal.senti_ma.data.mappers.toDomain
import com.unal.senti_ma.domain.model.AppResult
import com.unal.senti_ma.domain.model.User
import com.unal.senti_ma.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val credentialManager: CredentialManager,
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {

    override val authState: Flow<User?> = callbackFlow {

        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.toDomain())
        }

        val idTokenListener = FirebaseAuth.IdTokenListener { auth ->
            trySend(auth.currentUser?.toDomain())
        }

        firebaseAuth.addAuthStateListener(authStateListener)
        firebaseAuth.addIdTokenListener(idTokenListener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
            firebaseAuth.removeIdTokenListener(idTokenListener)
        }
    }

    override fun getCurrentUser(): User? {
        return firebaseAuth.currentUser?.toDomain()
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
    }

    override suspend fun sendPasswordResetEmail(
        email: String
    ): AppResult<Unit> {
        return try {
            firebaseAuth
                .sendPasswordResetEmail(email)
                .await()

            AppResult.Success(Unit)

        } catch (_: Exception) {
            AppResult.Error(
                context.getString(
                    R.string.text_error_send_email
                )
            )
        }
    }

    override suspend fun createUserWithEmailAndPassword(
        name: String,
        email: String,
        password: String,
        activity: Activity
    ): AppResult<User> {
        return try {
            val result = firebaseAuth
                .createUserWithEmailAndPassword(
                    email,
                    password
                )
                .await()

            val firebaseUser = result.user
                ?: return AppResult.Error(
                    context.getString(
                        R.string.text_error_sign_up
                    )
                )

            firebaseUser.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
            ).await()

            firebaseUser.sendEmailVerification().await()

            try {
                val request = CreatePasswordRequest(
                    email,
                    password
                )

                credentialManager.createCredential(
                    activity,
                    request
                ) as CreatePasswordResponse

            } catch (_: Exception) {
                Log.d(
                    "Dev", context.getString(
                        R.string.text_error_smart_lock
                    )
                )
            }

            AppResult.Success(firebaseUser.toDomain())

        } catch (_: FirebaseAuthUserCollisionException) {
            AppResult.Error(
                context.getString(
                    R.string.text_error_email_already_exists
                )
            )

        } catch (_: Exception) {
            AppResult.Error(context.getString(R.string.text_error_sign_up))
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): AppResult<User> {
        return try {
            val result = firebaseAuth
                .signInWithEmailAndPassword(
                    email,
                    password
                )
                .await()

            val firebaseUser = result.user
                ?: return AppResult.Error(
                    context.getString(
                        R.string.text_error_sign_in
                    )
                )

            firebaseUser.reload().await()

            if (!firebaseUser.isEmailVerified) {
                return AppResult.Error(
                    context.getString(
                        R.string.text_error_email_not_verified
                    )
                )
            }

            AppResult.Success(
                firebaseUser.toDomain()
            )

        } catch (_: Exception) {
            AppResult.Error(
                context.getString(
                    R.string.text_error_sign_in
                )
            )
        }
    }

    override suspend fun signInWithSavedCredentials(
        activity: Activity
    ): AppResult<User> {
        return try {
            val request = getSignInRequest()

            val result = credentialManager.getCredential(
                request = request,
                context = activity
            )

            handleAuthResult(result)

        } catch (_: GetCredentialCancellationException) {
            AppResult.Cancelled

        } catch (_: Exception) {
            AppResult.Error(
                context.getString(
                    R.string.text_error_sign_in
                )
            )
        }
    }

    override suspend fun signInWithGoogle(
        activity: Activity
    ): AppResult<User> {
        return try {
            val request = getGoogleRequest()

            val result = credentialManager.getCredential(
                request = request,
                context = activity
            )

            handleAuthResult(result)

        } catch (_: GetCredentialCancellationException) {
            AppResult.Cancelled

        } catch (_: Exception) {
            AppResult.Error(
                context.getString(
                    R.string.text_error_sign_in
                )
            )
        }
    }

    override suspend fun signInAnonymously(): AppResult<User> {
        return try {
            val result = firebaseAuth
                .signInAnonymously()
                .await()

            val firebaseUser = result.user
                ?: return AppResult.Error(
                    context.getString(
                        R.string.text_error_sign_in
                    )
                )

            AppResult.Success(
                firebaseUser.toDomain()
            )

        } catch (_: Exception) {
            AppResult.Error(
                context.getString(
                    R.string.text_error_sign_in
                )
            )
        }
    }

    private fun getSignInRequest(): GetCredentialRequest {
        val getPasswordOption = GetPasswordOption()

        val getGoogleIdOption =
            GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .setAutoSelectEnabled(true)
                .setNonce(UUID.randomUUID().toString())
                .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(getPasswordOption)
            .addCredentialOption(getGoogleIdOption)
            .build()
    }

    private fun getGoogleRequest(): GetCredentialRequest {
        val signInRequestOptions =
            GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .setAutoSelectEnabled(false)
                .setNonce(UUID.randomUUID().toString())
                .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(signInRequestOptions)
            .build()
    }

    private suspend fun handleAuthResult(
        result: GetCredentialResponse
    ): AppResult<User> {
        return try {
            when (val credential = result.credential) {

                is CustomCredential -> {
                    if (
                        credential.type ==
                        GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                    ) {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(
                                credential.data
                            )

                        val googleCredentials =
                            GoogleAuthProvider.getCredential(
                                googleIdTokenCredential.idToken,
                                null
                            )

                        val result =
                            firebaseAuth
                                .signInWithCredential(
                                    googleCredentials
                                )
                                .await()

                        val firebaseUser = result.user
                            ?: return AppResult.Error(
                                context.getString(
                                    R.string.text_error_sign_in
                                )
                            )

                        AppResult.Success(
                            firebaseUser.toDomain()
                        )

                    } else {
                        AppResult.Error(
                            context.getString(
                                R.string.text_error_sign_in
                            )
                        )
                    }
                }

                is PasswordCredential -> {
                    signInWithEmailAndPassword(
                        credential.id,
                        credential.password
                    )
                }

                else -> {
                    AppResult.Error(
                        context.getString(
                            R.string.text_error_sign_in
                        )
                    )
                }
            }

        } catch (_: Exception) {
            AppResult.Error(context.getString(R.string.text_error_sign_in))
        }
    }
}
