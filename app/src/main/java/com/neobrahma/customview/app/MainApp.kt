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
        }
    }
}