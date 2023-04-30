package com.jsb.calculator.manager

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher

class KeyboardManager {

    companion object{
        fun changeKeyboard(context: Context, launchKcPermission: ActivityResultLauncher<Intent>) {
            if (checkPermission(context)) {
                (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .showInputMethodPicker()
            } else {
                val enableIntent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
                enableIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                launchKcPermission.launch(enableIntent)
            }
        }

        private fun checkPermission(context: Context): Boolean {
            val packageLocal: String = context.packageName
            var isInputDeviceEnabled = false
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val list = inputMethodManager.enabledInputMethodList

            for (inputMethod in list) {
                val packageName = inputMethod.packageName
                if (packageName == packageLocal) {
                    isInputDeviceEnabled = true
                }
            }
            return isInputDeviceEnabled
        }
    }

}