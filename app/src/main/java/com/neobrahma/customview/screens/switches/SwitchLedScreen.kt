package com.neobrahma.customview.screens.switches

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.neobrahma.customview.R
import com.neobrahma.customview.views.TypeResource
import com.neobrahma.customview.views.switch.AbstractSwitchView
import com.neobrahma.customview.views.switch.SwitchItem
import com.neobrahma.customview.views.switch.SwitchLedView

@Preview
@Composable
fun SwitchLedScreen() {

    val items = listOf(
        SwitchItem(
            TypeResource.STRING,
            R.string.state_switch_on,
            R.string.content_description_switch_on,
            R.string.switch_action_off

        ),
        SwitchItem(
            TypeResource.STRING,
            R.string.state_switch_off,
            R.string.content_description_switch_off,
            R.string.switch_action_on
        )
    )

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (view) = createRefs()
        AndroidView(
            factory = { context ->
                SwitchLedView(context = context).apply {
                    this.items = items
                    isChecked = true
                    listener = AbstractSwitchView.SwitchListener {
                        Log.d("SwitchActivity", "onClickItem: $it")
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