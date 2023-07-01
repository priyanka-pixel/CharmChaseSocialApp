import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewModel : ViewModel() {
    private val _successMessage = mutableStateOf("")
    val successMessage: String
        get() = _successMessage.value

    private val _errorMessage = mutableStateOf("")
    val errorMessage: String
        get() = _errorMessage.value

    fun resetPassword(email: String) {
        if (email.isNotBlank() && email.contains("@")) {
            // Call your authentication provider's reset password method
            // For example, using FirebaseAuth:
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _successMessage.value = "Check your email for further instructions."
                        _errorMessage.value = ""
                    } else {
                        _successMessage.value = ""
                        _errorMessage.value = task.exception?.message ?: "Failed to reset password"
                    }
                }
        } else {
            _successMessage.value = ""
            _errorMessage.value = "Invalid email address"
        }
    }
}
