package com.jsb.calculator.activity

import android.os.Bundle
import com.jsb.calculator.manager.CalculatorManager
import androidx.appcompat.app.AppCompatActivity
import com.jsb.calculator.databinding.ActivityCalculatorBinding
import com.jsb.calculator.enums.CalButtons

class CalculatorActivity : AppCompatActivity() {


    lateinit var binding: ActivityCalculatorBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setUpView()

    }

    private fun setUpView() {
        val cm = CalculatorManager(binding.calculatedTextEt, binding.liveCalculatedText)

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



}