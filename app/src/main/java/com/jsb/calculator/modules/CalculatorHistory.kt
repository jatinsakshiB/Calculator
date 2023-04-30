package com.jsb.calculator.modules

data class CalculatorHistory(
    var time: Long,
    var cal: Double,
    val value: String,
    val type: Int,
)
