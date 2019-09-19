package ru.is2si.sisi.presentation.design.dialog

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

class AlertBottomSheetBehaviour<T : View> : BottomSheetBehavior<T> {

    var enableStartNestedScroll: Boolean = false
    var enableInterceptTouchEvent = true

    @Suppress("unused")
    constructor() : super()

    @Suppress("unused")
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onStartNestedScroll(
            coordinatorLayout: CoordinatorLayout,
            child: T,
            directTargetChild: View,
            target: View,
            axes: Int,
            type: Int
    ): Boolean {
        return if (enableStartNestedScroll)
            super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type)
        else
            false
    }

    override fun onInterceptTouchEvent(
            parent: CoordinatorLayout,
            child: T,
            event: MotionEvent
    ): Boolean {
        return if (enableInterceptTouchEvent)
            super.onInterceptTouchEvent(parent, child, event)
        else
            false
    }

}