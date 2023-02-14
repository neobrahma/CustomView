package com.neobrahma.customview.screens.remotecontrol

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.neobrahma.customview.R
import com.neobrahma.customview.views.TypeResource
import com.neobrahma.customview.views.remotecontrol.AbstractRemoteControlView
import com.neobrahma.customview.views.remotecontrol.RemoteControlItem
import com.neobrahma.customview.views.remotecontrol.RemoteControlView

@Composable
fun RemoteControl4ButtonsHueScreen(){

    val items = listOf(
        RemoteControlItem(
            TypeResource.DRAWABLE,
            R.drawable.ic_arrow_up,
            R.string.rc_garage_action_up
        ),
        RemoteControlItem(
            TypeResource.STRING,
            R.string.action_1,
            R.string.rc_garage_action_stop,
            2
        ),
        RemoteControlItem(
            TypeResource.STRING,
            R.string.action_2,
            R.string.rc_garage_action_down,
            2
        ),
        RemoteControlItem(
            TypeResource.DRAWABLE,
            R.drawable.ic_arrow_down,
            R.string.rc_garage_action_up
        ),
    )

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (view) = createRefs()
        AndroidView(
            factory = { context ->
                RemoteControlView(context = context).apply {
                    this.items = items
                    this.columns = 1
                    this.listener = AbstractRemoteControlView.RemoteControlListener {
                        Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .constrainAs(view) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                    width = Dimension.ratio("1:2")
                    height = Dimension.fillToConstraints
                }
                .padding(4.dp)
        )
    }
}