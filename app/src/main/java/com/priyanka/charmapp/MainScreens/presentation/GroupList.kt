package com.priyanka.charmapp.MainScreens.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.core.GroupsRequest
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.Group
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants
import com.priyanka.charmapp.authentication.presentation.components.ProgressBar

@Composable
fun GroupListView() {
    val groupList = remember { mutableStateListOf<Group>() }
    val isLoading = remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        fetchGroups(groupList, isLoading)
    }

    if (isLoading.value) {
        ProgressBar()
        // Show loading indicator
        //CircularProgressIndicator(modifier = Modifier)
    } else {
        GroupList(groups = groupList) { group ->
            navigateToMessageList(group, context)
        }
    }
}

@SuppressLint("SuspiciousIndentation")
fun fetchGroups(groupList: MutableList<Group>, loading: MutableState<Boolean>) {
    val groupsRequest =  GroupsRequest.GroupsRequestBuilder().setLimit(10).build()

        try {
            groupsRequest.fetchNext(object : CometChat.CallbackListener<List<Group?>?>() {
                override fun onSuccess(groups: List<Group?>?) {
                    groups?.let {
                        groupList.addAll(it.filterNotNull())
                        loading.value = false
                    }
                }

                override fun onError(e: CometChatException) {
                    // Handle error
                    Log.e("onError: ", e.message ?: "")
                    loading.value = false
                }
            })
        } catch (e: CometChatException) {
            // Handle error
            Log.e("onError: ", e.message ?: "")
            loading.value = false
        }
    }



fun navigateToMessageList(group: Group, context: Context) {
    val intent = Intent(context, CometChatMessageListActivity::class.java).apply {
        putExtra(UIKitConstants.IntentStrings.NAME, group.name?:"")
        putExtra(UIKitConstants.IntentStrings.GROUP_OWNER, group.owner)
        putExtra(UIKitConstants.IntentStrings.GUID, group.guid)
        putExtra(UIKitConstants.IntentStrings.AVATAR, group.icon)
        putExtra(UIKitConstants.IntentStrings.GROUP_TYPE, group.groupType)
        putExtra(UIKitConstants.IntentStrings.TYPE, CometChatConstants.RECEIVER_TYPE_GROUP)
        putExtra(UIKitConstants.IntentStrings.MEMBER_COUNT, group.membersCount)
        putExtra(UIKitConstants.IntentStrings.GROUP_DESC, group.description)
        putExtra(UIKitConstants.IntentStrings.GROUP_PASSWORD, group.password)
    }
    context.startActivity(intent)
}

@Composable
fun GroupList(groups: List<Group>, onItemClick: (Group) -> Unit) {
    LazyColumn {
        items(groups) { group ->
            GroupListItem(group = group, onItemClick = onItemClick)
        }
    }
}

@Composable
fun GroupListItem(group: Group, onItemClick: (Group) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(group) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberImagePainter(data = group.icon),
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(48.dp)
                .clip(shape = CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = group.name,
            style = MaterialTheme.typography.body1
        )
    }
}


