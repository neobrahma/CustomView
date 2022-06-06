package com.neobrahma.customview.views.switch

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.neobrahma.customview.R
import com.neobrahma.customview.views.TypeResource
import com.neobrahma.customview.views.helper.computeDistanceSegment
import com.neobrahma.customview.views.helper.getVectorBitmapFromDrawable

class SwitchView : AbstractSwitchView {

    private val stroke = context.resources.getDimension(R.dimen.stroke)
    private val iconSize = context.resources.getDimension(R.dimen.icon_size)
    private val rectShape = RectF(0f, 0f, 0f, 0f)
    private val rectSelected = RectF(0f, 0f, 0f, 0f)

    private val centerPoint = PointF(0f, 0f)
    private var rayon = 0f

    private var delta = 0f

    private val icons = mutableMapOf<Int, Bitmap>()

    private val path = Path()

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = stroke
        textSize = resources.getDimension(R.dimen.text)
    }

    private val paintBlur = Paint().apply {
        strokeWidth = stroke
        style = Paint.Style.STROKE
        color = ContextCompat.getColor(context, R.color.white)
        maskFilter = BlurMaskFilter(stroke * 2, BlurMaskFilter.Blur.NORMAL)
    }

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

        centerPoint.apply {
            x = rectShape.centerX()
            y = rectShape.centerY()
        }

        rayon = rectShape.height() / 2

        rectSelected.apply {
            left = rectShape.left
            top = rectShape.top
            right = rectShape.right
            bottom = rectShape.centerY()
        }

        delta = rectShape.height() / 2

        path.addArc(rectShape, 0f, 360f)
    }

    override fun initBounds() {
        items.forEachIndexed { index, item ->
            when (item.typeResource) {
                TypeResource.STRING -> {
                    val heightText = resources.getDimensionPixelSize(R.dimen.text)
                    val widthText =
                        paint.measureText(resources.getText(item.idResource).toString())
                            .toInt()
                    item.bound.apply {
                        left = ((rectSelected.width() - widthText) / 2).toInt()
                        right = left + widthText
                        top =
                            (((rectSelected.height() + heightText) / 2) + delta.toInt() * index).toInt()
                        bottom = top + heightText
                    }
                }
                TypeResource.DRAWABLE -> {
                    val bitmap =
                        getVectorBitmapFromDrawable(context, item.idResource, iconSize.toInt())
                    icons[index] = bitmap
                    item.bound.apply {
                        left = ((rectSelected.width() - bitmap.width) / 2).toInt()
                        right = left + bitmap.width
                        top =
                            (((rectSelected.height() - bitmap.height) / 2) + delta.toInt() * index).toInt()
                        bottom = top - bitmap.height
                    }
                }
            }
        }
    }

    override fun onDrawShape(
        canvas: Canvas,
        selectedPosition: Int,
        isHoverDisplayed: Boolean
    ) {
        drawBackground(canvas)
        drawSelectedItem(canvas, selectedPosition)
        drawContent(canvas)
        drawHover(canvas, isHoverDisplayed)
        drawStroke(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        paint.apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.blue_dark)
        }
        canvas.drawArc(rectShape, 0f, 360f, true, paint)
    }

    private fun drawContent(canvas: Canvas) {
        items.forEachIndexed { index, item ->
            when (item.typeResource) {
                TypeResource.STRING -> {
                    drawText(canvas, item.idResource, item.bound)
                }
                TypeResource.DRAWABLE -> {
                    icons[index]?.let { bitmap ->
                        canvas.drawBitmap(
                            bitmap,
                            item.bound.left.toFloat(),
                            item.bound.top.toFloat(),
                            null
                        )
                    }
                }
            }
        }
    }

    private fun drawHover(canvas: Canvas, isHoverDisplayed: Boolean) {
        if (isHoverDisplayed) {
            canvas.drawArc(rectShape, 0f, 360f, true, paintBlur)
        }
    }

    private fun drawText(canvas: Canvas, idResource: Int, bound: Rect) {
        paint.apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.white)
        }
        canvas.drawText(
            resources.getText(idResource).toString(),
            bound.left.toFloat(),
            bound.top.toFloat(),
            paint
        )
    }

    private fun drawStroke(canvas: Canvas) {
        paint.apply {
            style = Paint.Style.STROKE
            color = ContextCompat.getColor(context, R.color.purple)
        }
        canvas.drawArc(rectShape, 0f, 360f, true, paint)
        canvas.drawLine(
            rectShape.left,
            rectShape.centerY(),
            rectShape.right,
            rectShape.centerY(),
            paint
        )
    }

    private fun drawSelectedItem(canvas: Canvas, position: Int) {
        paint.apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.red)
        }
        canvas.save()
        canvas.clipPath(path)
        canvas.drawRect(
            rectSelected.left,
            rectSelected.top + position * delta,
            rectSelected.right,
            rectSelected.bottom + position * delta,
            paint
        )
        canvas.restore()
    }

    override fun isTouchInShape(touchPoint: PointF): Boolean {
        return computeDistanceSegment(touchPoint, centerPoint) <= rayon
    }
}