package com.jsb.calculator.manager

import android.os.CountDownTimer
import android.widget.EditText
import android.widget.TextView
import com.jsb.calculator.enums.CalButtons
import com.jsb.calculator.utils.isNumber
import com.jsb.calculator.utils.removeLast
import org.mariuszgromada.math.mxparser.Expression


class CalculatorManager(
    val calculatedTextEt: EditText,
    val liveCalculatedText: TextView
) {

    var cdt: CountDownTimer? = null

    private var currentText = ""


    init {
        calculatedTextEt.keyListener = null
        handleClear()
    }


    fun handleBackspace(){
        if (currentText.isNotEmpty()) {
            currentText = currentText.removeLast()
        }
    }

    private fun handleClear() {
        currentText = ""
    }

    private fun appendNumber(digit: String) {
        currentText += digit
    }

    private fun handleOperator(op: String) {
        // empty hai toh sirif minus hi laga sakte hai
        if (op != "-"){
            if (currentText.isEmpty()) return
        }
        // last char lenga currentText text se
        val last = currentText.last().toString()

        //ager last me plus hai toh or op me minus toh plus ko hatake minus karenga or ager kuch or hai jese Multiply, Divide to minus aa jayenga uske bad
        if (op == "-"){
            currentText += if (last == "+"){
                currentText.removeLast()+op
            }else{
                op
            }
            return
        }

        // check karenga last int hai kya ager int hao to hi Multiply, Divide, Plus add karenga warna Multiply, Divide, Plus, Minus ko remove karke new rakhenga
        currentText += if (last.isNumber()){
            op
        }else{
            currentText.removeLast()+op
        }
    }

    private fun handleDot() {
        if (currentText.isEmpty()){
            currentText += "0."
        }else{
            val last = currentText.last().toString()
            currentText += if (last.isNumber()){
                "$currentText."
            }else{
                "${currentText}0."
            }
        }
    }

    private fun handlePercentage() {
        if (currentText.isEmpty()) return
        val last = currentText.last().toString()
        currentText += if (last.isNumber()){
            "$currentText%"
        }else{
            currentText.removeLast()+'%'
        }
        calculate()
    }

    private fun handleEqualsTo() {

    }






    /*fun handleEqualsTo(){
        button.setOnClickListener {
            var aa = calculatedTextEt.text.toString()
            if (aa.isNotEmpty()) {
                if (aa.endsWith("+") || aa.endsWith("%") || aa.endsWith(".") || aa.endsWith("×") || aa.endsWith(
                        "÷"
                    ) || aa.endsWith("-")
                ) {
                    aa = method(aa)
                }
                if (aa == "-") {
                    return@setOnClickListener
                }
                val valll = aa
                val string = aa
                    .replace("x".toRegex(), "*")
                    .replace("×".toRegex(), "*")
                    .replace("÷".toRegex(), "/")
                    .replace("%".toRegex(), "/100")
                val e = Expression(string)
                val c = e.calculate()
                var calculate = c.toString()
                if (calculate.endsWith(".0")) {
                    calculate = method(calculate)
                    calculate = method(calculate)
                } else if (calculate.endsWith(".00")) {
                    calculate = method(calculate)
                    calculate = method(calculate)
                    calculate = method(calculate)
                }
                val calHis = CalHis()
                calHis.time = System.currentTimeMillis()
                calHis.type = 0
                calHis.cal = c
                calHis.value = valll
                saveCalHisList(calHis)
                binding.calculatedTextEt.setText(calculate)
            }
        }
    }*/

    fun addBt(button: TextView, buttonType: CalButtons) {

        button.setOnLongClickListener {
            when(buttonType){
                CalButtons.Backspace -> {
                    cdt = object : CountDownTimer((1000 * 50).toLong(), 50) {
                        override fun onTick(millisUntilFinished: Long) {
                            if (!button.isPressed) {
                                cdt!!.cancel()
                            } else {
                                handleBackspace()
                            }
                        }
                        override fun onFinish() {}
                    }
                    cdt!!.start()
                    return@setOnLongClickListener true
                }
                else -> {}
            }
            false
        }

        button.setOnClickListener {
            when(buttonType) {
                CalButtons.One -> appendNumber("1")
                CalButtons.Two -> appendNumber("2")
                CalButtons.Three -> appendNumber("3")
                CalButtons.Four -> appendNumber("4")
                CalButtons.Five -> appendNumber("5")
                CalButtons.Six -> appendNumber("6")
                CalButtons.Seven -> appendNumber("7")
                CalButtons.Eight -> appendNumber("8")
                CalButtons.Nine -> appendNumber("9")
                CalButtons.Zero -> appendNumber("0")
                CalButtons.DoubleZero -> appendNumber("00")
                CalButtons.Multiply -> handleOperator("×")
                CalButtons.Divide -> handleOperator("÷")
                CalButtons.Dot -> handleDot()
                CalButtons.Minus -> handleOperator("-")
                CalButtons.Percentage -> handlePercentage()
                CalButtons.Plus -> handleOperator("+")
                CalButtons.EqualsTo -> handleEqualsTo()
                CalButtons.Backspace -> handleBackspace()
                CalButtons.Clear -> handleClear()
            }
        }
        calculate()
    }


    private fun calculate() {
        /*var aa = binding!!.et1.text.toString()
        if (!aa.isEmpty()) {
            if (aa == "-") {
                binding!!.liveCalculator.text = "00"
                return
            }
            if (aa.endsWith("+") || aa.endsWith("%") || aa.endsWith(".") || aa.endsWith("×") || aa.endsWith(
                    "÷"
                ) || aa.endsWith("-")
            ) {
                aa = method(aa)
            }
            if (aa == "-") {
                return
            }
            val string231 = aa
                .replace("x".toRegex(), "*")
                .replace("×".toRegex(), "*")
                .replace("÷".toRegex(), "/")
                .replace("%".toRegex(), "/100")
            val e = Expression(string231)
            var tt = e.calculate().toString()
            if (tt.endsWith(".0")) {
                tt = method(tt)
                tt = method(tt)
            } else if (tt.endsWith(".00")) {
                tt = method(tt)
                tt = method(tt)
                tt = method(tt)
            }
            binding!!.liveCalculator.text = tt
        } else {
            binding!!.liveCalculator.text = "00"
        }*/

        updateText()
    }

    fun updateText(){
        calculatedTextEt.setText(currentText)
        liveCalculatedText.text = "00"
    }

}