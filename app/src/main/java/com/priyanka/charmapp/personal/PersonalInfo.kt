package com.priyanka.charmapp.personal

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.ktx.storage
import com.priyanka.charmapp.R
import com.priyanka.charmapp.authentication.data.UserProfile
import com.priyanka.charmapp.authentication.presentation.components.ProgressBar
import com.priyanka.charmchase.core.AppDetails.apiKey
import com.priyanka.charmchase.core.AppDetails.authKEY
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("NewApi")
@Composable
fun PersonalInfo(navController: NavController) {
    val primaryColor = Color.Black
    var imageUrl by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var seeking by remember { mutableStateOf("") }
    var aboutMe by remember { mutableStateOf("") }
    val isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var uri by remember { mutableStateOf<Uri?>(null) }

    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("MMM dd yyyy")
                .format(pickedDate)
        }
    }
    val dateDialogState = rememberMaterialDialogState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(start = 10.dp)
        ) {
            Image(painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.TopEnd,
                modifier = Modifier
                    .height(25.dp)
                    .width(25.dp)
                    .clickable { navController.navigateUp() })
            Spacer(modifier = Modifier.width(90.dp))
            Row {
                Text(
                    text = "Profile Info",
                    color = primaryColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                )
            }
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Profile picture
        ProfileImage(onImageUrlSelected = { selectedUrl ->
            imageUrl = selectedUrl
        }, onImageUriSelected = { selectedUri ->
            uri = selectedUri
        }, navController)
        // Date of Birth
        Spacer(modifier = Modifier.height(9.dp))
        Button(onClick = {
            dateDialogState.show()
        }) {
            Text(text = "Birth date")
        }
        Text(text = formattedDate)

        MaterialDialog(
            dialogState = dateDialogState,
            buttons = {
                positiveButton(text = "Ok") {
                    // Toast("clicked ok").show()
                }
                negativeButton(text = "Cancel")
            }
        ) {
            datepicker(
                initialDate = LocalDate.now(),
                title = "Pick a date",
//            allowedDateValidator = {
//                it.dayOfMonth % 2 == 1
//            }
            ) {
                pickedDate = it
            }
        }
        // First name
        Spacer(modifier = Modifier.height(9.dp))
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            placeholder = { Text(text = "First Name") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = primaryColor
            ),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .width(350.dp)
                .height(60.dp)
        )
        Spacer(modifier = Modifier.height(9.dp))
        // Last name
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            placeholder = { Text(text = "Last Name") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = primaryColor
            ),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .width(350.dp)
                .height(60.dp)
        )
        Spacer(modifier = Modifier.height(9.dp))
        // Seeking
        OutlinedTextField(
            value = seeking,
            onValueChange = { seeking = it },
            placeholder = { Text(text = "Seeking") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = primaryColor
            ),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .width(350.dp)
                .height(60.dp)
        )
        Spacer(modifier = Modifier.height(9.dp))
        // About me
        OutlinedTextField(
            value = aboutMe,
            onValueChange = { aboutMe = it },
            placeholder = { Text(text = "About Me") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = primaryColor
            ),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .width(350.dp)
                .height(100.dp)
        )
        if (isLoading) {
            ProgressBar()
        }
        RequestPermission(navController, permission = "", rationaleMessage = "")
        Spacer(modifier = Modifier.height(9.dp))
        Button(
            onClick = {
                val uid = Firebase.auth.uid?.lowercase()
                val imageBitmap = uri?.let { loadBitmapFromUri(context, uri = it) }
                uid?.let {
                    if (imageBitmap != null) {
                        uploadFile(navController,
                            uid,
                            imageBitmap = imageBitmap,
                            userProfile = UserProfile(
                                imageUrl,
                                firstName,
                                lastName,
                                seeking,
                                aboutMe,
                                formattedDate
                            ),
                            onComplete = {
                                profileUserwithCometChat(
                                    uid = uid,
                                    firstname = firstName,
                                    navController,
                                    imageurl = imageUrl,
                                    onRegisterError = {},
                                    onRegisterSuccess = {
                                        val MainScreen = "MainScreen"
                                        navController.navigate(MainScreen)
                                    }
                                )
                            }) {}
                    }
                }
            },
            shape = RoundedCornerShape(8.dp),
            enabled = firstName.isNotEmpty() && lastName.isNotEmpty() && seeking.isNotEmpty() && aboutMe.isNotEmpty(),
            modifier = Modifier
                .width(350.dp)
                .height(60.dp)
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = primaryColor, contentColor = Color.White
            ),
        ) {
            Text(text = "Looks Great!", color = Color.White)
        }
    }

}
fun uploadFile(
   navController: NavController,
    uid: String,
    imageBitmap: Bitmap?,
    userProfile: UserProfile,
    onComplete: (Any?) -> Unit,
    onError: (String) -> Unit
) {
    val storage = Firebase.storage.reference
    val fileRef = storage.child("profiles/$uid/$uid.jpeg")
    val metaData = StorageMetadata.Builder().setContentType("image/jpeg").build()
    val baos = ByteArrayOutputStream()
    imageBitmap?.compress(Bitmap.CompressFormat.JPEG, 80, baos)
    val data = baos.toByteArray()

    val uploadTask = fileRef.putBytes(data, metaData)
    uploadTask.addOnSuccessListener { taskSnapshot_ ->
        fileRef.downloadUrl.addOnSuccessListener { downloadUrl ->
            val imageUrl = downloadUrl.toString()
            val firestore = Firebase.firestore
            val user = mapOf(
                "imageUrl" to imageUrl,
                "displayName" to userProfile.displayName,
                "firstName" to userProfile.firstName,
                "lastName" to userProfile.lastName,
                "seeking" to userProfile.seeking,
                "aboutMe" to userProfile.aboutMe,
                "formattedDate" to userProfile.formattedDate
            )
            firestore.collection("users").document(uid).set(user).addOnSuccessListener {
                // Handle successful Firestore update
                onComplete{}
                profileUserwithCometChat(uid, userProfile.displayName, navController,imageUrl,onComplete, onRegisterSuccess ={val MainScreen = "MainScreen"
                    navController.navigate(MainScreen)})

            }.addOnFailureListener { exception ->
                // Handle Firestore update failure
                Log.e("Firestore Error", "Failed to update user data: ${exception.message}")
            }
        }
    }.addOnFailureListener { exception ->
        // Handle upload failure
        onError("")
        Log.e("Storage Error", "Failed to upload image: ${exception.message}")
    }
}

fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


private fun profileUserwithCometChat(
    uid: String,
    firstname: String,
    navController: NavController,
    imageurl: String,
    onRegisterError: (String) -> Unit,
    onRegisterSuccess: (Any?) -> Unit,
) {
    val user = User()
    user.uid = uid.lowercase()
    user.name = firstname
    user.avatar = imageurl
    CometChat.createUser(user, apiKey, object : CometChat.CallbackListener<User>() {
        override fun onSuccess(user: User) {
              onRegisterSuccess{loginWithCometChat(uid)}
            val MainScreen = "MainScreen"
            navController.navigate(MainScreen)

        }
        override fun onError(e: CometChatException) {
            e.message?.let { onRegisterError(it) }
            print("error")
        }
    })
}

private fun loginWithCometChat(
    uid: String,
) {
    CometChat.login(authKEY,uid,object :CometChat.CallbackListener<User>(){
        override fun onSuccess(p0: User?) {
           // context.startActivity(Intent(context, ConversationActivity::class.java))
            Log.d(TAG, "Login Successful : " + p0?.toString())
        }

        override fun onError(p0: CometChatException?) {
            Log.d(TAG, "Login failed with exception: " +  p0?.message)
        }
    })
}