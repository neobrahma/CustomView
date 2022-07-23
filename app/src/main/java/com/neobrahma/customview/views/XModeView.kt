package com.neobrahma.customview.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.neobrahma.customview.R

class XModeView : View {

    private var h: Int = 0
    private var w: Int = 0


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rect = RectF(5f, 5f, 805f, 805f)
    private val rect2 = RectF(5f, 5f, 805f, 805f)
    val path = Path().apply {
        addArc(rect, 0f, 360f)
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.h = h
        this.w = w
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawLinesInCircle(canvas)
        drawStroke(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        paint.apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.purple_700)
        }
        canvas.drawArc(rect, 0f, 360f, false, paint)
    }

    private fun drawLinesInCircle(canvas: Canvas) {
        canvas.save()
        paint.apply {
            style = Paint.Style.FILL
            strokeWidth = 10f
            color = ContextCompat.getColor(context, R.color.orange)
        }

        canvas.clipPath(path)
        canvas.drawLine(0f, 200f, 810f, 200f, paint)
        canvas.clipPath(path)
        canvas.drawLine(0f, 400f, 810f, 400f, paint)

        canvas.clipPath(path)
        canvas.drawLine(0f, 600f, 810f, 600f, paint)
        canvas.restore()
    }

    private fun drawStroke(canvas: Canvas) {
        paint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 10f
            color = ContextCompat.getColor(context, R.color.orange)
        }
        canvas.drawArc(rect2, 0f, 360f, true, paint)
    }
}