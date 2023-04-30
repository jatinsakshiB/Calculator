package com.jsb.calculator.utils


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