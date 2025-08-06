package com.ipekkochisarli.obssmovies.features.login

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class GoogleAuthClient(
    private val activity: Activity,
    private val clientId: String,
    private val signInLauncher: ActivityResultLauncher<Intent>,
    private val onSuccess: (idToken: String) -> Unit,
    private val onError: (Exception) -> Unit,
) {
    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso =
            GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientId)
                .requestEmail()
                .build()
        GoogleSignIn.getClient(activity, gso)
    }

    fun signIn() {
        signInLauncher.launch(googleSignInClient.signInIntent)
    }

    fun handleSignInResult(data: Intent?) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            if (account?.idToken != null) {
                onSuccess(account.idToken!!)
            } else {
                onError(Exception("Google Sign-In failed: token is null"))
            }
        } catch (e: ApiException) {
            onError(e)
        }
    }
}
