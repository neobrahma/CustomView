package com.neobrahma.customview.views.switch

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat

abstract class AbstractSwitchView : View {

    var isChecked = false
        set(value) {
            field = value
            selectedPosition = if (value) {
                0
            } else {
                1
            }
        }
    private var selectedPosition = 1
    private var isHoverDisplayed = false

    var items: List<SwitchItem> = emptyList()
    var listener: SwitchListener? = null

    private var action: String = ""

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initVariables(w, h)
        initBounds()
        updateDescription()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        onDrawShape(canvas, selectedPosition, isHoverDisplayed)
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
        val isNewHoverDisplayed = isTouchInShape(touchPoint)
        if(isHoverDisplayed != isNewHoverDisplayed){
            isHoverDisplayed = isNewHoverDisplayed
            invalidate()
        }
    }

    private fun onActionUp(touchPoint: PointF) {
        isHoverDisplayed = false
        if (isTouchInShape(touchPoint)) {
            performActionClick()
        }
        invalidate()
    }

    private fun performActionClick() {
        isChecked = !isChecked
        updateDescription()
        listener?.onClickItem(isChecked)
    }

    private fun updateDescription() {
        action = resources.getString(items[selectedPosition].idContentDescriptionAction)
        contentDescription = resources.getString(items[selectedPosition].idContentDescriptionView)

        ViewCompat.replaceAccessibilityAction(
            this, AccessibilityNodeInfoCompat
                .AccessibilityActionCompat.ACTION_CLICK,
            action
        ) { _, _ ->
            performActionClick()
            invalidate()
            true
        }
    }

    abstract fun initVariables(width: Int, height: Int)
    abstract fun initBounds()
    abstract fun onDrawShape(
        canvas: Canvas,
        selectedPosition: Int,
        isHoverDisplayed: Boolean
    )

    abstract fun isTouchInShape(touchPoint: PointF): Boolean

    fun interface SwitchListener {
        fun onClickItem(isChecked: Boolean)
    }
}