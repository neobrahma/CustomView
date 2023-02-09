package com.neobrahma.customview.views.remotecontrol

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.contains
import com.neobrahma.customview.R
import com.neobrahma.customview.views.TypeResource
import com.neobrahma.customview.views.helper.computeDistanceSegment
import com.neobrahma.customview.views.helper.computePositionOnCircle
import com.neobrahma.customview.views.helper.convertValueToDegree
import com.neobrahma.customview.views.helper.getVectorBitmapFromDrawable

class RemoteControlCircleView : AbstractRemoteControlView {

    private val stroke = context.resources.getDimension(R.dimen.stroke_2dp)
    private val paddingShape = context.resources.getDimension(R.dimen.stroke)
    private val iconSize = context.resources.getDimension(R.dimen.icon_size)
    private val rectShape = RectF(0f, 0f, 0f, 0f)

    private val rectShapePath1 = RectF(0f, 0f, 0f, 0f)
    private val rectShapePath2 = RectF(0f, 0f, 0f, 0f)
    private val listPoints = mutableListOf<PointF>()

    private val pointO = PointF(0f, 0f)

    private var radius: Float = 0f
    private var deltaAngle: Float = 0f

    private val icons = mutableMapOf<Int, Bitmap>()

    private val heightText = resources.getDimensionPixelSize(R.dimen.text)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = stroke
        textSize = resources.getDimension(R.dimen.text)
    }

    private val pathShape = Path()

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

        radius = rectShape.height() / 6

        pointO.apply {
            x = rectShape.centerX()
            y = rectShape.top + (rectShape.height() / 2)
        }

        initListPointItems()
        createShapePath()
    }

    private fun createShapePath() {
        val padding = paddingShape * 2
        rectShapePath1.apply {
            left = rectShape.left + padding
            top = rectShape.top + padding
            right = rectShape.right - padding
            bottom = rectShape.bottom - padding
        }

        val marginCircle = radius * 2 - (2 * padding)
        rectShapePath2.apply {
            left = rectShapePath1.left + marginCircle
            top = rectShapePath1.top + marginCircle
            right = rectShapePath1.right - marginCircle
            bottom = rectShapePath1.bottom - marginCircle
        }

        val quantity = items.size - 1
        val deltaAngle = (360 / quantity).toFloat()

        val pointStart = computePositionOnCircle(
            convertValueToDegree(deltaAngle / 2),
            PointF(rectShape.centerX(), padding),
            pointO
        )
        val pointEndRight = computePositionOnCircle(
            convertValueToDegree(-deltaAngle / 2),
            PointF(rectShape.centerX(), (radius / 2)),
            pointO
        )

        pathShape.moveTo(pointStart.x, pointStart.y)
        pathShape.arcTo(rectShapePath1, -90f - (deltaAngle / 2), deltaAngle)
        pathShape.lineTo(pointEndRight.x, pointEndRight.y)
        pathShape.arcTo(rectShapePath2, -90f + (deltaAngle / 2), -deltaAngle)
        pathShape.close()
    }

    private fun initListPointItems() {
        listPoints.add(pointO)
        val point1 = PointF(rectShape.centerX(), radius)
        listPoints.add(point1)

        val quantity = items.size - 1
        deltaAngle = (360 / quantity).toFloat()

        for (i in 2 until items.size) {
            val point = computePositionOnCircle(
                convertValueToDegree(deltaAngle * (i - 1)),
                point1,
                pointO
            )
            listPoints.add(point)
        }
    }

    override fun initBounds() {
        items.forEachIndexed { index, item ->
            val point = listPoints[index]
            when (item.typeResource) {
                TypeResource.STRING -> {
                    val widthText =
                        paint.measureText(resources.getText(item.idResource).toString())
                            .toInt()
                    item.bound.apply {
                        left = (point.x - ((widthText + iconSize) / 2)).toInt()
                        right = left + widthText + iconSize.toInt()
                        top = (point.y - ((heightText + iconSize) / 2)).toInt()
                        bottom = (top + heightText + iconSize).toInt()
                    }
                }
                TypeResource.DRAWABLE -> {
                    val bitmap =
                        getVectorBitmapFromDrawable(context, item.idResource, iconSize.toInt())
                    icons[index] = bitmap
                    item.bound.apply {
                        left = (point.x - iconSize / 2).toInt()
                        right = (left + iconSize).toInt()
                        top = (point.y - iconSize / 2).toInt()
                        bottom = (top + iconSize).toInt()
                    }
                }
            }
        }
    }

    override fun onDrawShape(canvas: Canvas, hoverPosition: Int) {
        drawBackground(canvas)
        drawStroke(canvas)
        drawHover(canvas, hoverPosition)
        drawContent(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        paint.apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.white)
        }
        canvas.drawRoundRect(rectShape, 16f, 16f, paint)
    }

    private fun drawHover(canvas: Canvas, position: Int) {
        if (position != -1) {
            when (position) {
                0 -> {
                    drawBtnCenter(canvas, R.color.orange)
                }
                else -> {
                    canvas.save()
                    canvas.rotate(
                        -deltaAngle * (position - 1),
                        rectShape.centerX(),
                        rectShape.centerY()
                    )
                    drawShape(canvas, R.color.orange)
                    canvas.restore()
                }
            }
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
        drawBtnCenter(canvas, R.color.grey)
        drawShapes(canvas, R.color.grey)
    }

    private fun drawBtnCenter(canvas: Canvas, idColor: Int) {
        paint.apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, idColor)
        }
        canvas.drawCircle(pointO.x, pointO.y, radius, paint)
        paint.apply {
            style = Paint.Style.STROKE
            color = ContextCompat.getColor(context, R.color.black)
        }
        canvas.drawCircle(pointO.x, pointO.y, radius, paint)
    }

    private fun drawShapes(canvas: Canvas, idColor: Int) {
        for (i in 1 until items.size) {
            canvas.save()
            canvas.rotate(
                deltaAngle * (i - 1),
                rectShape.centerX(),
                rectShape.centerY()
            )
            drawShape(canvas, idColor)
            canvas.restore()
        }
    }

    private fun drawShape(canvas: Canvas, idColor: Int) {
        paint.apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, idColor)
        }
        canvas.drawPath(pathShape, paint)
        paint.apply {
            style = Paint.Style.STROKE
            color = ContextCompat.getColor(context, R.color.black)
        }
        canvas.drawPath(pathShape, paint)
    }

    override fun isTouchInShape(touchPoint: PointF): Boolean {
        return rectShape.contains(touchPoint)
    }

    override fun computeSelectedPosition(touchPoint: PointF): Int {
        listPoints.forEachIndexed { index, pointF ->
            val distance = computeDistanceSegment(pointF, touchPoint)
            if (distance <= radius) {
                return index
            }
        }
        return -1
    }
}