package com.neobrahma.customview.screens.picker

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
import com.neobrahma.customview.views.joypad.JoypadItem
import com.neobrahma.customview.views.picker.BulbItem
import com.neobrahma.customview.views.picker.POIView
import com.neobrahma.customview.views.picker.PickerColorView
import com.neobrahma.customview.views.picker.PickerView

@Composable
fun BulbScreen() {

    val items = listOf(
        BulbItem(
            R.drawable.ic_64_object_base_gpe_lum7,
            0.4f
        )
        ,
        BulbItem(
            R.drawable.ic_64_object_base_gpe_lum7,
            0.85f
        ),
        BulbItem(
            R.drawable.ic_64_object_base_gpe_lum7,
            0.20f
        )
    )

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (picker) = createRefs()
        AndroidView(
            factory = { context ->
                PickerView(context = context).apply {
                    setListBulbItem(items)
                }
            },
            modifier = Modifier
                .constrainAs(picker) {
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