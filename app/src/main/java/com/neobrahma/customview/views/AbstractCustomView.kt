package com.neobrahma.customview.views

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import androidx.customview.widget.ExploreByTouchHelper

abstract class AbstractCustomView : View {

    protected abstract val exploreByTouchHelper: ExploreByTouchHelper

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        accessibilityLiveRegion = ACCESSIBILITY_LIVE_REGION_POLITE
    }

    override fun dispatchHoverEvent(event: MotionEvent?): Boolean {
        return (event?.let {
            exploreByTouchHelper.dispatchHoverEvent(it)
        } ?: run { false }
                || super.dispatchHoverEvent(event))
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        return (event?.let {
            exploreByTouchHelper.dispatchKeyEvent(it)
        } ?: run { false }
                || super.dispatchKeyEvent(event))
    }

    override fun onFocusChanged(
        gainFocus: Boolean,
        direction: Int,
        previouslyFocusedRect: Rect?
    ) {
        super.onFocusChanged(
            gainFocus, direction,
            previouslyFocusedRect
        )
        exploreByTouchHelper.onFocusChanged(
            gainFocus, direction,
            previouslyFocusedRect
        )
    }
}