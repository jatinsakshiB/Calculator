package com.jsb.calculator.manager

import android.content.Context
import android.content.SharedPreferences

class LocalStorage(val context: Context) {

    var sharedPreferences: SharedPreferences = context.getSharedPreferences("com.jsb.calculator", Context.MODE_PRIVATE)

}