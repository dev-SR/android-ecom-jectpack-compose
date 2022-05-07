package com.my.ecomr.domains.services

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.my.ecomr.BuildConfig
import com.my.ecomr.Response
import com.my.ecomr.domains.models.User
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
) {
    fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser
        var user: User? = if (firebaseUser != null) {
            User(firebaseUser.displayName!!, firebaseUser.email!!)
        } else
            null

        return user
    }

    fun logOut() {
        auth.signOut()
    }

    suspend fun signWithGoogleCredential(credential: AuthCredential) = flow {
        try {
            emit(Response.Loading)
            auth.signInWithCredential(credential).await()
            val firebaseUser: FirebaseUser = auth.currentUser!!
            Log.d("user", firebaseUser.email!!)
            emit(Response.Success(User(firebaseUser.displayName!!, firebaseUser.email!!)))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: "Error"))
        }
    }


}

fun getGoogleSignInClient(context: Context): GoogleSignInClient {
    val WEB_CLIENT_ID = BuildConfig.WEB_CLIENT_ID
    val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(WEB_CLIENT_ID)
        .requestEmail()
        .build()

    return GoogleSignIn.getClient(context, signInOptions)
}