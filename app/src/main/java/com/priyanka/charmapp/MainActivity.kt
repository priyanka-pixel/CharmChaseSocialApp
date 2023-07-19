package com.priyanka.charmapp

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.cometchat.pro.core.AppSettings
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.priyanka.charmapp.authentication.presentation.LoginScreen
import com.priyanka.charmapp.navigation.Navigation
import com.priyanka.charmapp.ui.theme.CharmAppTheme
import com.priyanka.charmchase.core.AppDetails
import com.priyanka.charmchase.core.AppDetails.appID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
   // private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        //initialize with firebase
       // auth = FirebaseAuth.getInstance()


        //initialize with cometchat
        val appSettings =
            AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers()
                .setRegion(AppDetails.region)
                .build()
        CometChat.init(this, appID, appSettings, object : CometChat.CallbackListener<String>() {
            override fun onSuccess(successMessage: String) {
                Log.d(ContentValues.TAG, "Initialization completed successfully")
                CometChat.setSource("ui-kit","kotlin","hello")
            }

            override fun onError(e: CometChatException) {
                Log.d(ContentValues.TAG, "Initialization failed with exception: " + e.message)
            }
        })


        setContent {
            CharmAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //SplashScreen(navController = NavController(this))
                    Navigation {}

                }
            }
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.symbol) )
    CharmAppTheme  {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            LottieAnimation(composition = composition, modifier = Modifier
                .size(400.dp)
                .background(Color.Black),
            isPlaying = true,
            restartOnPlay = true,
            iterations = 5)
            
            Spacer(modifier = Modifier.height(150.dp))
                Button(
                    onClick = {
                        val loginNav = "LoginScreen"
                        navController.navigate(loginNav)
                    },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .width(160.dp)
                        .padding(top = 30.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)

                ) {
                    Text(text = "LOGIN", color = Color.Red)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = {
                        val SignUp = "SignUp"
                        navController.navigate(SignUp)

                    },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .width(160.dp)
                        .padding(top = 30.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                ) {
                    Text(text = "SIGNUP", color = Color.Red)
                }
            }

        }

    }

