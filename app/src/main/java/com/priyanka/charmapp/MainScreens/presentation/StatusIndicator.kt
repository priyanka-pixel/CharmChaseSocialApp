package com.priyanka.charmapp.mainscreens.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StatusIndicatorScreen() {
    val userStatus = remember { mutableStateOf("online") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CometChatUserPresence(userStatus.value)

        Row(
            modifier = Modifier.padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Status: ", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = userStatus.value == "online",
                onClick = { userStatus.value = "online" }
            )
            Text("Online")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = userStatus.value == "offline",
                onClick = { userStatus.value = "offline" }
            )
            Text("Offline")
        }
    }
}

@Composable
fun CometChatUserPresence(status: String) {
    Box(
        modifier = Modifier
            .size(200.dp)
            .background(
                if (status == "online") Color.Green else Color.Red,
                shape = CircleShape
            )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewStatusIndicatorScreen() {
    StatusIndicatorScreen()
}
