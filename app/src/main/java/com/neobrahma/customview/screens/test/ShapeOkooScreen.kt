package com.neobrahma.customview.screens.test

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.neobrahma.customview.views.test.ShapeOkooView

@Composable
fun ShapeOkooScreen() {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (view) = createRefs()
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val (view) = createRefs()
            AndroidView(
                factory = { context ->
                    ShapeOkooView(context = context)
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
}