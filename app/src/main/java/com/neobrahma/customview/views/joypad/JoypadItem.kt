package com.neobrahma.customview.views.joypad

import android.graphics.Rect
import com.neobrahma.customview.views.TypeResource

data class JoypadItem(
    val typeResource: TypeResource,
    val idResource: Int,
    val idContentDescriptionView: Int,
    val bound : Rect = Rect(0,0,0,0)
)
