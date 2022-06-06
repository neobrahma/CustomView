package com.neobrahma.customview.views.switch

import android.graphics.Rect

data class SwitchItem(
    val typeResource: TypeResource,
    val idResource: Int,
    val idContentDescriptionView: Int,
    val idContentDescriptionAction: Int,
    val bound : Rect = Rect(0,0,0,0)
)

enum class TypeResource {
    STRING,
    DRAWABLE
}
