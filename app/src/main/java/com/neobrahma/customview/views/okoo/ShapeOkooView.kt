package com.neobrahma.customview.views.test

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.neobrahma.customview.R

class ShapeOkooView : View {
    private val stroke = context.resources.getDimension(R.dimen.stroke_2dp)

    private val rectShape: RectF = RectF(0f, 0f, 0f, 0f)
    private val path: Path = Path()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = stroke
        color = ContextCompat.getColor(context, R.color.white)
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectShape.apply {
            bottom = h.toFloat()
            right = w.toFloat()
        }

        val halfWitdh = w / 2

        path.moveTo(rectShape.left, rectShape.top)
        path.lineTo(halfWitdh.toFloat(), rectShape.top)
        path.arcTo(rectShape, -90f, 90f)
        path.lineTo(rectShape.right, rectShape.bottom)
        path.lineTo(rectShape.left, rectShape.bottom)
        path.close()

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.clipPath(path, Region.Op.INTERSECT)
        canvas.drawRoundRect(rectShape, 25f, 25f, paint)
    }
}