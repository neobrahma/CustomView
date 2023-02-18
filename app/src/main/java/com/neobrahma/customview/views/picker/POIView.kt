package com.neobrahma.customview.views.picker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.neobrahma.customview.R
import com.neobrahma.customview.views.helper.*

class POIView : View {

    companion object {
        private const val START = 90f
    }

    private var heightView = 0f

    private val stroke = context.resources.getDimension(R.dimen.stroke_poi)
    private val iconSize = context.resources.getDimension(R.dimen.icon_size)
    private val iconPOISize = iconSize / 1.5
    private val rectPOI = RectF(0f, 0f, 0f, 0f)

    private val centerRectPoint = PointF(0f, 0f)
    private val iconCenterPoint = PointF(0f, 0f)
    private val iconPoint = PointF(0f, 0f)
    private val pointO = PointF(0f, 0f)

    private val path = Path()

    private val bitmap =
        getVectorBitmapFromDrawable(
            context,
            R.drawable.ic_64_object_base_gpe_lum7,
            iconPOISize.toInt()
        )

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = stroke * 4
        strokeCap = Paint.Cap.ROUND
    }

    private var selectedItem = -1
    private val POIBulbs: MutableList<PickerView.POIBulb> = mutableListOf()

    var listener: POIListener? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val padding = stroke * 2
        heightView = h.toFloat()

        rectPOI.apply {
            top = padding
            left = (w - iconSize) / 2
            bottom = top + iconSize
            right = left + iconSize
        }

        iconCenterPoint.apply {
            x = rectPOI.centerX()
            y = rectPOI.centerY()
        }

        centerRectPoint.apply {
            x = w / 2f
            y = h / 2f
        }

        pointO.apply {
            x = w.toFloat()
            y = h / 2f
        }

        iconPoint.apply {
            x = rectPOI.left + ((rectPOI.width() - iconPOISize) / 2).toFloat()
            y = rectPOI.top + ((rectPOI.width() - iconPOISize) / 2).toFloat()
        }
        //draw POI's shape
        path.addArc(rectPOI, 45f, -270f)
        path.lineTo(rectPOI.centerX(), rectPOI.bottom)
        path.close()

        computeCenterPointPoi()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        POIBulbs.forEach { POIBulb ->
            paint.apply {
                style = Paint.Style.STROKE
                color = POIBulb.color
            }
            canvas.save()
            canvas.rotate(POIBulb.angle + START, centerRectPoint.x, centerRectPoint.y)

            drawStroke(canvas)


            drawBackground(canvas, POIBulb.color)
            canvas.drawBitmap(
                bitmap,
                iconPoint.x,
                iconPoint.y,
                null
            )

            canvas.restore()
        }
    }

    private fun drawStroke(canvas: Canvas) {
        paint.apply {
            style = Paint.Style.STROKE
            color = ContextCompat.getColor(context, R.color.white)
        }
        canvas.drawPath(path, paint)
    }

    private fun drawBackground(canvas: Canvas, bulbColor: Int) {
        paint.apply {
            style = Paint.Style.FILL
            color = bulbColor
        }
        canvas.drawPath(path, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchPoint = PointF(event.x, event.y)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onActionDown(touchPoint)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                onActionMove(touchPoint)
            }
            MotionEvent.ACTION_UP -> {
                onActionUp()
            }
        }
        return true
    }

    private fun onActionDown(touchPoint: PointF){
        POIBulbs.forEachIndexed { index, poiBulb ->
            if(computeDistanceSegment(poiBulb.centerPoint, touchPoint)<= iconSize/2){
                selectedItem = index
            }
        }
        if(selectedItem != -1){
            val poi = POIBulbs[selectedItem]
            POIBulbs.removeAt(selectedItem)
            POIBulbs.add( poi)
            selectedItem = POIBulbs.size-1
        }
    }

    private fun onActionMove(touchPoint: PointF) {
        if(selectedItem != -1) {
            var angleTmp = computeAngleAOB(centerRectPoint, touchPoint, pointO)
            if (touchPoint.y < pointO.y) {
                angleTmp = 180f + (180f - angleTmp)
            }
            POIBulbs[selectedItem].apply {
                angle = angleTmp.toFloat()
                centerPoint.apply {
                    val p = updateCenterPointPoi(angleTmp.toFloat())
                    x = p.x
                    y = p.y
                }
            }
            invalidate()
            listener?.updateColor(angleTmp.toInt())
        }
    }

    private fun onActionUp(){
        if(selectedItem>0) {
            listener?.selectedColor(POIBulbs[selectedItem].id, POIBulbs[selectedItem].angle.toInt())
            selectedItem = -1
        }
    }

    fun updatePOIBulb(list: List<PickerView.POIBulb>) {
        POIBulbs.addAll(list)
        computeCenterPointPoi()
        invalidate()
    }

    private fun computeCenterPointPoi() {
        POIBulbs.forEach {
            val pointToto = computePositionOnCircle(
                convertValueToDegree(it.angle - START),
                iconCenterPoint,
                centerRectPoint
            )

            it.centerPoint.apply {
                x = pointToto.x
                y = heightView - pointToto.y
            }
        }
    }

    private fun updateCenterPointPoi(angle: Float): PointF {
        val pointToto = computePositionOnCircle(
            convertValueToDegree(angle - START),
            iconCenterPoint,
            centerRectPoint
        )

        return PointF(pointToto.x, heightView - pointToto.y)
    }

    fun updateColor(color: Int) {
        POIBulbs[selectedItem].color = color
        invalidate()
    }

    interface POIListener {
        fun updateColor(position: Int)
        fun selectedColor(id : Int, position : Int)
    }
}