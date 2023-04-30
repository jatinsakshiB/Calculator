package com.jsb.calculator.utils

class Log(m: String, logs: Logs = Logs.COMMON) {
    init {
        if (Utils.testMode){
            println("${logs.name}: $m")
        }
    }
}

enum class Logs{
    COMMON, API, ADS, HOME, LocalStorage
}
