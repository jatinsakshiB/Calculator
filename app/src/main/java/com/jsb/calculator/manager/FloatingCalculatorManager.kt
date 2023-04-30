package com.jsb.calculator.manager

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.jsb.calculator.Service.FloatingCalculator

class FloatingCalculatorManager(var context: Context) {

    fun toggle() : Boolean{
        if (isRunning()) {
            try {
                context.stopService(Intent(context, FloatingCalculator::class.java))
            } catch (e: java.lang.Exception) {
                Toast.makeText(
                    context,
                    "Your device not supporting floating calculator.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            try {
                context.startService(Intent(context, FloatingCalculator::class.java))
            } catch (e: java.lang.Exception) {
                Toast.makeText(
                    context,
                    "Your device not supporting floating calculator.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return isRunning()
    }

    fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            true
        }
    }

    fun requestPermission(launchFcmPermission: ActivityResultLauncher<Intent>) {
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle("Screen overlay detected")
        alertBuilder.setMessage("Enable 'Draw over other apps' in your system setting.")
        alertBuilder.setPositiveButton(
            "OPEN SETTINGS"
        ) { dialog, which ->
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + context.packageName)
            )
            launchFcmPermission.launch(intent)
        }
        alertBuilder.create().show()
    }

    fun isRunning() = try {
        val serviceClass = FloatingCalculator::class.java
        (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            .getRunningServices(Int.MAX_VALUE)
            .any { it.service.className == serviceClass.name }
    } catch (e: Exception) {
        false
    }
}