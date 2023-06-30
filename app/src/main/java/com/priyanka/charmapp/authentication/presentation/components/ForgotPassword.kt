package com.priyanka.charmapp.authentication.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ForgotPassword(
    navController: NavController,
    viewModel: ForgotPasswordViewModel,
    onResetPassword: () -> Unit
) {
    val emailState = remember { mutableStateOf("") }
    val email = emailState.value

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { emailState.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()

        )

        Button(
            onClick = { viewModel.resetPassword() },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Reset Password")
        }

        Text(
            text = "Check your email for further instructions.",
            modifier = Modifier
                .padding(16.dp)
                .alpha(viewModel.successMessageAlpha),
            color = Color.Green
        )

        Text(
            text = viewModel.errorMessage,
            modifier = Modifier
                .padding(16.dp)
                .alpha(viewModel.errorMessageAlpha),
            color = Color.Red
        )

        Text(
            text = "Remember your password? Log in",
            modifier = Modifier
                .padding(16.dp)
                .clickable { onResetPassword() },
            color = Color.Blue,
            textDecoration = TextDecoration.Underline
        )
    }
}
