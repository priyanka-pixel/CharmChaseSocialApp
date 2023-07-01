import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ForgotPassword(
    navController: NavController,
    viewModel: ForgotPasswordViewModel
) {
    val emailState = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val email = emailState.value
                viewModel.resetPassword(email)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Reset Password")
        }

        if (viewModel.successMessage.isNotEmpty()) {
            Text(
                text = viewModel.successMessage,
                modifier = Modifier.padding(16.dp),
                color = Color.Green
            )
        }

        if (viewModel.errorMessage.isNotEmpty()) {
            Text(
                text = viewModel.errorMessage,
                modifier = Modifier.padding(16.dp),
                color = Color.Red
            )
        }

        Text(
            text = "Remember your password? Log in",
            modifier = Modifier
                .padding(16.dp)
                .clickable { navController.navigate("login") },
            color = Color.Blue,
            textDecoration = TextDecoration.Underline
        )
    }
}
