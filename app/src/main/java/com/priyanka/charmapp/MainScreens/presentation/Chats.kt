package com.priyanka.charmapp.MainScreens.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.core.ConversationsRequest
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.Conversation
import com.cometchat.pro.models.Group
import com.cometchat.pro.models.User
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants
import java.util.*


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Chats(navController: NavController) {
    val conversationList = remember { mutableStateListOf<Conversation>() }
    val isLoading = remember { mutableStateOf(true) }
    val context = LocalContext.current

    fetchConversations(conversationList, isLoading)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Chats") },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            )
        },
        content = {
            Column {
                SearchBar(modifier = Modifier.padding(16.dp))
                if (isLoading.value) {
                    // Show loading indicator
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                } else {
                    ConversationList(conversations = conversationList) { conversation ->
                        navigateToMessageList(conversation, context)
                    }
                }
            }
        }
    )
}

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier.padding(start = 8.dp, end = 4.dp)
            )
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.Black),
                placeholder = {
                    Text(text = "Search", color = Color.Gray)
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
    }
}


fun fetchConversations(conversationList: MutableList<Conversation>, loading: MutableState<Boolean>) {
    val conversationsRequest = ConversationsRequest.ConversationsRequestBuilder().setLimit(30).build()

    conversationsRequest.fetchNext(object : CometChat.CallbackListener<List<Conversation>>() {
        override fun onSuccess(conversations: List<Conversation>) {
            conversationList.addAll(conversations)
            loading.value = false
        }

        override fun onError(e: CometChatException) {
            // Handle error
            Log.e("onError: ", e.message ?: "")
            loading.value = false
        }
    })
}

@Composable
fun ConversationList(conversations: List<Conversation>, onItemClick: (Conversation) -> Unit) {
    LazyColumn {
        items(conversations) { conversation ->
            ConversationListItem(conversation = conversation, onItemClick = onItemClick)
            Divider(color = Color.LightGray, thickness = 1.dp)
        }
    }
}

@Composable
fun ConversationListItem(conversation: Conversation, onItemClick: (Conversation) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(conversation) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val avatar = when (conversation.conversationType) {
            CometChatConstants.CONVERSATION_TYPE_GROUP -> {
                val group = conversation.conversationWith as? Group
                group?.icon
            }
            CometChatConstants.CONVERSATION_TYPE_USER -> {
                val user = conversation.conversationWith as? User
                user?.avatar
            }
            else -> null
        }
        Image(
            painter = rememberImagePainter(data = avatar),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(48.dp)
                .clip(shape = CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))
        val name = when (conversation.conversationType) {
            CometChatConstants.CONVERSATION_TYPE_GROUP -> {
                val group = conversation.conversationWith as? Group
                group?.name
            }
            CometChatConstants.CONVERSATION_TYPE_USER -> {
                val user = conversation.conversationWith as? User
                user?.name
            }
            else -> null
        }

        Text(
            text = name ?: "",
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = conversation.lastMessage?.rawMessage.toString() ?: "",
            style = MaterialTheme.typography.body2,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

fun navigateToMessageList(conversation: Conversation, context: Context) {
    val intent = Intent(context, CometChatMessageListActivity::class.java).apply {
        putExtra(UIKitConstants.IntentStrings.TYPE, conversation.conversationType)
        if (conversation.conversationType == CometChatConstants.CONVERSATION_TYPE_GROUP) {
            val group = conversation.conversationWith as Group
            putExtra(UIKitConstants.IntentStrings.NAME, group.name)
            putExtra(UIKitConstants.IntentStrings.GUID, group.guid)
            putExtra(UIKitConstants.IntentStrings.GROUP_OWNER, group.owner)
            putExtra(UIKitConstants.IntentStrings.AVATAR, group.icon)
            putExtra(UIKitConstants.IntentStrings.GROUP_TYPE, group.groupType)
            putExtra(UIKitConstants.IntentStrings.MEMBER_COUNT, group.membersCount)
            putExtra(UIKitConstants.IntentStrings.GROUP_DESC, group.description)
            putExtra(UIKitConstants.IntentStrings.GROUP_PASSWORD, group.password)
        } else {
            val user = conversation.conversationWith as User
            putExtra(UIKitConstants.IntentStrings.NAME, user.name)
            putExtra(UIKitConstants.IntentStrings.UID, user.uid)
            putExtra(UIKitConstants.IntentStrings.AVATAR, user.avatar)
            putExtra(UIKitConstants.IntentStrings.STATUS, user.status)
        }
    }
    context.startActivity(intent)
}

fun formatTime(timestamp: Long): String {
    // Format the timestamp to your desired format
    // Example implementation:
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}


