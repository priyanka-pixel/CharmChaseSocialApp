package com.priyanka.charmapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.priyanka.charmapp.SplashScreen
import com.priyanka.charmapp.authentication.presentation.LoginScreen
import com.priyanka.charmapp.authentication.presentation.SignUp
import com.priyanka.charmapp.authentication.presentation.components.ForgotPassword
import com.priyanka.charmapp.authentication.presentation.components.ForgotPasswordViewModel
import com.priyanka.charmapp.authentication.presentation.components.forgotPassword
import com.priyanka.charmapp.personal.PersonalInfo

@Composable
fun Navigation(toggleTheme: () -> Unit) {
    val onBoardingNav = "onBoardingScreen"
    val loginNav = "LoginScreen"
    val signup = "Signup"
    val Forgotpassword = "forgotPassword"
    val personalInfo = "personalInfo"
    val context = LocalContext.current
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = onBoardingNav) {

        composable(route = onBoardingNav) {
            SplashScreen(navController = navController)
        }
        composable(route =loginNav) {
            LoginScreen(navController = navController, onSignIn = { account, token ->
                // Do something with the account and token
                if (account != null && token != null) {
                    // Navigate to the main page if the account and token are not null
                    //context.startActivity(Intent(context, ConversationActivity::class.java))
                }
            } )
        }
        composable(route = signup){
            SignUp(navController = navController, onSignIn = { account, token ->
                // Do something with the account and token
                if (account != null && token != null) {
                    // Navigate to the main page if the account and token are not null
                    //context.startActivity(Intent(context, ConversationActivity::class.java))
                }
            } )
        }
        composable(route = Forgotpassword){
            ForgotPassword(navController, viewModel = ForgotPasswordViewModel(), onResetPassword = {})
        }
        composable(route = personalInfo){
            PersonalInfo(navController)
    }
}}