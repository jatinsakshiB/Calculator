package com.jsb.calculator.manager

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.jsb.calculator.modules.CalculatorHistory

class LocalStorage(val context: Context) {

    var sharedPreferences: SharedPreferences = context.getSharedPreferences("com.jsb.calculator", Context.MODE_PRIVATE)

    fun saveCalculatorHistory(calculatorHistory: CalculatorHistory) {
        val calculatorHistoryMList = getCalculatorHistoryList().toMutableList()
        calculatorHistoryMList.add(calculatorHistory)
        saveCalculatorHistoryList(calculatorHistoryMList)
    }
    fun saveCalculatorHistoryList(calculatorHistoryList: List<CalculatorHistory>) {
        val json = Gson().toJson(calculatorHistoryList)
        sharedPreferences.edit().putString("CalHis", json).apply()
    }

    fun getCalculatorHistoryList(): List<CalculatorHistory>{
        val calculatorHistoryListString = sharedPreferences.getString("CalHis", null)
        val type = object : TypeToken<List<CalculatorHistory>>() {}.type
        val calculatorHistoryList = try {
            Gson().fromJson(calculatorHistoryListString, type)
        }catch (e: JsonSyntaxException){
            listOf<CalculatorHistory>()
        }
        return calculatorHistoryList
    }
}