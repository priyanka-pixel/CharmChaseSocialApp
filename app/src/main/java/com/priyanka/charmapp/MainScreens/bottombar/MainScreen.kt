package com.priyanka.charmapp.MainScreens.bottombar

import androidx.compose.animation.Crossfade
import com.priyanka.charmapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.cometchat.pro.models.User
import com.priyanka.charmapp.MainScreens.presentation.Chats
import com.priyanka.charmapp.MainScreens.presentation.GroupListView
import com.priyanka.charmapp.MainScreens.presentation.Users
import com.priyanka.charmapp.MainScreens.presentation.components.bottomBarHeight
import com.priyanka.charmapp.MainScreens.presentation.components.icon


@Composable
fun MainScreen(navController: NavController) {
    val sectionState = remember { mutableStateOf(HomeSection.Chats) }
    val navItems = HomeSection.values()
        .toList()
    Scaffold(
        bottomBar = {
            BottomBar(
                items = navItems,
                currentSection = sectionState.value,
                onSectionSelected = { sectionState.value = it },
            )
        }) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        Crossfade(
            modifier = modifier,
            targetState = sectionState.value
        )
        { section ->
            when (section) {
                HomeSection.Chats -> Chats(navController)
                HomeSection.Calls -> Content(title = "calls")
                HomeSection.User -> Users()
                HomeSection.Groups -> GroupListView()
                HomeSection.More -> Content(title = "More")
            }
        }
    }
}

@Composable
private fun Content(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5
        )
    }
}

@Composable
private fun BottomBar(
    items: List<HomeSection>,
    currentSection: HomeSection,
    onSectionSelected: (HomeSection) -> Unit,
) {
    BottomNavigation(
        modifier = Modifier.height(bottomBarHeight),
        backgroundColor = MaterialTheme.colors.background,
        contentColor = contentColorFor(MaterialTheme.colors.background)
    ) {
        items.forEach { section ->

            val selected = section == currentSection

            val iconRes = if (selected) section.selectedIcon else section.icon

            BottomNavigationItem(
                icon = {

                    if (section == HomeSection.More) {
                        BottomBarProfile(selected)
                    } else {
                          Icon(
                              painter = painterResource(id = iconRes),
                            modifier = Modifier.icon(),
                            contentDescription = ""
                        )
                    }

                },
                selected = selected,
                onClick = { onSectionSelected(section) },
                alwaysShowLabel = false
            )
        }
    }
}

@Composable
private fun BottomBarProfile(isSelected: Boolean) {
    val shape = CircleShape

    val borderModifier = if (isSelected) {
        Modifier
            .border(
                color = Color.LightGray,
                width = 1.dp,
                shape = shape
            )
    } else Modifier

    val padding = if (isSelected) 3.dp else 0.dp

    Box(
        modifier = borderModifier
    ) {
        Box(
            modifier = Modifier
                .icon()
                .padding(padding)
                .background(color = Color.LightGray, shape = shape)
                .clip(shape)
        ) {
            val currentUser = User()
            Image(
                painter = rememberImagePainter(currentUser.avatar),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

}

private enum class HomeSection(
    val icon: Int,
    val selectedIcon: Int
) {
    Chats(R.drawable.baseline_chat_bubble_outline_24, R.drawable.baseline_chat_bubble_24),
    Calls(R.drawable.baseline_call_24,R.drawable.baseline_call_24),
    User(R.drawable.baseline_person_24, R.drawable.baseline_person_24),
    Groups(R.drawable.baseline_group_24,R.drawable.baseline_group_24),
    More(R.drawable.baseline_more_vert_24, R.drawable.baseline_more_vert_24)

}

