package com.neobrahma.customview.views.remotecontrol

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.contains
import com.neobrahma.customview.R
import com.neobrahma.customview.views.TypeResource
import com.neobrahma.customview.views.helper.getVectorBitmapFromDrawable

class RemoteControlView : AbstractRemoteControlView {

    private val stroke = context.resources.getDimension(R.dimen.stroke)
    private val iconSize = context.resources.getDimension(R.dimen.icon_size)
    private val rectShape = RectF(0f, 0f, 0f, 0f)
    private val pointCenterItem = PointF(0f, 0f)

    private var rows: Int = 1

    private var deltaX: Float = 0f
    private var deltaY: Float = 0f

    private val roundRect: Float = 40f

    private val icons = mutableMapOf<Int, Bitmap>()

    private val heightText = resources.getDimensionPixelSize(R.dimen.text)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = stroke
        textSize = resources.getDimension(R.dimen.text)
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    override fun initVariables(width: Int, height: Int) {
        val padding = stroke / 2
        rectShape.apply {
            left = padding
            top = padding
            right = width - padding
            bottom = height - padding
        }

        rows = items.size / columns

        deltaX = rectShape.width() / columns
        deltaY = rectShape.height() / rows

        pointCenterItem.apply {
            x = rectShape.left + deltaX / 2
            y = rectShape.top + deltaY / 2
        }
    }

    override fun initBounds() {
        items.forEachIndexed { index, item ->
            val row = index / columns
            val column = index % columns
            when (item.typeResource) {
                TypeResource.STRING -> {
                    val widthText =
                        paint.measureText(resources.getText(item.idResource).toString())
                            .toInt()
                    item.bound.apply {
                        left =
                            ((pointCenterItem.x - ((widthText + iconSize) / 2)) + deltaX.toInt() * column).toInt()
                        right = left + widthText + iconSize.toInt()
                        top =
                            ((pointCenterItem.y - ((heightText + iconSize) / 2)) + deltaY.toInt() * row).toInt()
                        bottom = (top + heightText + iconSize).toInt()
                    }
                }
                TypeResource.DRAWABLE -> {
                    val bitmap =
                        getVectorBitmapFromDrawable(context, item.idResource, iconSize.toInt())
                    icons[index] = bitmap
                    item.bound.apply {
                        left =
                            ((pointCenterItem.x - iconSize / 2) + deltaX.toInt() * column).toInt()
                        right = (left + iconSize).toInt()
                        top =
                            ((pointCenterItem.y - iconSize / 2) + deltaY.toInt() * row).toInt()
                        bottom = (top + iconSize).toInt()
                    }
                }
            }
        }
    }

    override fun onDrawShape(canvas: Canvas, hoverPosition: Int) {
        drawBackground(canvas)
        drawHover(canvas, hoverPosition)
        drawContent(canvas)
        drawStroke(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        paint.apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.purple_700)
        }
        canvas.drawRoundRect(rectShape, roundRect, roundRect, paint)
    }

    private fun drawHover(canvas: Canvas, position: Int) {
        if (position != -1) {
            canvas.save()
            paint.apply {
                style = Paint.Style.FILL
                color = ContextCompat.getColor(context, R.color.orange)
            }
            val row = position / columns
            val column = position % columns
            val rect = RectF().apply {
                left = rectShape.left + deltaX * column
                right = left + deltaX
                top = rectShape.top + deltaY * row
                bottom = top + deltaY
            }
            canvas.clipRect(rect)
            canvas.drawRoundRect(rectShape,roundRect, roundRect, paint)
            canvas.restore()
        }
    }

    private fun drawContent(canvas: Canvas) {
        items.forEachIndexed { index, item ->
            when (item.typeResource) {
                TypeResource.STRING -> {
                    drawText(canvas, item.idResource, item.bound)
                }
                TypeResource.DRAWABLE -> {
                    icons[index]?.let { bitmap ->
                        paint.apply {
                            style = Paint.Style.FILL
                            color = ContextCompat.getColor(context, R.color.white)
                        }
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

    private fun drawText(canvas: Canvas, idResource: Int, bound: Rect) {
        paint.apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.white)
        }
        canvas.drawText(
            resources.getText(idResource).toString(),
            bound.left.toFloat() + (iconSize / 2),
            bound.top.toFloat() + heightText + (iconSize / 2),
            paint
        )
    }

    private fun drawStroke(canvas: Canvas) {
        paint.apply {
            style = Paint.Style.STROKE
            color = ContextCompat.getColor(context, R.color.purple)
        }
        canvas.drawRoundRect(rectShape, roundRect, roundRect, paint)

        for (row in 1 until rows) {
            val y = deltaY * row
            canvas.drawLine(rectShape.left, y, rectShape.right, y, paint)
        }

        for (column in 1 until columns) {
            val x = deltaX * column
            canvas.drawLine(x * column, rectShape.top, x * column, rectShape.bottom, paint)
        }
    }

    override fun isTouchInShape(touchPoint: PointF): Boolean {
        return rectShape.contains(touchPoint)
    }

    override fun computeSelectedPosition(touchPoint: PointF): Int {
        val row = ((touchPoint.y - rectShape.top) / deltaY).toInt()
        val column = ((touchPoint.x - rectShape.left) / deltaX).toInt()
        return row * columns + column
    }
}