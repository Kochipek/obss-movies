package com.ipekkochisarli.obssmovies.features.login.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.core.data.PreferencesManager
import com.ipekkochisarli.obssmovies.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupRememberMe()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRememberMe() {
        if (preferencesManager.isRememberMeEnabled()) {
            binding.etEmail.setText(preferencesManager.getSavedEmail())
            binding.checkboxRememberMe.isChecked = true
        }
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            onLoginClicked()
        }

        binding.btnContinueAsGuest.setOnClickListener {
            onContinueAsGuestClicked()
        }
    }

    private fun onLoginClicked() {
        val email =
            binding.etEmail.text
                ?.toString()
                ?.trim()
        val password =
            binding.etPassword.text
                ?.toString()
                ?.trim()

        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            showToast("Email ve şifre giriniz")
            return
        }

        viewModel.loginUser(email, password)
    }

    private fun onContinueAsGuestClicked() {
        viewModel.loginGuest()
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.authState.collectLatest { state ->
                if (state.isUserLoggedIn) {
                    handleSuccessfulLogin()
                }

                state.errorMessage?.let { showToast(it) }
            }
        }
    }

    private fun handleSuccessfulLogin() {
        if (binding.checkboxRememberMe.isChecked) {
            preferencesManager.setRememberMeEnabled(true)
            preferencesManager.saveEmail(binding.etEmail.text.toString())
        } else {
            preferencesManager.setRememberMeEnabled(false)
        }
        preferencesManager.setUserLoggedIn(true)

        val navOptions =
            navOptions {
                popUpTo(R.id.loginFragment) { inclusive = true }
            }

        findNavController().navigate(
            R.id.action_loginFragment_to_homeFragment,
            null,
            navOptions,
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
