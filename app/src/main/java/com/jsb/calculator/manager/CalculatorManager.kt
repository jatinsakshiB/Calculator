package com.jsb.calculator.manager

import android.os.CountDownTimer
import android.widget.EditText
import android.widget.TextView
import com.jsb.calculator.enums.CalButtons
import com.jsb.calculator.modules.CalculatorHistory
import com.jsb.calculator.utils.equalsAny
import com.jsb.calculator.utils.focus
import com.jsb.calculator.utils.formatAllNumber
import com.jsb.calculator.utils.formatNumber
import com.jsb.calculator.utils.getLastNumber
import com.jsb.calculator.utils.isNumber
import com.jsb.calculator.utils.removeLast
import org.mariuszgromada.math.mxparser.Expression


class CalculatorManager(
    private val calculatedTextEt: EditText,
    private val liveCalculatedText: TextView,
    private val calculateFrom: Int
) {

    var cdt: CountDownTimer? = null

    private var currentText = ""
    private var calculated = currentText
    private var calculatedDouble: Double = 0.0

    init {
        calculatedTextEt.keyListener = null
        handleClear()
    }


    fun handleBackspace(){
        if (currentText.isNotEmpty()) {
            currentText = currentText.removeLast()
        }
        calculate()
    }

    private fun handleClear() {
        currentText = ""
        calculate()
    }

    private fun appendNumber(digit: String) {
        currentText += if (currentText.isNotEmpty()){
            if (currentText.last().toString() == "%"){
                "×$digit"
            }else digit
        }else digit
        calculate()
    }

    private fun handleOperator(op: String) {
        // empty hai toh sirif minus hi laga sakte hai
        if (currentText.isEmpty()) {
            if (op == "-"){
                currentText = op
            }
        }else{
            if (currentText != "-") {
                // last char lenga currentText text se
                val last = currentText.last().toString()

                if (last == "-"){
                    if (op == "-") return
                    if (!currentText.removeLast().last().toString().isNumber()){
                        currentText = currentText.removeLast()
                        handleOperator(op)
                        return
                    }
                }

                //ager last me plus hai toh or op me minus toh plus ko hatake minus karenga or ager kuch or hai jese Multiply, Divide to minus aa jayenga uske bad
                currentText = if (op == "-"){
                    if (last.equalsAny("+.")){
                        currentText.removeLast()+op
                    }else{
                        currentText+op
                    }
                }else{
                    // check karenga last int hai kya ager int hao to hi Multiply, Divide, Plus add karenga warna Multiply, Divide, Plus, Minus ko remove karke new rakhenga
                    if (last.isNumber() || last.equalsAny("%")){
                        currentText+op
                    }else{
                        currentText.removeLast()+op
                    }
                }
            }
        }
        calculate()
    }

    private fun handleDot() {
        if (currentText.isEmpty()){
            currentText += "0."
        }else{
            val last = currentText.last().toString()
            if (last != "."){
                currentText = if (last.isNumber()){
                    if ("." in currentText.getLastNumber()) {
                        return
                    }
                    "$currentText."
                }else{
                    "${currentText}0."
                }
            }
        }
        calculate()
    }

    private fun handlePercentage() {
        if (currentText.isNotEmpty()){
            val last = currentText.last().toString()
            currentText = if (last.isNumber() || last == "%"){
                "$currentText%"
            }else{
                currentText.removeLast()+'%'
            }
        }
        calculate()
    }

    private fun handleEqualsTo() {
        calculate()
        LocalStorage(calculatedTextEt.context).saveCalculatorHistory(
            CalculatorHistory(
                System.currentTimeMillis(),
                calculatedDouble,
                calculatedTextEt.text.toString(),
                calculateFrom
            )
        )
        currentText = calculated
        calculated = ""
        calculatedTextEt.setText(currentText.formatNumber())
        liveCalculatedText.text = calculated.formatNumber()
        calculatedTextEt.focus()
    }



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
    }


    private fun calculate() {
        var calculate = currentText
        if (calculate.isNotEmpty()){
            var last = calculate.last().toString()
            if (!last.isNumber() && last != "%"){
                calculate = calculate.removeLast()
                if (calculate.isNotEmpty()) {
                    last = calculate.last().toString()
                    if (!last.isNumber() && last != "%"){
                        calculate = calculate.removeLast()
                    }
                }
            }
            if (calculate.isNotEmpty()){
                calculate = calculate
                    .replace("x", "*")
                    .replace("×", "*")
                    .replace("÷", "/")
                    .replace("%", "/100")


                val expression = Expression(calculate)
                calculatedDouble =  expression.calculate()
                calculated = calculatedDouble.toString()
                // remove unnecessary 00 and dot
                calculated = calculated.replace(Regex("(\\.\\d*?)0+([^\\d]|$)"), "$1$2")
                calculated = calculated.replace(Regex("\\.$"), "")

            }
        }else{
            calculated = "0"
        }
        updateText()
    }

    private fun updateText(){
        calculatedTextEt.setText(currentText.formatAllNumber())
        liveCalculatedText.text = calculated.formatNumber()
        calculatedTextEt.focus()
    }

}