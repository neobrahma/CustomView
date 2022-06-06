package com.neobrahma.customview.views.switch

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.neobrahma.customview.R

class SwitchLedView : AbstractSwitchView {

    private val stroke = context.resources.getDimension(R.dimen.stroke)
    private val rectShape = RectF(0f, 0f, 0f, 0f)
    private val rectButton = RectF(0f, 0f, 0f, 0f)
    private val rectShadow = RectF(0f, 0f, 0f, 0f)
    private val rectLed = RectF(0f, 0f, 0f, 0f)

    private val pathShadowOn = Path()
    private val pathShadowOff = Path()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = stroke
        textSize = resources.getDimension(R.dimen.text)
    }

    private val _paintBlur = Paint().apply {
        set(paint)
        strokeWidth = stroke * 3
        maskFilter = BlurMaskFilter(18f, BlurMaskFilter.Blur.NORMAL)
        color = ContextCompat.getColor(context, R.color.on_blur)
    }

    private val around = 32f

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    override fun initVariables(width: Int, height: Int) {
        val padding = stroke * 3
        rectShape.apply {
            left = padding
            top = padding
            right = width - padding
            bottom = height - padding
        }
        rectShadow.apply {
            left = rectShape.left + padding
            top = rectShape.top + padding
            right = rectShape.right - padding
            bottom = rectShape.bottom - padding
        }
        rectButton.apply {
            left = rectShadow.left + stroke
            top = rectShadow.top + (2 * stroke)
            right = rectShadow.right - stroke
            bottom = rectShadow.bottom - stroke
        }
        rectLed.apply {
            left = rectButton.left + padding
            bottom = rectButton.bottom - padding
            top = bottom - padding
            right = rectShape.bottom - (2 * padding)
        }

        initShadowOn()
        initShadowOff()
    }

    private fun initShadowOn() {
        val rectRightTop = RectF(
            rectShadow.right - around,
            rectShadow.top,
            rectShadow.right,
            rectShadow.top + around
        )

        val rectRightBottom = RectF(
            rectShadow.right - around - stroke,
            rectShadow.bottom - around,
            rectShadow.right - stroke,
            rectShadow.bottom
        )

        val rectLeftBottom = RectF(
            rectShadow.left + stroke,
            rectShadow.bottom - around,
            rectShadow.left + around + stroke,
            rectShadow.bottom
        )

        val rectLeftTop = RectF(
            rectShadow.left,
            rectShadow.top,
            rectShadow.left + around,
            rectShadow.top + around
        )

        pathShadowOn.moveTo(rectShadow.left + rectShadow.width() / 2, rectShadow.top)
        pathShadowOn.arcTo(rectRightTop, -90f, 90f)
        pathShadowOn.lineTo(rectRightBottom.right, rectRightBottom.top)
        pathShadowOn.arcTo(rectRightBottom, 0f, 90f)
        pathShadowOn.lineTo(rectLeftBottom.right, rectLeftBottom.bottom)
        pathShadowOn.arcTo(rectLeftBottom, 90f, 90f)
        pathShadowOn.lineTo(rectLeftTop.left, rectLeftTop.bottom)
        pathShadowOn.arcTo(rectLeftTop, 180f, 90f)
        pathShadowOn.close()
    }

    private fun initShadowOff() {
        val rectRightTop = RectF(
            rectShadow.right - around - stroke,
            rectShadow.top,
            rectShadow.right - stroke,
            rectShadow.top + around
        )

        val rectRightBottom = RectF(
            rectShadow.right - around,
            rectShadow.bottom - around,
            rectShadow.right,
            rectShadow.bottom
        )

        val rectLeftBottom = RectF(
            rectShadow.left,
            rectShadow.bottom - around,
            rectShadow.left + around,
            rectShadow.bottom
        )

        val rectLeftTop = RectF(
            rectShadow.left + stroke,
            rectShadow.top,
            rectShadow.left + around + stroke,
            rectShadow.top + around
        )

        pathShadowOff.moveTo(rectShadow.left + rectShadow.width() / 2, rectShadow.top)
        pathShadowOff.arcTo(rectRightTop, -90f, 90f)
        pathShadowOff.lineTo(rectRightBottom.right, rectRightBottom.top)
        pathShadowOff.arcTo(rectRightBottom, 0f, 90f)
        pathShadowOff.lineTo(rectLeftBottom.right, rectLeftBottom.bottom)
        pathShadowOff.arcTo(rectLeftBottom, 90f, 90f)
        pathShadowOff.lineTo(rectLeftTop.left, rectLeftTop.bottom)
        pathShadowOff.arcTo(rectLeftTop, 180f, 90f)
        pathShadowOff.close()
    }

    override fun initBounds() {}

    override fun onDrawShape(canvas: Canvas, selectedPosition: Int, isHoverDisplayed: Boolean) {
        drawBackground(canvas)
        drawButton(canvas)
        drawLed(canvas)
    }

    override fun isTouchInShape(touchPoint: PointF): Boolean {
        return rectShape.contains(touchPoint.x, touchPoint.y)
    }

    private fun drawBackground(canvas: Canvas) {
        paint.apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.white)
        }
        canvas.drawRoundRect(rectShape, 16f, 16f, paint)
    }

    private fun drawButton(canvas: Canvas) {
        paint.apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.grey)
        }
        if (isChecked) {
            canvas.drawPath(pathShadowOn, paint)
        } else {
            canvas.drawPath(pathShadowOff, paint)
        }

        paint.apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.beige)
        }

        rectButton.apply {
            if (isChecked) {
                top = rectShadow.top + (2 * stroke)
                bottom = rectShadow.bottom
            } else {
                top = rectShadow.top
                bottom = rectShadow.bottom - (2 * stroke)
            }
        }

        canvas.drawRoundRect(rectButton, 16f, 16f, paint)
    }

    private fun drawLed(canvas: Canvas) {
        paint.apply {
            style = Paint.Style.FILL
            color = if (isChecked) {
                ContextCompat.getColor(context, R.color.on)
            } else {
                ContextCompat.getColor(context, R.color.off)
            }
        }
        rectLed.apply {
            bottom = if (isChecked) {
                rectShadow.bottom - (stroke * 3)
            } else {
                rectShadow.bottom - (stroke * 5)
            }
            top = bottom - (stroke * 3)
        }
        if(isChecked){
            canvas.drawRoundRect(rectLed, 16f, 16f, _paintBlur)
        }
        canvas.drawRoundRect(rectLed, 16f, 16f, paint)
    }

}