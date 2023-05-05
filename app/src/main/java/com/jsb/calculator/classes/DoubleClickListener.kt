package com.jsb.calculator.classes

import android.os.Handler
import android.os.SystemClock
import android.view.View

abstract class DoubleClickListener : View.OnClickListener {
    private var isSingleEvent = false
    private val doubleClickQualificationSpanInMillis: Long = DEFAULT_QUALIFICATION_SPAN
    private var timestampLastClick: Long = 0
    private val handler: Handler = Handler()
    private val runnable: Runnable

    init {
        runnable = Runnable {
            if (isSingleEvent) {
                onSingleClick()
            }
        }
    }

    override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - timestampLastClick < doubleClickQualificationSpanInMillis) {
            isSingleEvent = false
            handler.removeCallbacks(runnable)
            onDoubleClick()
            return
        }
        isSingleEvent = true
        handler.postDelayed(runnable, DEFAULT_QUALIFICATION_SPAN)
        timestampLastClick = SystemClock.elapsedRealtime()
    }

    abstract fun onDoubleClick()
    abstract fun onSingleClick()

    companion object {
        private const val DEFAULT_QUALIFICATION_SPAN: Long = 200
    }
}