package com.neobrahma.customview.screens.switches

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.neobrahma.customview.R
import com.neobrahma.customview.views.TypeResource
import com.neobrahma.customview.views.switch.AbstractSwitchView
import com.neobrahma.customview.views.switch.SwitchDoubleCanalView
import com.neobrahma.customview.views.switch.SwitchItem

@Composable
fun DoubleSwitchScreen() {

    val items1 = listOf(
        SwitchItem(
            TypeResource.STRING,
            R.string.state_switch_on,
            R.string.content_description_canal_1_switch_on,
            R.string.switch_action_off
        ),
        SwitchItem(
            TypeResource.STRING,
            R.string.state_switch_off,
            R.string.content_description_canal_1_switch_off,
            R.string.switch_action_on
        )
    )

    val items2 = listOf(
        SwitchItem(
            TypeResource.STRING,
            R.string.state_switch_on,
            R.string.content_description_canal_2_switch_on,
            R.string.switch_action_off
        ),
        SwitchItem(
            TypeResource.STRING,
            R.string.state_switch_off,
            R.string.content_description_canal_2_switch_off,
            R.string.switch_action_on
        )
    )

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (view1, view2) = createRefs()
        AndroidView(
            factory = { context ->
                SwitchDoubleCanalView(context = context).apply {
                    this.items = items1
                    isChecked = true
                    direction = SwitchDoubleCanalView.Position.LEFT
                    listener = AbstractSwitchView.SwitchListener {
                        Log.d("SwitchActivity", "onClickItem: switch1 $it")
                    }
                }
            },
            modifier = Modifier
                .constrainAs(view1) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(view2.start)

                    width = Dimension.ratio("1:2")
                    height = Dimension.fillToConstraints
                }
        )

        AndroidView(
            factory = { context ->
                SwitchDoubleCanalView(context = context).apply {
                    this.items = items2
                    isChecked = false
                    direction = SwitchDoubleCanalView.Position.RIGHT
                    listener = AbstractSwitchView.SwitchListener {
                        Log.d("SwitchActivity", "onClickItem: switch2 $it")
                    }
                }
            },
            modifier = Modifier
                .constrainAs(view2) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(view1.end)
                    end.linkTo(parent.end)

                    width = Dimension.ratio("1:2")
                    height = Dimension.fillToConstraints
                }
        )
    }
}