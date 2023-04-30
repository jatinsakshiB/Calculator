package com.jsb.calculator.modules

data class HomeData(
    var name: String,
    var image: Int,
    var onClick: (() -> Unit)
)