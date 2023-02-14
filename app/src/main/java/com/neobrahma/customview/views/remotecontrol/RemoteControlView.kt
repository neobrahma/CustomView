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

    private val listSquareData: MutableList<MutableList<SquareData>> = mutableListOf()

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

        var positionCell = 0
        items.forEachIndexed { index, item ->
            if (item.weight == 1) {
                val data = SquareData(index, item.weight)
                listSquareData.add(mutableListOf(data))
                positionCell++
            } else {
                if (listSquareData.size == positionCell) {
                    val data = SquareData(index, item.weight)
                    listSquareData.add(mutableListOf(data))
                } else {
                    val data = SquareData(index, item.weight)
                    listSquareData[positionCell].add(data)
                    if (listSquareData[positionCell].size == data.weight) {
                        positionCell++
                    }
                }
            }
        }

        rows = listSquareData.size / columns

        deltaX = rectShape.width() / columns
        deltaY = rectShape.height() / rows

        pointCenterItem.apply {
            x = rectShape.left + deltaX / 2
            y = rectShape.top + deltaY / 2
        }
    }

    override fun initBounds() {
        listSquareData.forEachIndexed { index, item ->
            val row = index / columns
            val column = index % columns
            val weight = item.size

            if (weight == 1) {
                val itemRC = items[item[0].id]
                when (itemRC.typeResource) {
                    TypeResource.STRING -> {
                        computeBoundForText(itemRC, column, row, weight, 0)
                    }
                    TypeResource.DRAWABLE -> {
                        computeBoundForDrawable(itemRC, item[0].id, column, row, weight, 0)
                    }
                }
            } else {
                val newDeltaY = deltaY / weight
                item.forEachIndexed { index, cellData ->
                    val itemRC = items[cellData.id]
                    when (itemRC.typeResource) {
                        TypeResource.STRING -> {
                            computeBoundForText(itemRC, column, row, weight, newDeltaY.toInt() * index)
                        }
                        TypeResource.DRAWABLE -> {
                            computeBoundForDrawable(
                                itemRC,
                                cellData.id,
                                column,
                                row,
                                weight,
                                newDeltaY.toInt() * index
                            )
                        }
                    }
                }
            }
        }
    }

    private fun computeBoundForDrawable(
        itemRC: RemoteControlItem,
        id: Int,
        column: Int,
        row: Int,
        weight: Int,
        newDeltaY: Int
    ) {
        val bitmap =
            getVectorBitmapFromDrawable(context, itemRC.idResource, iconSize.toInt())
        icons[id] = bitmap
        itemRC.bound.apply {
            left =
                ((pointCenterItem.x - iconSize / 2) + deltaX.toInt() * column).toInt()
            right = (left + iconSize).toInt()
            top =
                ((pointCenterItem.y / weight - iconSize / 2) + deltaY.toInt() * row + newDeltaY).toInt()
            bottom = (top + iconSize).toInt()
        }
    }

    private fun computeBoundForText(
        itemRC: RemoteControlItem,
        column: Int,
        row: Int,
        weight: Int,
        newDeltaY: Int
    ) {
        val widthText =
            paint.measureText(resources.getText(itemRC.idResource).toString())
                .toInt()
        itemRC.bound.apply {
            left =
                ((pointCenterItem.x - ((widthText + iconSize) / 2)) + deltaX.toInt() * column).toInt()
            right = left + widthText + iconSize.toInt()
            top =
                ((pointCenterItem.y/weight - ((heightText + iconSize) / 2)) + deltaY.toInt() * row + newDeltaY).toInt()
            bottom = (top + heightText + iconSize).toInt()
        }
    }

    override fun onDrawShape(canvas: Canvas, hoverPosition: Int) {
        drawBackground(canvas)
        drawHover(canvas, hoverPosition)
        drawContent(canvas)
        drawGrid(canvas)
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

            var positionSquare = 0
            var weight = 1
            var positionY = 0
            listSquareData.forEachIndexed { index, cellData ->
                if (cellData.size == 1 && cellData[0].id == position) {
                    positionSquare = index
                } else {
                    val quantity = cellData.size
                    val positionStartList = cellData[0].id
                    if (positionStartList <= position && position < positionStartList + quantity) {
                        positionSquare = index
                        weight = quantity
                        positionY = position - positionStartList
                    }
                }
            }

            val row = positionSquare / columns
            val column = positionSquare % columns

            val rect = RectF().apply {
                left = rectShape.left + deltaX * column
                right = left + deltaX
                top = rectShape.top + deltaY * row + (deltaY / weight) * positionY
                bottom = top + (deltaY / weight)
            }
            canvas.clipRect(rect)
            canvas.drawRoundRect(rectShape, roundRect, roundRect, paint)
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

    private fun drawGrid(canvas: Canvas) {
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

        listSquareData.forEachIndexed { index, cellData ->
            val weight = cellData.size
            if (weight != 1) {
                val heightY = deltaY / weight
                val row = index / this.columns
                val column = index % this.columns
                for (i in 1 until weight) {
                    canvas.drawLine(
                        rectShape.left + column * deltaX,
                        rectShape.top + deltaY * row + heightY * i,
                        rectShape.left + column * deltaX + deltaX,
                        rectShape.top + deltaY * row + heightY * i, paint
                    )
                }
            }
        }
    }

    override fun isTouchInShape(touchPoint: PointF): Boolean {
        return rectShape.contains(touchPoint)
    }

    override fun computeSelectedPosition(touchPoint: PointF): Int {
        val row = ((touchPoint.y - rectShape.top) / deltaY).toInt()
        val column = ((touchPoint.x - rectShape.left) / deltaX).toInt()
        val positionSquare = row * columns + column

        val weight = listSquareData[positionSquare].size
        var positionItem = 0
        if (weight != 1) {
            positionItem =
                ((touchPoint.y - rectShape.top - deltaY * row) / (deltaY / weight)).toInt()

        }

        var quantityItemBeforeSquare = 0
        for (i in 0 until positionSquare) {
            quantityItemBeforeSquare += listSquareData[i].size
        }

        return quantityItemBeforeSquare + positionItem
    }
}

data class SquareData(
    val id: Int,
    val weight: Int,
)