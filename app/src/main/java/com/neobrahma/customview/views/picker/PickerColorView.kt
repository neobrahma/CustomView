package com.neobrahma.customview.views.picker

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.neobrahma.customview.R

class PickerColorView : View {

    private val stroke = context.resources.getDimension(R.dimen.stroke)
    private val iconSize = context.resources.getDimension(R.dimen.icon_size)
    private val rectPicker = RectF(0f, 0f, 0f, 0f)
    private val pointO = PointF(0f, 0f)

    var listener: PickerColorListener? = null

    companion object {
        private const val SIZE = 360
        private const val DEFAULT_BRIGHTNESS = 1.0f
        private const val DEFAULT_SATURATION = 1.0f
    }

    private val centerPoint = PointF(0f, 0f)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = stroke * 4
    }

    private val tabColorGradient = IntArray(SIZE)
    private val tabPositionGradient = FloatArray(SIZE)

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val padding = iconSize + stroke + stroke + stroke
        rectPicker.apply {
            top = padding
            left = padding
            bottom = h - padding
            right = w - padding
        }

        centerPoint.apply {
            x = rectPicker.centerX()
            y = rectPicker.centerY()
        }

        val rectTmp = RectF()
        rectTmp.apply {
            top = iconSize
            left = iconSize
            bottom = h - iconSize
            right = w - iconSize
        }

        pointO.apply {
            x = rectTmp.right
            y = rectTmp.centerY()
        }

        val deltaPosition = 1f / SIZE
        for (i in tabPositionGradient.indices) {
            tabPositionGradient[i] = i * deltaPosition
        }

        val deltaColor = 360 / SIZE
        for (i in tabColorGradient.indices) {
            tabColorGradient[i] =
                Color.HSVToColor(
                    floatArrayOf(
                        (i * deltaColor).toFloat(),
                        DEFAULT_SATURATION,
                        DEFAULT_BRIGHTNESS
                    )
                )
        }

        listener?.tabColorGradientComputed()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawPicker(canvas)
    }

    private fun drawPicker(canvas: Canvas) {
        paint.apply {
            style = Paint.Style.STROKE
            shader = SweepGradient(
                centerPoint.x,
                centerPoint.y,
                tabColorGradient,
                tabPositionGradient
            )
        }
        canvas.drawArc(rectPicker, 0f, 360f, false, paint)
    }

    fun getSelectedColor(position: Int): Int {
        return tabColorGradient[position]
    }

    fun getColor(hue: Float): Int {
        val position = (hue * 360).toInt()
        return tabColorGradient[position]
    }

    fun getAngle(hue: Float): Float {
        return (hue * 360).toInt().toFloat()//multiplie par le pas
    }

    fun interface PickerColorListener {
        fun tabColorGradientComputed()
    }
}