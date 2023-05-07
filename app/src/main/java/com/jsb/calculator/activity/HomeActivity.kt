package com.jsb.calculator.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.jsb.calculator.R
import com.jsb.calculator.adapter.HomeDataAdapter
import com.jsb.calculator.databinding.ActivityHomeBinding
import com.jsb.calculator.manager.AdsManager
import com.jsb.calculator.manager.FloatingCalculatorManager
import com.jsb.calculator.manager.KeyboardManager
import com.jsb.calculator.modules.HomeData
import com.jsb.calculator.utils.focus

class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding

    private var adsManager: AdsManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AdsManager(this).initialize {
            adsManager = it
            it.setUpBanner(binding.adView)
            it.loadInterstitial()
            it.loadAppOpen {it2 ->
                it2.showAppOpen(this)
            }
        }


        checkAppUpdate()

        val adapter = HomeDataAdapter()
        binding.data.adapter = adapter


        val launchActivities = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            adsManager?.showInterstitial(this)
        }


        val launchKcPermission = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            KeyboardManager.changeKeyboard(this)
        }
        adapter.addItem(HomeData("Keyboard\nCalculator", R.drawable.ic_keyboard_white){
            KeyboardManager.changeKeyboard(this, launchKcPermission)
            binding.editText.requestFocus()
        })

        adapter.addItem(HomeData("Calculator", R.drawable.ic_calculator2){
            launchActivities.launch(Intent(this, CalculatorActivity::class.java))
        })

        val fcm = FloatingCalculatorManager(this)
        val launchFcmPermission = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (fcm.checkPermission()){
                fcm.toggle()
            }else{
                Toast.makeText(this, "Please grant overlay permission to use floating keyboard.", Toast.LENGTH_LONG).show()
            }
        }
        val fcImage = if (fcm.isRunning()){
            R.drawable.ic_floating_cal_off_white
        }else{
            R.drawable.ic_floating_cal_white
        }
        adapter.addItem(HomeData("Floating\nCalculator", fcImage){
            if (fcm.checkPermission()){
                val fcImage = if (fcm.toggle()){
                    R.drawable.ic_floating_cal_off_white
                }else{
                    R.drawable.ic_floating_cal_white
                }
                adapter.update("Floating\nCalculator", fcImage)
            }else{
                fcm.requestPermission(launchFcmPermission)
            }
        })

        adapter.addItem(HomeData("History", R.drawable.ic_history_white){
            launchActivities.launch(Intent(this, HistoryActivity::class.java))
        })

        adapter.addItem(HomeData("Customize\nKeyboard", R.drawable.ic_baseline_text_format_24){
            launchActivities.launch(Intent(this, CustomizeKeyboardActivity::class.java))
        })

        adapter.addItem(HomeData("Share", R.drawable.ic_share_white){
            shareApp()
        })

        adapter.addItem(HomeData("Rate App", R.drawable.ic_star_border){
            rateUs()
        })

        adapter.addItem(HomeData("Settings", R.drawable.round_settings){
            launchActivities.launch(Intent(this, CustomizeKeyboardActivity::class.java))
        })


        registerForInputChange()
    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_INPUT_METHOD_CHANGED) {

                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

                // Get a list of all available input methods
                val inputMethodList = inputMethodManager.inputMethodList
                val inputMethodPackageNames = inputMethodList.map { it.packageName }
                println(inputMethodPackageNames)
                // Check if the current input method is your keyboard
                if (inputMethodPackageNames.contains(packageName)) {

                    inputMethodManager.showSoftInput(binding.editText, InputMethodManager.SHOW_IMPLICIT)
                }
            }
        }
    }

    private fun registerForInputChange(){
        val filter = IntentFilter(Intent.ACTION_INPUT_METHOD_CHANGED)
        registerReceiver(receiver, filter)

    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun rateUs() {
        val manager = ReviewManagerFactory.create(this)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task: Task<ReviewInfo?> ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                val flow =
                    manager.launchReviewFlow(this, reviewInfo)
                flow.addOnCompleteListener { task1: Task<Void?>? ->
                    Toast.makeText(
                        this,
                        "Thank you for your rating!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        Firebase.analytics.logEvent("rate_us", Bundle().apply {
            putBoolean("rate", false)
        })
    }

    private fun shareApp() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            val shareMessage = "Get quick calculations on the go with this ${getString(R.string.app_name)} app! Try it now: https://play.google.com/store/apps/details?id=com.jsb.calculator"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "Choose one"))


            Firebase.analytics.logEvent("share_app", Bundle().apply {
                putBoolean("share", true)
            })
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAppUpdate() {
        val mAppUpdateManager = AppUpdateManagerFactory.create(this)

        mAppUpdateManager.appUpdateInfo
            .addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                ) {
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/,
                            this,
                            0
                        )
                    } catch (e: SendIntentException) {
                        e.printStackTrace()
                    }
                }
            }

    }

}