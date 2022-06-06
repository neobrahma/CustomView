package com.neobrahma.customview.views.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.VectorDrawable
import androidx.core.content.ContextCompat

fun getVectorBitmapFromDrawable(context: Context, drawableId: Int, iconSize: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(context, drawableId) as VectorDrawable
    val bitmap = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}