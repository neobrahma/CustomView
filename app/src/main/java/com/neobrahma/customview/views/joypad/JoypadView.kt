package com.neobrahma.customview.views.joypad

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.neobrahma.customview.R
import com.neobrahma.customview.views.TypeResource
import com.neobrahma.customview.views.helper.*

class JoypadView : AbstractJoypadView {

    private val stroke = context.resources.getDimension(R.dimen.stroke)
    private val hover = stroke * 2
    private val iconSize = context.resources.getDimension(R.dimen.icon_size)
    private val rectShape = RectF(0f, 0f, 0f, 0f)
    private val rectSelectedItem = RectF(0f, 0f, 0f, 0f)

    private val centerPoint = PointF(0f, 0f)
    private val pointO = PointF(0f, 0f)
    private var rayon = 0f

    private val icons = mutableMapOf<Int, Bitmap>()

    private var deltaAngle: Float = 0f
    private var startAngle: Float = 0f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = stroke
        textSize = resources.getDimension(R.dimen.text)
    }

    private val pathHoverClipOut = Path()

    private val tabColorGradient = intArrayOf(
        resources.getColor(R.color.orange_alpha),
        resources.getColor(R.color.orange),
        resources.getColor(R.color.orange_alpha)
    )

    private val heightText = resources.getDimensionPixelSize(R.dimen.text)

    private val tabPositionGradient = FloatArray(3)

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    override fun initVariables(width: Int, height: Int) {
        val padding = stroke / 2
        rectShape.apply {
            val paddingShape = padding + hover
            left = paddingShape
            top = paddingShape
            right = width - paddingShape
            bottom = height - paddingShape
        }

        pathHoverClipOut.addArc(rectShape, 0f, 360f)

        rectSelectedItem.apply {
            left = rectShape.left + hover
            top = rectShape.top - hover
            right = rectShape.right - hover
            bottom = rectShape.bottom
        }

        centerPoint.apply {
            x = rectShape.centerX()
            y = rectShape.centerY()
        }

        rayon = rectShape.height() / 2

        pointO.apply {
            x = rectShape.centerX()
            y = rectShape.top + (rectShape.height() / 8)
        }

        val start = 0.75f
        val delta = 1f / (items.size * 2)
        tabPositionGradient[0] = start - delta
        tabPositionGradient[1] = start
        tabPositionGradient[2] = start + delta

        deltaAngle = 360f / items.size
        startAngle = -90 - deltaAngle / 2
    }

    override fun initBounds() {
        items.forEachIndexed { index, item ->
            val pointItem = computePositionOnCircle(
                convertValueToDegree(index * deltaAngle),
                pointO,
                centerPoint
            )

            when (item.typeResource) {
                TypeResource.STRING -> {
                    val widthText =
                        paint.measureText(resources.getText(item.idResource).toString()).toInt()
                    item.bound.apply {
                        left = (pointItem.x - (widthText / 2) - (iconSize / 2)).toInt()
                        right = left + widthText + iconSize.toInt()
                        top = (pointItem.y - (heightText / 2) - (iconSize / 2)).toInt()
                        bottom = (top + heightText + iconSize).toInt()
                    }
                }
                TypeResource.DRAWABLE -> {
                    val bitmap =
                        getVectorBitmapFromDrawable(context, item.idResource, iconSize.toInt())
                    icons[index] = bitmap
                    item.bound.apply {
                        left = (pointItem.x - iconSize / 2).toInt()
                        right = (left + iconSize).toInt()
                        top = (pointItem.y - iconSize / 2).toInt()
                        bottom = (top + iconSize).toInt()
                    }
                }
            }
        }
    }

    override fun onDrawShape(canvas: Canvas, selectedPosition: Int) {
        drawBackground(canvas)
        drawHover(canvas, selectedPosition)
        drawContent(canvas, items)
    }

    private fun drawBackground(canvas: Canvas) {
        paint.apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.purple_700)
        }
        canvas.drawArc(rectShape, 0f, 360f, true, paint)
    }

    private fun drawHover(canvas: Canvas, position: Int) {
        if (position != -1) {
            paint.apply {
                style = Paint.Style.FILL_AND_STROKE
                color = ContextCompat.getColor(context, R.color.red)
                shader = SweepGradient(
                    centerPoint.x,
                    centerPoint.y,
                    tabColorGradient,
                    tabPositionGradient
                )
            }
            canvas.save()
            canvas.rotate(
                -deltaAngle * position,
                rectShape.centerX(),
                rectShape.centerY()
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                canvas.clipOutPath(pathHoverClipOut)
            } else {
                canvas.clipPath(pathHoverClipOut, Region.Op.DIFFERENCE)
            }

            canvas.drawArc(rectSelectedItem, 0f, 360f, false, paint)
            canvas.restore()
            paint.shader = null
        }
    }

    private fun drawContent(canvas: Canvas, items: List<JoypadItem>) {
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

    override fun isTouchInShape(touchPoint: PointF): Boolean {
        return computeDistanceSegment(touchPoint, centerPoint) <= rayon
    }

    override fun computeSelectedPosition(touchPointF: PointF): Int {
        var angle = computeAngleAOB(centerPoint, pointO, touchPointF)
        if (touchPointF.x >= rectShape.centerX()) {
            angle = 180 + (180 - angle)
        }
        angle += +deltaAngle / 2
        return (angle / deltaAngle).toInt()
    }
}