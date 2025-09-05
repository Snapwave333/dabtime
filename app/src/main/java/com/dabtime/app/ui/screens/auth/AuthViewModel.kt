package com.dabtime.app.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dabtime.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    init {
        checkAuthState()
    }
    
    private fun checkAuthState() {
        viewModelScope.launch {
            val isAuthenticated = authRepository.isUserAuthenticated()
            _uiState.value = _uiState.value.copy(isAuthenticated = isAuthenticated)
        }
    }
    
    fun signInWithGoogle() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                loadingMethod = "google",
                errorMessage = null
            )
            
            try {
                val result = authRepository.signInWithGoogle()
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        loadingMethod = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        loadingMethod = null,
                        errorMessage = result.exceptionOrNull()?.message ?: "Google sign-in failed"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loadingMethod = null,
                    errorMessage = e.message ?: "Google sign-in failed"
                )
            }
        }
    }
    
    fun signInWithFacebook() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                loadingMethod = "facebook",
                errorMessage = null
            )
            
            try {
                val result = authRepository.signInWithFacebook()
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        loadingMethod = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        loadingMethod = null,
                        errorMessage = result.exceptionOrNull()?.message ?: "Facebook sign-in failed"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loadingMethod = null,
                    errorMessage = e.message ?: "Facebook sign-in failed"
                )
            }
        }
    }
    
    fun signInWithEmail() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                loadingMethod = "email",
                errorMessage = null
            )
            
            try {
                val result = authRepository.signInWithEmail()
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        loadingMethod = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        loadingMethod = null,
                        errorMessage = result.exceptionOrNull()?.message ?: "Email sign-in failed"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loadingMethod = null,
                    errorMessage = e.message ?: "Email sign-in failed"
                )
            }
        }
    }
    
    fun signInAnonymously() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                loadingMethod = "anonymous",
                errorMessage = null
            )
            
            try {
                val result = authRepository.signInAnonymously()
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        loadingMethod = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        loadingMethod = null,
                        errorMessage = result.exceptionOrNull()?.message ?: "Anonymous sign-in failed"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loadingMethod = null,
                    errorMessage = e.message ?: "Anonymous sign-in failed"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val loadingMethod: String? = null,
    val errorMessage: String? = null
)