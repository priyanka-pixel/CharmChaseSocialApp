package com.priyanka.charmapp.personal

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.runtime.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.priyanka.charmapp.authentication.data.UserProfile

@Composable
fun ProfileImage(
    onImageUrlSelected: (String) -> Unit, onImageUriSelected: (Uri) -> Unit,
    navController: NavController
) {
    var imageView by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val painter = rememberImagePainter(data = imageView, builder = {
      //  placeholder(R.)
        crossfade(true)

    })
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        uri?.let {
            imageView = it
            onImageUriSelected(it)
        }
    }
    Column(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .padding(top = 64.dp)
                .clip(CircleShape)
                .width(128.dp)
                .height(128.dp)
                .background(Color.Gray)
                .clickable {
                    launcher.launch("image/*")
                },
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )
    }
    // This effect will be triggered whenever `imageView` changes
    LaunchedEffect(imageView) {
        imageView?.let { uri ->

            val selectedImageUrl = uri.toString()
            // Call the callback function with the selected image URL
            onImageUrlSelected(selectedImageUrl)
            val imageBitmap = loadBitmapFromUri(context, uri)
            // Upload the bitmap to Firebase Storage
            if (imageBitmap != null) {
                val uid = Firebase.auth.uid?.lowercase()
                uid?.let {

                    uploadFile(navController,it,
                        imageBitmap,
                        userProfile = UserProfile(),
                        onComplete = {

                        }
                    ) {}
                }
            }
        }
    }
}
