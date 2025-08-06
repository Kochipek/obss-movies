package com.ipekkochisarli.obssmovies.features.onboarding.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.viewpager2.widget.ViewPager2
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.core.base.BaseFragment
import com.ipekkochisarli.obssmovies.core.data.PreferencesManager
import com.ipekkochisarli.obssmovies.databinding.FragmentOnboardingBinding
import com.ipekkochisarli.obssmovies.features.onboarding.adapter.OnBoardingPagerAdapter
import com.ipekkochisarli.obssmovies.features.onboarding.uimodel.OnBoardingUiModel
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingFragment : BaseFragment<FragmentOnboardingBinding>(FragmentOnboardingBinding::inflate) {
    private lateinit var onboardingAdapter: OnBoardingPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var dotsIndicator: DotsIndicator
    private val viewModel: OnBoardingViewModel by viewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        setupOnboardingAdapter()

        if (viewModel.isOnboardingFinished()) {
            navigateToLoginScreen()
            return
        }
    }

    private fun initializeViews() {
        viewPager = binding.viewPagerOnboarding
        dotsIndicator = binding.dotsIndicator
    }

    private fun setupOnboardingAdapter() {
        val onboardingPages =
            listOf(
                OnBoardingUiModel(
                    titleResId = R.string.onboarding_title_1,
                    descriptionResId = R.string.onboarding_description_1,
                    imageResId = R.drawable.onboarding_poster_1,
                ),
                OnBoardingUiModel(
                    titleResId = R.string.onboarding_title_2,
                    descriptionResId = R.string.onboarding_description_2,
                    imageResId = R.drawable.onboarding_poster_2,
                ),
                OnBoardingUiModel(
                    titleResId = R.string.onboarding_title_3,
                    descriptionResId = R.string.onboarding_description_3,
                    imageResId = R.drawable.onboarding_poster_3,
                ),
            )

        onboardingAdapter =
            OnBoardingPagerAdapter(onboardingPages) {
                handleNextButtonClick()
            }

        viewPager.adapter = onboardingAdapter
        dotsIndicator.attachTo(viewPager)
    }

    private fun handleNextButtonClick() {
        val currentItem = viewPager.currentItem
        val lastIndex = onboardingAdapter.itemCount - 1

        if (currentItem == lastIndex) {
            markOnBoardingAsFinished()
            navigateToLoginScreen()
        } else {
            viewPager.currentItem = currentItem + 1
        }
    }

    private fun markOnBoardingAsFinished() {
        viewModel.setOnboardingFinished()
    }

    private fun navigateToLoginScreen() {
        findNavController().navigate(
            R.id.action_onBoardingFragment_to_loginFragment,
            null,
            navOptions {
                popUpTo(R.id.onBoardingFragment) {
                    inclusive = true
                }
            },
        )
    }
}
