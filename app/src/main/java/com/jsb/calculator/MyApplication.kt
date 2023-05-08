package com.jsb.calculator

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.jsb.calculator.utils.AppData
import com.jsb.calculator.utils.Utils
import com.onesignal.OneSignal

class MyApplication : Application(), ActivityLifecycleCallbacks, LifecycleObserver {


    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)

        // Enable verbose OneSignal logging to debug issues if needed.
        if (Utils.testMode){
            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        }

        // OneSignal Initialization
        OneSignal.initWithContext(this)
        OneSignal.setAppId(AppData.oneSignalAppId)

    }


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
    interface OnShowAdCompleteListener {
        fun onShowAdComplete()
    }
}