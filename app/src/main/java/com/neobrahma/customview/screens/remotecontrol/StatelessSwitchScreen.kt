package com.neobrahma.customview.screens.remotecontrol

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.neobrahma.customview.R
import com.neobrahma.customview.views.TypeResource
import com.neobrahma.customview.views.remotecontrol.AbstractRemoteControlView
import com.neobrahma.customview.views.remotecontrol.RemoteControlItem
import com.neobrahma.customview.views.remotecontrol.SwitchStatelessView

@Composable
fun StatelessSwitchScreen() {

    val items = listOf(
        RemoteControlItem(
            TypeResource.STRING,
            R.string.state_switch_on,
            R.string.rc_switch_stateless_on
        ),
        RemoteControlItem(
            TypeResource.STRING,
            R.string.state_switch_off,
            R.string.rc_switch_stateless_off
        )
    )

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (view) = createRefs()
        AndroidView(
            factory = { context ->
                SwitchStatelessView(context = context).apply {
                    this.items = items
                    listener = AbstractRemoteControlView.RemoteControlListener {

                    }
                }
            },
            modifier = Modifier
                .constrainAs(view) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                    width = Dimension.ratio("1:1")
                    height = Dimension.fillToConstraints
                }
        )
    }
}