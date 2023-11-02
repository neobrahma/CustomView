package com.neobrahma.customview.app

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.neobrahma.customview.R
import com.neobrahma.customview.screens.joypad.JoypadScreen
import com.neobrahma.customview.screens.picker.BulbScreen
import com.neobrahma.customview.screens.remotecontrol.RemoteControl3ButtonsScreen
import com.neobrahma.customview.screens.remotecontrol.RemoteControl4ButtonsHueScreen
import com.neobrahma.customview.screens.remotecontrol.RemoteControl4ButtonsScreen
import com.neobrahma.customview.screens.remotecontrol.RemoteControlCircleButtonsScreen
import com.neobrahma.customview.screens.remotecontrol.RemoteControlXButtonsScreen
import com.neobrahma.customview.screens.remotecontrol.StatelessSwitchScreen
import com.neobrahma.customview.screens.switches.DoubleSwitchScreen
import com.neobrahma.customview.screens.switches.SwitchLedScreen
import com.neobrahma.customview.screens.switches.SwitchScreen
import com.neobrahma.customview.screens.test.ShapeOkooScreen
import kotlinx.coroutines.launch

@Composable
fun MainApp() {
    val navController = rememberNavController()
    BoxWithConstraints(
        modifier = Modifier.background(color = colorResource(id = R.color.white))
    ) {
        NavHost(navController = navController, startDestination = HOME) {
            composable(route = HOME)
            {
                Test()
//                HomeScreen(navController)
            }
            composable(route = SWITCH_SCREEN) {
                SwitchScreen()
            }
            composable(route = SWITCH_SCREEN_LED) {
                SwitchLedScreen()
            }
            composable(route = DOUBLE_SWITCH_SCREEN) {
                DoubleSwitchScreen()
            }
            composable(route = STATELESS_SWITCH_SCREEN) {
                StatelessSwitchScreen()
            }
            composable(route = RC_RECT_BUTTONS_SCREEN) {
                RemoteControl3ButtonsScreen()
            }
            composable(route = RC_CIRCLE_BUTTONS_SCREEN) {
                RemoteControlCircleButtonsScreen()
            }
            composable(route = RC_4_BUTTONS_SCREEN) {
                RemoteControl4ButtonsScreen()
            }
            composable(route = RC_4_BUTTONS_SCREEN_HUE) {
                RemoteControl4ButtonsHueScreen()
            }
            composable(route = RC_X_BUTTONS_SCREEN) {
                RemoteControlXButtonsScreen()
            }
            composable(route = JOYPAD_SCREEN) {
                JoypadScreen()
            }
            composable(route = BULB_SCREEN) {
                BulbScreen()
            }
            composable(route = PATH_BACKGROUND_OKOO) {
                ShapeOkooScreen()
            }
        }
    }
}

@Composable
fun Test() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        ButtomCustom()
    }
}

@Composable
fun ButtomCustom() {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = Modifier
            .height(50.dp)
            .width(100.dp)
            .clickable(
                onClick = {
                          println("tom971 click")
                },
                interactionSource = interactionSource,
                indication = null
            )
    ) {
        Box(
            modifier = Modifier
                .height(47.dp)
                .width(97.dp)
                .clip(CircleShape)
                .background(color = Color(0xFFFFF00FF))
                .align(Alignment.BottomEnd)
        )
        Box(
            modifier = Modifier
                .height(47.dp)
                .width(97.dp)
                .clip(CircleShape)
                .background(color = Color(0xFF8C08FF))
                .align(if(isPressed){
                    Alignment.BottomEnd
                }else{
                    Alignment.TopStart
                })
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "text",
                color = Color.White
            )
        }
    }
}

