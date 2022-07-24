package com.neobrahma.customview.views.joypad

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.PointF
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.contains
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.customview.widget.ExploreByTouchHelper
import com.neobrahma.customview.views.AbstractCustomView

abstract class AbstractJoypadView : AbstractCustomView {

    var items: List<JoypadItem> = emptyList()
    var listener: JoypadListener? = null

    private var selectedPosition = -1

    final override val exploreByTouchHelper: ExploreByTouchHelper = JoypadExploreByTouchHelper(this)

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        ViewCompat.setAccessibilityDelegate(this, exploreByTouchHelper)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initVariables(w, h)
        initBounds()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        onDrawShape(canvas, selectedPosition)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchPoint = PointF(event.x, event.y)
        when (event.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                onActionDownAndMove(touchPoint)
            }
            MotionEvent.ACTION_UP -> {
                onActionUp(touchPoint)
            }
        }
        return true
    }

    private fun onActionDownAndMove(touchPoint: PointF) {
        val selectedPositionTmp = if (isTouchInShape(touchPoint)) {
            computeSelectedPosition(touchPoint)
        } else {
            -1
        }
        if (selectedPosition != selectedPositionTmp) {
            selectedPosition = selectedPositionTmp
            invalidate()
        }
    }

    private fun onActionUp(touchPoint: PointF) {
        if (isTouchInShape(touchPoint)) {
            performActionClick()
        }
        selectedPosition = -1
        invalidate()
    }

    private fun performActionClick() {
        listener?.onSelectedItem(selectedPosition)
    }

    abstract fun initVariables(width: Int, height: Int)
    abstract fun initBounds()
    abstract fun onDrawShape(canvas: Canvas, selectedPosition: Int)

    abstract fun isTouchInShape(touchPoint: PointF): Boolean
    abstract fun computeSelectedPosition(touchePointF: PointF): Int

    inner class JoypadExploreByTouchHelper(host: View) :
        ExploreByTouchHelper(host) {

        override fun getVirtualViewAt(x: Float, y: Float): Int {
            val pointClick = Point(x.toInt(), y.toInt())
            items.forEachIndexed { index, joypadItem ->
                if (joypadItem.bound.contains(pointClick)) {
                    return index
                }
            }
            return HOST_ID
        }

        override fun getVisibleVirtualViews(virtualViewIds: MutableList<Int>?) {
            for (i in items.indices) {
                virtualViewIds?.add(i)
            }
        }

        override fun onPopulateNodeForVirtualView(
            virtualViewId: Int,
            node: AccessibilityNodeInfoCompat
        ) {
            node.text = resources.getString(items[virtualViewId].idContentDescriptionView)
            node.addAction(
                AccessibilityNodeInfoCompat
                    .AccessibilityActionCompat.ACTION_CLICK
            )
            node.setBoundsInParent(items[virtualViewId].bound)
        }

        override fun onPerformActionForVirtualView(
            virtualViewId: Int,
            action: Int,
            arguments: Bundle?
        ): Boolean {
            when (action) {
                AccessibilityNodeInfoCompat.ACTION_CLICK -> {
                    selectedPosition = virtualViewId
                    performActionClick()
                    selectedPosition = -1
                    return true
                }
            }
            return false
        }
    }

    fun interface JoypadListener {
        fun onSelectedItem(position: Int)
    }
}