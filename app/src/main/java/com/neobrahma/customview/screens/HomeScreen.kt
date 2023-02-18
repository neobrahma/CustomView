package com.neobrahma.customview.screens

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.neobrahma.customview.R
import com.neobrahma.customview.app.*

sealed class ItemList {
    data class TitleItem(val title: String) : ItemList()
    data class ButtonItem(val title: String, val route: String) : ItemList()
}

@Composable
fun HomeScreen(navController: NavController) {
    val list: List<ItemList> = listOf<ItemList>(
        ItemList.TitleItem("Abstract Switch View"),
        ItemList.ButtonItem("switch 1 Enki", SWITCH_SCREEN),
        ItemList.ButtonItem("switch 2 LED", SWITCH_SCREEN_LED),
        ItemList.ButtonItem("double switch Enki", DOUBLE_SWITCH_SCREEN),
        ItemList.TitleItem("Abstract Remote Control View"),
        ItemList.ButtonItem("stateless switch Enki", STATELESS_SWITCH_SCREEN),
        ItemList.ButtonItem("Remote control 3 buttons Enki", RC_RECT_BUTTONS_SCREEN),
        ItemList.ButtonItem("Remote control 4 buttons Enki", RC_4_BUTTONS_SCREEN),
        ItemList.ButtonItem("Remote control Circle 3 buttons", RC_CIRCLE_BUTTONS_SCREEN),
        ItemList.ButtonItem("Remote control 4 buttons Hue", RC_4_BUTTONS_SCREEN_HUE),
        ItemList.ButtonItem("Remote control X buttons with weight", RC_X_BUTTONS_SCREEN),
        ItemList.TitleItem("Abstract joypad View"),
        ItemList.ButtonItem("Joypad 4 buttons", JOYPAD_SCREEN)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
    ) {
        items(list) {
            when (it) {
                is ItemList.ButtonItem -> ButtonView(navController, it.title, it.route)
                is ItemList.TitleItem -> TitleView(it.title)
            }
        }
    }
}

@Composable
fun ButtonView(navController: NavController, title: String, route: String) {
    val context = LocalContext.current
    Button(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp),
        onClick = {
            navController.navigate(route)
        }) {
        Text(text = title)
    }
}

@Composable
fun TitleView(title: String) {
    Text(
        text = title,
        color = colorResource(id = R.color.white),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        textAlign = TextAlign.Center
    )
}