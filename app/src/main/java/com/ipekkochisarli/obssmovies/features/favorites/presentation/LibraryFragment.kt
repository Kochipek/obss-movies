package com.ipekkochisarli.obssmovies.features.favorites.presentation

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.common.ErrorDialog
import com.ipekkochisarli.obssmovies.core.base.BaseFragment
import com.ipekkochisarli.obssmovies.core.data.PreferencesManager
import com.ipekkochisarli.obssmovies.databinding.FragmentLibraryBinding
import com.ipekkochisarli.obssmovies.features.favorites.presentation.adapter.LibraryPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LibraryFragment : BaseFragment<FragmentLibraryBinding>(FragmentLibraryBinding::inflate) {
    private lateinit var pagerAdapter: LibraryPagerAdapter

    @Inject
    lateinit var preferences: PreferencesManager

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        if (!preferences.isUserLoggedIn()) {
            ErrorDialog.show(
                parentFragmentManager,
                getString(R.string.login_required_message),
            ) {
                findNavController().navigate(R.id.loginFragment)
            }
            return
        }

        pagerAdapter = LibraryPagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text =
                when (position) {
                    0 -> getString(R.string.watched_movies)
                    1 -> getString(R.string.watch_later_movies)
                    else -> ""
                }
        }.attach()

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}
