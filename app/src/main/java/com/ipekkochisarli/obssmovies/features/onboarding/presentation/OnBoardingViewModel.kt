package com.ipekkochisarli.obssmovies.features.onboarding.presentation

import androidx.lifecycle.ViewModel
import com.ipekkochisarli.obssmovies.core.data.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel
    @Inject
    constructor(
        private val preferences: PreferencesManager,
    ) : ViewModel() {
        fun isOnboardingFinished() = preferences.isOnBoardingFinished()

        fun setOnboardingFinished() = preferences.setOnBoardingFinished(true)
    }
