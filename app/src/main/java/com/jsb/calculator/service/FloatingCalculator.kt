package com.jsb.calculator.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.Toast
import com.jsb.calculator.databinding.FloatingCalculatorBinding
import com.jsb.calculator.enums.CalButtons
import com.jsb.calculator.manager.CalculatorManager

class FloatingCalculator : Service() {

    lateinit var binding: FloatingCalculatorBinding

    var windowManager: WindowManager? = null


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()
        val inflate = baseContext
            .getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = FloatingCalculatorBinding.inflate(inflate)



        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val windowParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_TOAST,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
            x = 0
            y = 0
        }

        binding.closeBt.setOnClickListener { stopSelf() }

        binding.calLL.visibility = View.VISIBLE
        binding.showCal.visibility = View.GONE

        binding.showCal.setOnTouchListener(DragTouchListener(windowManager!!, windowParams, true))

        binding.minimizeBt.setOnClickListener {
            binding.calLL.visibility = View.GONE
            binding.showCal.visibility = View.VISIBLE
            windowManager!!.updateViewLayout(binding.root, windowParams)
        }
        binding.dragLL.setOnTouchListener(DragTouchListener(windowManager!!, windowParams))
        try {
            windowManager!!.addView(binding.root, windowParams)
            setUpCalculator()
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Your device not supporting floating calculator.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setUpCalculator() {
        val cm = CalculatorManager(binding.calculatedTextEt, binding.liveCalculatedText, 0)

        cm.addBt(binding.bt1, CalButtons.One)
        cm.addBt(binding.bt2, CalButtons.Two)
        cm.addBt(binding.bt3, CalButtons.Three)
        cm.addBt(binding.bt4, CalButtons.Four)
        cm.addBt(binding.bt5, CalButtons.Five)
        cm.addBt(binding.bt6, CalButtons.Six)
        cm.addBt(binding.bt7, CalButtons.Seven)
        cm.addBt(binding.bt8, CalButtons.Eight)
        cm.addBt(binding.bt9, CalButtons.Nine)
        cm.addBt(binding.bt0, CalButtons.Zero)
        cm.addBt(binding.bt00, CalButtons.DoubleZero)
        cm.addBt(binding.btMultiply, CalButtons.Multiply)
        cm.addBt(binding.btDivide, CalButtons.Divide)
        cm.addBt(binding.btDot, CalButtons.Dot)
        cm.addBt(binding.btMinus, CalButtons.Minus)
        cm.addBt(binding.btPercentage, CalButtons.Percentage)
        cm.addBt(binding.btPlus, CalButtons.Plus)
        cm.addBt(binding.btEqualsTo, CalButtons.EqualsTo)
        cm.addBt(binding.btClear, CalButtons.Clear)
        cm.addBt(binding.btBackspace, CalButtons.Backspace)
    }


    override fun onDestroy() {
        super.onDestroy()
        windowManager?.removeView(binding.root)
        stopSelf()
    }


    private inner class DragTouchListener(
        private val windowManager: WindowManager,
        private val windowParams: WindowManager.LayoutParams,
        private val open: Boolean = false
    ) : OnTouchListener {

        private var updatedParameters = windowParams
        private var x = 0.0
        private var y = 0.0
        private var pressedX = 0.0
        private var pressedY = 0.0
        private var oldTimeInMillis: Long = 0

        override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = updatedParameters.x.toDouble()
                    y = updatedParameters.y.toDouble()
                    pressedX = motionEvent.rawX.toDouble()
                    pressedY = motionEvent.rawY.toDouble()
                    oldTimeInMillis = System.currentTimeMillis()
                }

                MotionEvent.ACTION_MOVE -> {
                    updatedParameters.y = (y + (motionEvent.rawY - pressedY)).toInt()
                    updatedParameters.x = (x + (motionEvent.rawX - pressedX)).toInt()
                    windowManager.updateViewLayout(binding.root, updatedParameters)
                }

                MotionEvent.ACTION_UP -> {
                    if (open){
                        if (oldTimeInMillis + 200 > System.currentTimeMillis()) {
                            binding.calLL.visibility = View.VISIBLE
                            binding.showCal.visibility = View.GONE
                            windowManager.updateViewLayout(binding.root, windowParams)
                        }
                    }
                }
            }
            return true
        }
    }

}