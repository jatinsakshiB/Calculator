package com.jsb.calculator.manager

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceManager
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.jsb.calculator.modules.CalculatorHistory
import com.jsb.calculator.modules.MyFont
import java.lang.Exception

class LocalStorage(val context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("com.jsb.calculator", Context.MODE_PRIVATE)
    var defaultSharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun saveCalculatorHistory(calculatorHistory: CalculatorHistory) {
        if (defaultSharedPreferences.getBoolean("settingsSaveHistory", true)){
            val calculatorHistoryMList = getCalculatorHistoryList().toMutableList()
            calculatorHistoryMList.add(calculatorHistory)
            saveCalculatorHistoryList(calculatorHistoryMList)
        }
    }
    fun saveCalculatorHistoryList(calculatorHistoryList: List<CalculatorHistory>) {
        val json = Gson().toJson(calculatorHistoryList)
        try {
            sharedPreferences.edit().putString("CalHis", json).apply()
        }catch (e: Exception){
            FirebaseCrashlytics.getInstance().log(e.toString())
        }
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

    fun getKeyboardType(): String{
        val keyboardType = defaultSharedPreferences.getString("settingsTextKeyboardType", "qwert")
        return keyboardType ?: "qwert"
    }

    fun getDefaultCurrency(): String{
        val defaultCurrency = defaultSharedPreferences.getString("settingsDefaultCurrency", "₹")
        return defaultCurrency ?: "₹"
    }

    fun saveDefaultCurrency(currency: String){
        defaultSharedPreferences.edit().putString("settingsDefaultCurrency", currency).apply()
    }

    fun getSubmitAction(): String{
        val submitAction = defaultSharedPreferences.getString("settingsSubmitAction", "text")
        return submitAction ?: "text"
    }

    fun saveDefKeyboard(keyboard: String){
        sharedPreferences.edit().putString("defKeyboard", keyboard).apply()

        Firebase.analytics.logEvent("def_keyboard", Bundle().apply {
            putString("keyboard", keyboard)
        })
    }

    fun getDefKeyboard() : String{
        return when (defaultSharedPreferences.getString("settingsDefaultKeyword", "last")) {
            "last" -> {
                sharedPreferences.getString("defKeyboard", "calculator") ?: "calculator"
            }
            "calculator" -> {
                "calculator"
            }
            "number" -> {
                "number"
            }
            else -> {
                "normal"
            }
        }
    }

}