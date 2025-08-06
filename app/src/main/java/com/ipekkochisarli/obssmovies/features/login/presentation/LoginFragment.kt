package com.ipekkochisarli.obssmovies.features.login.presentation

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.ipekkochisarli.obssmovies.R
import com.ipekkochisarli.obssmovies.core.base.BaseFragment
import com.ipekkochisarli.obssmovies.databinding.FragmentLoginBinding
import com.ipekkochisarli.obssmovies.features.login.GoogleAuthClient
import com.ipekkochisarli.obssmovies.util.extensions.gone
import com.ipekkochisarli.obssmovies.util.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val viewModel: LoginViewModel by viewModels()

    private lateinit var googleAuthClient: GoogleAuthClient
    private var isLoginMode = true

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                googleAuthClient.handleSignInResult(result.data)
            } else {
                Toast
                    .makeText(requireContext(), "Google Sign-In canceled", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.shouldAutoLogin()) {
            navigateToHome()
            return
        }

        val clientId = getString(R.string.default_web_client_id)
        googleAuthClient =
            GoogleAuthClient(
                requireActivity(),
                clientId,
                googleSignInLauncher,
                onSuccess = { idToken -> viewModel.loginWithGoogle(idToken) },
                onError = { e ->
                    Toast
                        .makeText(
                            requireContext(),
                            "Google Sign-In Error: ${e.message}",
                            Toast.LENGTH_SHORT,
                        ).show()
                },
            )

        binding.btnGoogleLogin.setOnClickListener {
            googleAuthClient.signIn()
        }

        observeViewModel()
        setupClickListeners()
        observeViewModel()
        updateUiForMode()
    }

    private fun setupClickListeners() =
        with(binding) {
            btnLogin.setOnClickListener {
                if (isLoginMode) onLoginClicked() else onRegisterClicked()
            }
            tvSignUp.setOnClickListener {
                isLoginMode = !isLoginMode
                updateUiForMode()
            }
            btnContinueAsGuest.setOnClickListener {
                viewModel.loginGuest()
            }
            checkboxRememberMe.setOnCheckedChangeListener { _, isChecked ->
                viewModel.setRememberMeChecked(isChecked)
            }
            etEmail.doAfterTextChanged { text ->
                viewModel.onEmailChanged(text.toString())
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

    private fun validateLoginFields(
        email: String,
        password: String,
    ) = if (email.isEmpty() || password.isEmpty()) {
        showToast(getString(R.string.login_error_fields_empty))
        false
    } else {
        true
    }

    private fun validateRegisterFields(
        email: String,
        password: String,
        username: String,
    ) = if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
        showToast(getString(R.string.register_error_fields_empty))
        false
    } else {
        true
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authState.collectLatest { state ->

                binding.checkboxRememberMe.isChecked = state.isRememberMeEnabled
                if (binding.etEmail.text.toString() != state.savedEmail) {
                    binding.etEmail.setText(state.savedEmail)
                }

                when {
                    state.isUserLoggedIn -> navigateToHome()
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

    private fun navigateToHome() {
        findNavController().navigate(
            R.id.homeFragment,
            null,
            navOptions {
                popUpTo(R.id.loginFragment) { inclusive = true }
            },
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
