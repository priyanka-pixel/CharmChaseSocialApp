package com.priyanka.charmapp.MainScreens.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter

@Composable
fun More(navController: NavController,
    profileImageUrl: String,
    onProfileImageClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAboutUsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile image
        Image(
            painter = rememberImagePainter(data = profileImageUrl),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(200.dp)
                .clip(shape = CircleShape)
                .clickable { onProfileImageClick() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Settings button
        Button(onClick = { onSettingsClick() }) {
            Text(text = "Settings")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // About Us button
        Button(onClick = { onAboutUsClick() }) {
            Text(text = "About Us")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Logout button
        Button(onClick = { onLogoutClick() }) {
            Text(text = "Logout")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Delete Account button
        Button(onClick = { onDeleteAccountClick() }) {
            Text(text = "Delete Account")
        }
    }
}
