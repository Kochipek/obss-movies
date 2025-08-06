package com.ipekkochisarli.obssmovies.core.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferencesManager
    @Inject
    constructor(
        @ApplicationContext context: Context,
    ) {
        private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        companion object {
            private const val PREF_NAME = "com.ipekkochisarli.obssmovies.PREFS"
            private const val IS_ONBOARDING_FINISHED = "is_onboarding_finished"
            private const val KEY_REMEMBER_ME = "key_remember_me"
            private const val KEY_SAVED_EMAIL = "key_saved_email"
            private const val KEY_IS_USER_LOGGED_IN = "key_is_user_logged_in"
        }

        private fun getBoolean(
            key: String,
            defaultValue: Boolean = false,
        ): Boolean = sharedPreferences.getBoolean(key, defaultValue)

        private fun saveBoolean(
            key: String,
            value: Boolean,
        ) {
            sharedPreferences.edit { putBoolean(key, value) }
        }

        private fun saveString(
            key: String,
            value: String,
        ) {
            sharedPreferences.edit { putString(key, value) }
        }

        private fun getString(key: String): String? = sharedPreferences.getString(key, null)

        // Remember Me
        fun isRememberMeEnabled() = getBoolean(KEY_REMEMBER_ME)

        fun setRememberMeEnabled(enabled: Boolean) = saveBoolean(KEY_REMEMBER_ME, enabled)

        fun saveEmail(email: String) = saveString(KEY_SAVED_EMAIL, email)

        fun getSavedEmail(): String? = getString(KEY_SAVED_EMAIL)

        // User Logged In
        fun isUserLoggedIn() = getBoolean(KEY_IS_USER_LOGGED_IN)

        fun setUserLoggedIn(loggedIn: Boolean) = saveBoolean(KEY_IS_USER_LOGGED_IN, loggedIn)

        // Onboarding
        fun isOnBoardingFinished(): Boolean = getBoolean(IS_ONBOARDING_FINISHED)

        fun setOnBoardingFinished(isFinished: Boolean) {
            saveBoolean(IS_ONBOARDING_FINISHED, isFinished)
        }
    }
