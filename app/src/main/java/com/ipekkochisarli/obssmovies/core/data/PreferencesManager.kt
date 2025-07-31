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
        }

        private fun saveBoolean(
            key: String,
            value: Boolean,
        ) {
            sharedPreferences.edit { putBoolean(key, value) }
        }

        private fun Boolean.getBoolean(key: String): Boolean = sharedPreferences.getBoolean(key, this)

        // Onboarding
        fun isOnBoardingFinished(): Boolean = false.getBoolean(IS_ONBOARDING_FINISHED)

        fun setOnBoardingFinished(isFinished: Boolean) {
            saveBoolean(IS_ONBOARDING_FINISHED, isFinished)
        }
    }
