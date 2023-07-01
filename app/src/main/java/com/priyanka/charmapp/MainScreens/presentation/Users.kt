package com.priyanka.charmapp.MainScreens.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.core.UsersRequest
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants

@Composable
fun Users() {
    val userList = remember { mutableStateListOf<User>() }
    val isLoading = remember { mutableStateOf(true) }
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        fetchUsers(userList, isLoading)
    }

    if (isLoading.value) {
        // Show loading indicator
        CircularProgressIndicator(modifier = Modifier)
    } else {
        UserList(users = userList) { user ->
            navigateToMessageList(user, context)
        }
    }
}

fun fetchUsers(userList: MutableList<User>, loading: MutableState<Boolean>) {
    val usersRequest = UsersRequest.UsersRequestBuilder().setLimit(30).build()
    usersRequest.fetchNext(object : CometChat.CallbackListener<List<User>>() {
        override fun onSuccess(users: List<User>?) {
            users?.let {
                userList.addAll(users)
                loading.value = false
            }
        }

        override fun onError(e: CometChatException) {
            // Handle error
        }
    })


}

fun navigateToMessageList(user: User, context: Context) {
    val intent = Intent(context, CometChatMessageListActivity::class.java).apply {
        putExtra(UIKitConstants.IntentStrings.TYPE, CometChatConstants.RECEIVER_TYPE_USER)
        putExtra(UIKitConstants.IntentStrings.NAME, user.name)
        putExtra(UIKitConstants.IntentStrings.UID, user.uid)
        putExtra(UIKitConstants.IntentStrings.AVATAR, user.avatar)
        putExtra(UIKitConstants.IntentStrings.STATUS, user.status)
    }
    context.startActivity(intent)
}

@Composable
fun UserList(users: List<User>, onItemClick: (User) -> Unit) {
    LazyColumn {
        items(users) { user ->
            UserListItem(user = user, onItemClick = onItemClick)
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UserListItem(user: User, onItemClick: (User) -> Unit) {
    var searchText by remember { mutableStateOf("") }
            Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(user) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberImagePainter(data = user.avatar),
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(48.dp)
                .clip(shape = CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = user.name,
            style = MaterialTheme.typography.body1
        )
    }
}
