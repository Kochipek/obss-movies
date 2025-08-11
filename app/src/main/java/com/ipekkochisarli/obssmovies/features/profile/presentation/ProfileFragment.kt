package com.ipekkochisarli.obssmovies.features.profile.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.core.base.BaseFragment
import com.ipekkochisarli.obssmovies.databinding.FragmentProfileBinding
import com.ipekkochisarli.obssmovies.features.profile.ProfileUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initListeners()
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state: ProfileUiState ->
                    binding.textViewEmail.text = state.email
                    binding.textViewUsername.text = state.username
                }
            }
        }
    }

    private fun navigateToLogin() {
        val navController = findNavController()
        navController.popBackStack(R.id.nav_graph, true)
        navController.navigate(R.id.loginFragment)
    }

    private fun initListeners() {
        binding.buttonLogout.setOnClickListener {
            viewModel.logout()
            navigateToLogin()
        }
    }
}
