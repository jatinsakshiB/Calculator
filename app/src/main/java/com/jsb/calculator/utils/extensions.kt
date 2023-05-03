package com.jsb.calculator.utils

import android.util.Log
import android.widget.EditText
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale


fun String.removeLast() : String {
    var str = this
    if (str.isNotEmpty()) {
        str = str.substring(0, str.length - 1)
    }
    return str
}

fun String.isNumber() : Boolean {
    return this.toDoubleOrNull() != null
}

fun String.getLastNumber() : String{
    val lastNumber = split(Regex("""[\+\-\*/%]""")).lastOrNull()
    return lastNumber ?: ""
}

fun String.equalsAny(list: String) : Boolean{
    for (c in list) {
        if (this == c.toString()) {
            return true
        }
    }
    return false
}

fun EditText.focus(){
    requestFocus()
    setSelection(length())
}

fun String.formatNumber(): String {
    return try {
        val number = this.replace(",", "").toDouble()
        val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
        formatter.maximumFractionDigits = number.getNumberOfDecimals()
        formatter.format(number)
    } catch (e: NumberFormatException) {
        ""
    }
}

fun String.formatAllNumber(): String {
    return try {
        val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
        return this.replace(Regex("(\\d+)(\\.\\d+)?")) {
            val number = it.value.replace(",", "").toDouble()
            formatter.maximumFractionDigits = number.getNumberOfDecimals()
            formatter.format(number)
        }
    } catch (e: NumberFormatException) {
        ""
    }
}

fun Double.getNumberOfDecimals(): Int {
    val bigDecimalValue = BigDecimal(this)
    return bigDecimalValue.scale()
}