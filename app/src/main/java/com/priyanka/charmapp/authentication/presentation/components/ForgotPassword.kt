import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.priyanka.charmapp.R

@Composable
fun ForgotPassword(
    navController: NavController,
    viewModel: ForgotPasswordViewModel
) {
    val emailState = remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(start = 0.dp)
        )
        {
            Image(
                painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.TopEnd,
                modifier = Modifier
                    .height(25.dp)
                    .width(25.dp)
                    .clickable { navController.navigateUp() })
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = emailState.value,
            onValueChange = {
                emailState.value = it
            },
            label = { Text(text = "Email Address", color = Color.Gray) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White
            ),
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .width(350.dp)
                .height(60.dp)
        )
//        TextField(
//            value = emailState.value,
//            onValueChange = { emailState.value = it },
//            label = { Text("Email") },
//            modifier = Modifier.fillMaxWidth()
//        )

        Button(
            onClick = {
                val email = emailState.value
                viewModel.resetPassword(email)
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .width(350.dp)
                .height(60.dp)
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor =Color.Black)
        ) {
            Text(text = "Reset Password", color = Color.White)
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
                .clickable {
                    val loginNav = "LoginScreen"
                    navController.navigate(loginNav)
                           },
            color = Color.Blue,
            textDecoration = TextDecoration.Underline
        )
    }
}
