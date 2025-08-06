package com.ipekkochisarli.obssmovies.features.login.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.core.base.BaseFragment
import com.ipekkochisarli.obssmovies.core.data.PreferencesManager
import com.ipekkochisarli.obssmovies.databinding.FragmentLoginBinding
import com.ipekkochisarli.obssmovies.util.extensions.gone
import com.ipekkochisarli.obssmovies.util.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var preferencesManager: PreferencesManager

    private var isLoginMode = true

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        if (shouldAutoLogin()) {
            navigateToHome()
            return
        }

        setupRememberMe()
        setupClickListeners()
        observeViewModel()
        updateUiForMode()
    }

    private fun shouldAutoLogin(): Boolean = preferencesManager.isRememberMeEnabled() && preferencesManager.isUserLoggedIn()

    private fun setupRememberMe() {
        if (preferencesManager.isRememberMeEnabled()) {
            binding.etEmail.setText(preferencesManager.getSavedEmail())
            binding.checkboxRememberMe.isChecked = true
        }
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            if (isLoginMode) onLoginClicked() else onRegisterClicked()
        }
        binding.tvSignUp.setOnClickListener {
            isLoginMode = !isLoginMode
            updateUiForMode()
        }
        binding.btnContinueAsGuest.setOnClickListener {
            viewModel.loginGuest()
        }
    }

    private fun updateUiForMode() =
        with(binding) {
            if (isLoginMode) {
                tvLoginTitle.setText(R.string.login)
                btnLogin.setText(R.string.login)
                tvSignUp.setText(R.string.sign_up)
                tilUsername.gone()
                checkboxRememberMe.visible()
                tvForgotPassword.visible()
            } else {
                tvNoAccount.gone()
                tvLoginTitle.setText(R.string.sign_up)
                btnLogin.setText(R.string.sign_up)
                tvSignUp.setText(R.string.back_to_login)
                tilUsername.visible()
                checkboxRememberMe.gone()
                tvForgotPassword.gone()
            }
        }

    private fun onLoginClicked() {
        val email =
            binding.etEmail.text
                .toString()
                .trim()
        val password =
            binding.etPassword.text
                .toString()
                .trim()

        if (!validateLoginFields(email, password)) return

        viewModel.loginUser(email, password)
    }

    private fun validateLoginFields(
        email: String,
        password: String,
    ): Boolean =
        if (email.isEmpty() || password.isEmpty()) {
            showToast(getString(R.string.login_error_fields_empty))
            false
        } else {
            true
        }

    private fun onRegisterClicked() {
        val email =
            binding.etEmail.text
                .toString()
                .trim()
        val password =
            binding.etPassword.text
                .toString()
                .trim()
        val username =
            binding.etUsername.text
                .toString()
                .trim()

        if (!validateRegisterFields(email, password, username)) return

        viewModel.registerUser(email, password, username)
    }

    private fun validateRegisterFields(
        email: String,
        password: String,
        username: String,
    ): Boolean =
        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            showToast(getString(R.string.register_error_fields_empty))
            false
        } else {
            true
        }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authState.collectLatest { state ->
                when {
                    state.isUserLoggedIn -> handleSuccessfulLogin()
                    state.isUserSignedUp -> {
                        isLoginMode = true
                        updateUiForMode()
                        showToast(getString(R.string.register_success))
                    }

                    state.isEmailAlreadyExists -> showToast(getString(R.string.email_already_exists))
                    else -> state.errorMessage?.let { showToast(it) }
                }
            }
        }
    }

    private fun handleSuccessfulLogin() =
        with(binding) {
            if (checkboxRememberMe.isChecked) {
                preferencesManager.setRememberMeEnabled(true)
                preferencesManager.saveEmail(etEmail.text.toString())
            } else {
                preferencesManager.setRememberMeEnabled(false)
                preferencesManager.saveEmail("")
            }

            preferencesManager.setUserLoggedIn(true)
            navigateToHome()
        }

    private fun navigateToHome() {
        findNavController().navigate(
            R.id.action_loginFragment_to_homeFragment,
            null,
            navOptions {
                popUpTo(R.id.loginFragment) {
                    inclusive = true
                }
            },
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
