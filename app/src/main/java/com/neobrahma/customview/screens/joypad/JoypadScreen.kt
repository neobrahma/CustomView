package com.neobrahma.customview.screens.joypad

import android.util.Log
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
import com.neobrahma.customview.views.joypad.AbstractJoypadView
import com.neobrahma.customview.views.joypad.JoypadItem
import com.neobrahma.customview.views.joypad.JoypadView

@Composable
fun JoypadScreen() {

    val items = listOf(
        JoypadItem(
            TypeResource.DRAWABLE,
            R.drawable.ic_arrow_up,
            R.string.joypad_action_up
        ),
        JoypadItem(
            TypeResource.DRAWABLE,
            R.drawable.ic_arrow_left,
            R.string.joypad_action_left
        ),
        JoypadItem(
            TypeResource.DRAWABLE,
            R.drawable.ic_arrow_down,
            R.string.joypad_action_down
        ),
        JoypadItem(
            TypeResource.DRAWABLE,
            R.drawable.ic_arrow_right,
            R.string.joypad_action_right
        )
    )

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (view) = createRefs()
        AndroidView(
            factory = { context ->
                JoypadView(context = context).apply {
                    this.items = items
                    listener = AbstractJoypadView.JoypadListener {
                        Log.e("JoypadCircleActivity", "on selected position : $it")
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
                .padding(4.dp)
        )
    }
}