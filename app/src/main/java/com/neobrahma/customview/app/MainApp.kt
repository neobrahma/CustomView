package com.neobrahma.customview.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.neobrahma.customview.R
import com.neobrahma.customview.screens.*
import com.neobrahma.customview.screens.remotecontrol.*
import com.neobrahma.customview.screens.switches.DoubleSwitchScreen
import com.neobrahma.customview.screens.switches.SwitchLedScreen
import com.neobrahma.customview.screens.switches.SwitchScreen

@Composable
fun MainApp() {
    val navController = rememberNavController()
    BoxWithConstraints(
        modifier = Modifier.background(color = colorResource(id = R.color.background))
    ) {
        NavHost(navController = navController, startDestination = HOME) {
            composable(route = HOME)
            {
                HomeScreen(navController)
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
        }
    }
}