package com.neobrahma.customview.views.remotecontrol

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.customview.widget.ExploreByTouchHelper
import com.neobrahma.customview.views.AbstractCustomView

abstract class AbstractRemoteControlView : AbstractCustomView {

    var items: List<RemoteControlItem> = emptyList()
    var listener: RemoteControlListener? = null
    var columns: Int = 1
    private var hoverPosition = -1

    final override val exploreByTouchHelper: ExploreByTouchHelper =
        RemoteControlExploreByTouchHelper(this)

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
        onDrawShape(canvas, hoverPosition)
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
        val hoverPositionTmp = if (isTouchInShape(touchPoint)) {
            computeSelectedPosition(touchPoint)
        } else {
            -1
        }
        if (hoverPosition != hoverPositionTmp) {
            hoverPosition = hoverPositionTmp
            invalidate()
        }
    }

    private fun onActionUp(touchPoint: PointF) {
        if (isTouchInShape(touchPoint)) {
            performActionClick()
        }
        invalidate()
    }

    private fun performActionClick() {
        listener?.onSelectedItem(hoverPosition)
        hoverPosition = -1
    }

    protected abstract fun initVariables(width: Int, height: Int)
    protected abstract fun initBounds()
    protected abstract fun onDrawShape(canvas: Canvas, hoverPosition: Int)
    protected abstract fun isTouchInShape(touchPoint: PointF): Boolean
    protected abstract fun computeSelectedPosition(touchPoint: PointF): Int

    inner class RemoteControlExploreByTouchHelper(host: View) :
        ExploreByTouchHelper(host) {

        override fun getVirtualViewAt(x: Float, y: Float): Int {
            return computeSelectedPosition(PointF(x, y))
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
                    hoverPosition = virtualViewId
                    performActionClick()
                    return true
                }
            }
            return false
        }
    }

    fun interface RemoteControlListener {
        fun onSelectedItem(position: Int)
    }
}