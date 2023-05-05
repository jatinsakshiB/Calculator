package com.jsb.calculator.view_manager

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import com.jsb.calculator.keyboard_service.SimpleIME
import com.jsb.calculator.R
import com.jsb.calculator.databinding.Keyboard123Binding

class NumberKeyboardViewManager(
    var context: SimpleIME,
    private var binding: Keyboard123Binding
) : OnClickListener {

    init {
        setUpKeyboard()
    }

    fun refresh(){
        context.addDoneButton(binding.btKeyEnter)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpKeyboard() {
        binding.btKeyNum1.setOnClickListener(this)
        binding.btKeyNum2.setOnClickListener(this)
        binding.btKeyNum3.setOnClickListener(this)
        binding.btKeyNum4.setOnClickListener(this)
        binding.btKeyNum5.setOnClickListener(this)
        binding.btKeyNum6.setOnClickListener(this)
        binding.btKeyNum7.setOnClickListener(this)
        binding.btKeyNum8.setOnClickListener{
            context.addText(" ")
        }
        binding.btKeyNum9.setOnClickListener(this)
        binding.btKeyNum10.setOnClickListener(this)
        binding.btKeyNum11.setOnClickListener(this)
        context.addBackspace(binding.btKeyNum12)
        context.addDoneButton(binding.btKeyEnter)

        binding.btNormalKeyFromNum.setOnClickListener { context.showNormalKeyboard() }
        binding.btNumCCaall.setOnClickListener { context.showCalculatorKeyboard() }
        binding.btKeyNum13.setOnClickListener(this)
        binding.btKeyNum14.setOnClickListener(this)
        binding.btKeyNum15.setOnClickListener(this)
        binding.actionsLlNum2.addView(newText("+"))
        binding.actionsLlNum2.addView(newText("-"))
        binding.actionsLlNum2.addView(newText("/"))
        binding.actionsLlNum2.addView(newText(":"))
        binding.actionsLlNum2.addView(newText("="))
        binding.actionsLlNum2.addView(newText("("))
        binding.actionsLlNum2.addView(newText(")"))
        binding.actionsLlNum2.addView(newText("'"))
        binding.actionsLlNum2.addView(newText("\""))
        binding.actionsLlNum2.addView(newText("&"))
        binding.actionsLlNum2.addView(newText("%"))
        binding.actionsLlNum2.addView(newText("#"))
        binding.actionsLlNum2.addView(newText("@"))
        binding.actionsLlNum2.addView(newText("!"))
        binding.actionsLlNum2.addView(newText("?"))
        binding.actionsLlNum2.addView(newText("*"))
        binding.actionsLlNum2.addView(newText("^"))
        binding.actionsLlNum2.addView(newText("["))
        binding.actionsLlNum2.addView(newText("]"))
        binding.actionsLlNum2.addView(newText("{"))
        binding.actionsLlNum2.addView(newText("}"))
    }

    private fun newText(text: String): TextView {
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val textView = TextView(context)
        textView.textSize = 20f
        textView.gravity = Gravity.CENTER
        textView.text = text
        textView.setPadding(20, 0, 20, 0)
        textView.setBackgroundResource(R.drawable.button_r1)
        textView.layoutParams = layoutParams
        textView.setOnClickListener(this)
        return textView
    }

    override fun onClick(v: View?) {
        context.addText((v as TextView).text.toString())
    }

}