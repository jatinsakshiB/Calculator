package com.jsb.calculator.view_manager

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.jsb.calculator.keyboard_service.SimpleIME
import com.jsb.calculator.activity.CalculatorActivity
import com.jsb.calculator.activity.HistoryActivity
import com.jsb.calculator.databinding.KeyboardCalculatorBinding
import com.jsb.calculator.enums.CalButtons
import com.jsb.calculator.manager.CalculatorManager
import com.jsb.calculator.manager.FloatingCalculatorManager

class CalculatorKeyboardViewManager(
    var context: SimpleIME,
    private var binding: KeyboardCalculatorBinding
) {


    private var currencySymbol = "₹"[0]


    init {
        setUpClicks()
        setUpCalculator()
    }

    fun refresh(){

    }


    private fun setUpClicks() {
        binding.btSymbol.text = currencySymbol.toString()
        binding.btSymbol.setOnClickListener {
            context.addText(currencySymbol.toString())
        }
        binding.btSymbol.setOnLongClickListener { view: View? ->
            binding.currencyLl.visibility = View.VISIBLE
            //₿ ¥ £ ₱ € $ ₹
            fun setSymbol(symbol: String) {
                currencySymbol = symbol[0]
                binding.btSymbol.text = symbol
                binding.currencyLl.visibility = View.GONE
            }
            binding.btSBit1.setOnClickListener { setSymbol("₿") }
            binding.btSBri1.setOnClickListener { setSymbol("£") }
            binding.btSEuro1.setOnClickListener { setSymbol("€") }
            binding.btSPhi1.setOnClickListener { setSymbol("₱") }
            binding.btSRup1.setOnClickListener { setSymbol("₹") }
            binding.btSUsd1.setOnClickListener { setSymbol("$") }
            binding.btSYen1.setOnClickListener { setSymbol("¥") }
            true
        }

        context.addBackspace(binding.btMainBackspace)


        binding.bt3342.setOnClickListener {
            context.showToast("Coming soon")
        }
        binding.btHistory.setOnClickListener { view: View? ->
            val intent = Intent(context, HistoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            Firebase.analytics.logEvent("history_open_from_keyboard", Bundle().apply {
                putString("open", "yes")
            })
        }
        binding.submitBt.setOnClickListener {
            context.addText(binding.calculatedTextEt.text.toString())
            Firebase.analytics.logEvent("submit_calculator_keyboard", Bundle().apply {
                putString("text", binding.calculatedTextEt.text.toString())
            })
        }

        binding.btOpenCalculator.setOnClickListener {
            val intent = Intent(context, CalculatorActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)

            Firebase.analytics.logEvent("calculator_open_from_keyboard", Bundle().apply {
                putString("open", "yes")
            })
        }
        binding.btOpenFloatingCalculator.setOnClickListener {
            val fcm = FloatingCalculatorManager(context)
            if (fcm.checkPermission()){
                fcm.toggle()
            }else{
                context.showToast("Start the floating calculator from the app once")
            }

            Firebase.analytics.logEvent("floating_calculator_open_from_keyboard", Bundle().apply {
                putString("open", "yes")
            })
        }

        binding.btABCc.setOnClickListener {
            context.showNormalKeyboard()
        }
        binding.bt12233.setOnClickListener {
            context.showNumberKeyboard()
        }
    }

    private fun setUpCalculator() {
        val cm = CalculatorManager(binding.calculatedTextEt, binding.liveCalculatedText, 2)
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
        cm.addBt(binding.btCalculatorBackspace, CalButtons.Backspace)

        cm.onClick = {
            //context.playSound()
        }
    }


}