package com.ipekkochisarli.obssmovies.features.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.ipekkochisarli.obssmovies.core.network.ApiResult
import com.ipekkochisarli.obssmovies.features.login.domain.CheckEmailRegisteredUseCase
import com.ipekkochisarli.obssmovies.features.login.domain.LoginUserUseCase
import com.ipekkochisarli.obssmovies.features.login.domain.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val registerUseCase: RegisterUseCase,
        private val loginUserUseCase: LoginUserUseCase,
        private val checkEmailRegisteredUseCase: CheckEmailRegisteredUseCase,
    ) : ViewModel() {
        private val _authState = MutableStateFlow(LoginUiState())
        val authState = _authState.asStateFlow()

        private val firebaseAuth = FirebaseAuth.getInstance()

        fun loginUser(
            email: String,
            password: String,
        ) {
            viewModelScope.launch {
                _authState.update { it.copy(isLoading = true, errorMessage = null) }
                when (val result = loginUserUseCase(email, password)) {
                    is ApiResult.Success -> {
                        _authState.update {
                            it.copy(
                                isLoading = false,
                                isUserLoggedIn = true,
                            )
                        }
                    }

                    is ApiResult.Error -> {
                        _authState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.exception.message,
                            )
                        }
                    }
                }
            }
        }

        fun registerUser(
            email: String,
            password: String,
        ) {
            viewModelScope.launch {
                _authState.update {
                    it.copy(
                        isLoading = true,
                        errorMessage = null,
                        isEmailAlreadyExists = false,
                    )
                }

                when (val emailCheckResult = checkEmailRegisteredUseCase(email)) {
                    is ApiResult.Success -> {
                        if (emailCheckResult.data) {
                            // Email already exists
                            _authState.update {
                                it.copy(
                                    isLoading = false,
                                    isEmailAlreadyExists = true,
                                )
                            }
                        } else {
                            when (val registerResult = registerUseCase(email, password)) {
                                is ApiResult.Success -> {
                                    _authState.update {
                                        it.copy(
                                            isLoading = false,
                                            isUserSignedUp = true,
                                        )
                                    }
                                }

                                is ApiResult.Error -> {
                                    _authState.update {
                                        it.copy(
                                            isLoading = false,
                                            errorMessage = registerResult.exception.message,
                                        )
                                    }
                                }
                            }
                        }
                    }

                    is ApiResult.Error -> {
                        _authState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = emailCheckResult.exception.message,
                            )
                        }
                    }
                }
            }
        }

        fun loginGuest() {
            viewModelScope.launch {
                _authState.update { it.copy(isLoading = true, errorMessage = null) }
                try {
                    val result = firebaseAuth.signInAnonymously().await()
                    if (result.user != null) {
                        _authState.update {
                            it.copy(
                                isLoading = false,
                                isUserLoggedIn = true,
                            )
                        }
                    } else {
                        _authState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "Anonim giriş başarısız oldu",
                            )
                        }
                    }
                } catch (e: Exception) {
                    _authState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.localizedMessage ?: "Anonim girişte hata oluştu",
                        )
                    }
                }
            }
        }
    }
