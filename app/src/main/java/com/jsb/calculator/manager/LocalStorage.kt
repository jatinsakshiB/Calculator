package com.jsb.calculator.manager

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.jsb.calculator.modules.CalculatorHistory
import com.jsb.calculator.modules.MyFont

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
        if (calculatorHistoryListString == null){
            return listOf()
        }
        val calculatorHistoryList = try {
            Gson().fromJson(calculatorHistoryListString, type)
        }catch (e: JsonSyntaxException){
            listOf<CalculatorHistory>()
        }
        return calculatorHistoryList
    }


    fun saveFont(myFont: MyFont){
        sharedPreferences.edit().putString("defFont", Gson().toJson(myFont)).apply()
    }

    fun getFont(): MyFont{
        val fontString = sharedPreferences.getString("defFont", "")
        return if (fontString.isNullOrEmpty()){
            MyFont()
        }else{
            Gson().fromJson(fontString, MyFont::class.java)
        }
    }

    fun hasFont(): Boolean{
        val fontString = sharedPreferences.getString("defFont", "")
        return !fontString.isNullOrEmpty()
    }


    fun saveDefKeyboard(keyboard: String){
        sharedPreferences.edit().putString("defKeyboard", keyboard).apply()

        Firebase.analytics.logEvent("def_keyboard", Bundle().apply {
            putString("keyboard", keyboard)
        })
    }

    fun getDefKeyboard() : String{
        return sharedPreferences.getString("defKeyboard", "calculator") ?: "calculator"
    }
}