package com.priyanka.charmapp.authentication.presentation

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.priyanka.charmapp.MainActivity
import com.priyanka.charmapp.authentication.presentation.components.ProgressBar
import com.priyanka.charmchase.core.AppDetails.authKEY

@Composable
fun SignUp(navController: NavController,
           onSignIn: (GoogleSignInAccount?, String?) -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(com.priyanka.charmapp.R.raw.arrow) )
    val composition2 by rememberLottieComposition(LottieCompositionSpec.RawRes(com.priyanka.charmapp.R.raw.symbol) )
    val composition3 by rememberLottieComposition(LottieCompositionSpec.RawRes(com.priyanka.charmapp.R.raw.googlelogo) )
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var ConfirmPassword by remember { mutableStateOf("") }
    val progressBarVisible = remember { mutableStateOf(false) }
    val primaryColor = Color.White
    val context = LocalContext.current
    val signInLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task, onSignIn)
        }

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
                .padding(start = 10.dp)
        ) {
            Image(painterResource(id = com.priyanka.charmapp.R.drawable.baseline_arrow_back_ios_24),
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        LottieAnimation(composition = composition2, modifier = Modifier
            .width(500.dp)
            .size(200.dp)
            .background(Color.Blue),
            isPlaying = true,
            restartOnPlay = true,
            iterations = 5)

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "SignUp an account",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = Color.Black,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Right
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = email, onValueChange = {
                email = it
            },
            placeholder = { Text(text = "Email Address") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = primaryColor
            ),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .width(350.dp)
                .height(60.dp)
            //.padding(30.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text(text = "Password") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = primaryColor
            ),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .width(350.dp)
                .height(60.dp)
            // .padding(20.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = ConfirmPassword,
            onValueChange = { ConfirmPassword = it },
            placeholder = { Text(text = "Confirm Password") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = primaryColor
            ),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .width(350.dp)
                .height(60.dp)
            // .padding(20.dp)
        )
        Button(
            onClick = {
                registerUserwithFirebase(email, password, context, onComplete = {}, onError = {})
            },
            shape = RoundedCornerShape(8.dp),
            enabled = email.isNotEmpty() && password.isNotEmpty() && ConfirmPassword.isNotEmpty(),
            modifier = Modifier
                .width(350.dp)
                .height(60.dp)
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = primaryColor,
                contentColor = Color.White
            ),

            ) {
            Text(text = "SIGNUP", color = Color.White)
        }
        Button(
            onClick = {
                signInLauncher.launch(getSignInIntent(context))
            },
            modifier = Modifier
                .width(350.dp)
                .height(60.dp)
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Black,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        )
        {
            LottieAnimation(composition = composition3, modifier = Modifier
                .width(500.dp)
                .size(200.dp)
                .background(Color.Blue),
                isPlaying = true,
                restartOnPlay = true,
                iterations = 5)

            Text(text = "Google Sign In")
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "By signing up, you acknowledge that you have read the ",
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            color = Color.Gray,
            fontFamily = FontFamily.Default
        )
        Row {
            Text(
                text = "Privacy Policy",
                color = primaryColor,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                modifier = Modifier.clickable(
                    onClick = {
                    })
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "and agree to our",
                color = Color.Gray,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
            )
            Text(
                text = "Terms of Service.",
                color = primaryColor,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                modifier = Modifier.clickable(
                    onClick = {
                    })
            )
        }
        Spacer(modifier = Modifier.width(5.dp))
        Row {
            Text(
                text = "Already have an account?",
                color = Color.Gray,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                fontFamily = FontFamily.Default
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "Login",
                color = primaryColor,
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                modifier = Modifier.clickable(
                    onClick = {
                        //login(user = User(),context)
                        context.startActivity(Intent(context, MainActivity::class.java))
                    }
                ))
        }
        if (progressBarVisible.value) {
            ProgressBar()
        }
    }
}
private fun registerUserwithFirebase(
    email: String,
    password: String,
    context: Context,
    onComplete: () -> Unit,
    onError: (String) -> Unit
) {
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onComplete()
                //context.startActivity(Intent(context, ProfileActivity::class.java))
                //task.result?.user?.uid?.let{onComplete()}
            } else {
                onError(task.exception?.message ?: "Error")
                //onComplete(false, task.exception?.message)
            }
        }
}
fun login(user: User, context: Context) {
    CometChat.login(user.getUid(), authKEY, object : CometChat.CallbackListener<User>() {
        override fun onSuccess(user: User) {
           // context.startActivity(Intent(context, ConversationActivity::class.java))
        }

        override fun onError(e: CometChatException) {
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    })
}


