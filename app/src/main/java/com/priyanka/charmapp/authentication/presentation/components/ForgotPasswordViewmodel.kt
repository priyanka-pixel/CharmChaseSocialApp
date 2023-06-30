package com.priyanka.charmapp.authentication.presentation.components

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewModel : ViewModel() {
    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _successMessageAlpha = mutableStateOf(0f)
    val successMessageAlpha: State<Float> = _successMessageAlpha

    private val _errorMessageAlpha = mutableStateOf(0f)
    val errorMessageAlpha: State<Float> = _errorMessageAlpha

    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> = _errorMessage

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun resetPassword() {
        val emailValue = email.value

        if (emailValue.isNotBlank() && emailValue.contains("@")) {
            // Call your authentication provider's reset password method
            // For example, using FirebaseAuth:
            FirebaseAuth.getInstance().sendPasswordResetEmail(emailValue)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _successMessageAlpha.value = 1f
                        _errorMessageAlpha.value = 0f
                        _errorMessage.value = ""
                    } else {
                        _successMessageAlpha.value = 0f
                        _errorMessageAlpha.value = 1f
                        _errorMessage.value = task.exception?.message ?: "Failed to reset password"
                    }
                }
        } else {
            _successMessageAlpha.value = 0f
            _errorMessageAlpha.value = 1f
            _errorMessage.value = "Invalid email address"
        }
    }
}
