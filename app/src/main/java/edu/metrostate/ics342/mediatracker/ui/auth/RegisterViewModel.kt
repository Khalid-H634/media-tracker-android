package edu.metrostate.ics342.mediatracker.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.metrostate.ics342.mediatracker.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _displayName = MutableStateFlow("")
    val displayName = _displayName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _username = MutableStateFlow("")
    val username = _username.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun setDisplayName(newValue: String) {
        _displayName.value = newValue
    }

    fun setEmail(newValue: String) {
        _email.value = newValue
    }

    fun setUsername(newValue: String) {
        _username.value = newValue
    }

    fun setPassword(newValue: String) {
        _password.value = newValue
    }

    fun setConfirmPassword(newValue: String) {
        _confirmPassword.value = newValue
    }

    fun onSignUpClicked(onSuccess: () -> Unit) {
        if (_displayName.value.isBlank() || _email.value.isBlank() || _username.value.isBlank() ||
            _password.value.isBlank() || _confirmPassword.value.isBlank()
        ) {
            _errorMessage.value = "Please fill in all fields."
            return
        }

        if (_password.value != _confirmPassword.value) {
            _errorMessage.value = "Passwords do not match."
            return
        }

        _errorMessage.value = ""
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val success = userRepository.createAccount(
                    displayName = _displayName.value,
                    username = _username.value,
                    email = _email.value,
                    password = _password.value
                )

                _isLoading.value = false

                if (success) {
                    onSuccess()
                } else {
                    _errorMessage.value = "Registration failed. Please try again."
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Error: ${e.message}"
                e.printStackTrace()
            }
        }
    }
}