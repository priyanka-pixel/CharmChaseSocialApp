package com.priyanka.charmapp.authentication.presentation

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
import com.cometchat.pro.core.CometChat.login
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.priyanka.charmapp.R
import com.priyanka.charmchase.core.AppDetails.apiKey

@Composable
fun LoginScreen(
    navController: NavController,
    onSignIn: (GoogleSignInAccount?, String?) -> Unit
) {
    val composition2 by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.symbol) )
    val composition3 by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.googlelogo) )
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val progressBarVisible = remember { mutableStateOf(false) }
    val signInLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task, onSignIn)
        }
    //Login Ui
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
            Image(painterResource(id = R.drawable.baseline_arrow_back_ios_24),
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
        modifier = Modifier
            .fillMaxWidth()
    )
    {
        Spacer(modifier = Modifier.height(50.dp))
        LottieAnimation(composition = composition2, modifier = Modifier
            .size(200.dp)
            .background(Color.White),
            isPlaying = true,
            restartOnPlay = true,
            iterations = 5)

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Hello Again!",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = Color.White,
            fontFamily = FontFamily.Default,
            textAlign = TextAlign.Right
        )
        Text(
            text = "Welcome back in your World",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Gray,
            fontFamily = FontFamily.Default
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
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
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password", color = Color.Gray) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White
            ),
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .width(350.dp)
                .height(60.dp)

        )
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Spacer(modifier = Modifier.width(200.dp))
            Text(text = "Forgot password?",
                color = Color.Red,
                textAlign = TextAlign.End,
                modifier = Modifier.clickable(
                    onClick = {
                        val ForgotPassword = "forgotPassword"
                        navController.navigate(ForgotPassword)
                    }
                ))
        }
        Button(
            onClick = {
                progressBarVisible.value = true
                login(navController, email, password, onLoginError = {"Error"}, onLoginSuccess = {

                })
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .width(350.dp)
                .height(60.dp)
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor =Color.Black)

        ) {
            Text(text = "LOGIN", color = Color.White)
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
            shape = RoundedCornerShape(20.dp)
        )
        {
            LottieAnimation(composition = composition3, modifier = Modifier.size(50.dp)
                .background(Color.Black),
                isPlaying = true,
                restartOnPlay = true,
                iterations = 5)

            Text(text = "Google Sign In", color = Color.White)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row {
            Text(text = "Not a member?")
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "Register Now",
                color = Color.Blue,
                textAlign = TextAlign.End,
                modifier = Modifier.clickable(
                    onClick = {
                        val SignUp = "signup"
                        navController.navigate(SignUp)
                    }
                ))
        }

        if (progressBarVisible.value) {
            CircularProgressIndicator()
        }
    }
}

//google sign in
fun getSignInIntent(context: Context): Intent {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.google_client_id))
        .requestEmail()
        .build()
    val signInClient = GoogleSignIn.getClient(context, gso)
    return signInClient.signInIntent
}

fun handleSignInResult(
    task: Task<GoogleSignInAccount>,
    onSignIn: (GoogleSignInAccount?, String?) -> Unit
) {
    try {
        val account = task.getResult(ApiException::class.java)
        val idToken = account?.idToken
        onSignIn(account, idToken)
    } catch (e: ApiException) {
        onSignIn(null, null)
    }
}
private fun loginWithCometChatId(
    navController: NavController,
    uid: String,
    onLoginSuccess: (Any?) -> Unit,
    onLoginError: (String) -> Unit
) {
    CometChat.login(uid, apiKey, object : CometChat.CallbackListener<User>() {
        override fun onSuccess(user: User) {
            onLoginSuccess{}
            val MainScreen = "MainScreen"
            navController.navigate(MainScreen)

        }
        override fun onError(e: CometChatException) {
            e.message?.let { onLoginError(it) }
            print("Error")
        }
    })
}

//login with firebase
private fun performLogin(
    navController: NavController,
    email: String,
    password: String,
    onLoginSuccess: (String) -> Unit,
    onLoginError: (String) -> Unit
) {
    val auth = Firebase.auth
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val MainScreen = "MainScreen"
                navController.navigate(MainScreen)
                task.result?.user?.uid?.let { onLoginSuccess(it) }
            } else {
                println("error")
                onLoginError(task.exception?.message ?: "Unknown error")
            }
        }
}

//login with both firebase and cometchat
private fun login(
    navController: NavController,
    email: String,
    password: String,
    onLoginSuccess: (Any?) -> Unit,
    onLoginError: (String) -> Unit
) {
    performLogin(navController, email, password, onLoginSuccess = { uid ->
        loginWithCometChatId(navController, uid, onLoginSuccess, onLoginError)
    }) { errorMessage ->
        print(errorMessage)
    }
}