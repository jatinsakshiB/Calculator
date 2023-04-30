package com.jsb.calculator.manager

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.jsb.calculator.utils.AppData
import com.jsb.calculator.utils.Log
import com.jsb.calculator.utils.Logs
import com.jsb.calculator.utils.Utils

class AdsManager(val context: Context) {


    var mInterstitialAd: InterstitialAd? = null
    var appOpenAd: AppOpenAd? = null

    fun initialize(done: ((AdsManager)->Unit)){
        if (!Utils.testMode){
            MobileAds.initialize(context) {
                done.invoke(this)
            }
        }else{
            done.invoke(this)
        }
    }

    fun setUpBanner(adView: AdView) {
        if (Utils.testMode) return
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Log("onAdLoaded", Logs.ADS)
                super.onAdLoaded()
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                Log("onAdFailedToLoad: $loadAdError", Logs.ADS)
                super.onAdFailedToLoad(loadAdError)
            }

            override fun onAdClicked() {
                Log("onAdClicked", Logs.ADS)
                super.onAdClicked()
            }

            override fun onAdImpression() {
                Log("onAdImpression", Logs.ADS)
                super.onAdImpression()
            }
        }
        return
    }

    fun loadInterstitial(onLoaded: ((AdsManager)->Unit) = {}) {
        if (Utils.testMode) return
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, AppData.interstitial, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    mInterstitialAd = interstitialAd
                    Log("onAdLoaded", Logs.ADS)
                    onLoaded(this@AdsManager)
                    mInterstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            Log("The ad was dismissed.", Logs.ADS)
                            loadInterstitial()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            Log("The ad failed to show.", Logs.ADS)
                        }

                        override fun onAdShowedFullScreenContent() {
                            mInterstitialAd = null
                            Log("The ad was shown.", Logs.ADS)
                        }
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    mInterstitialAd = null
                    Log(loadAdError.message, Logs.ADS)
                }
            })
    }


    fun loadAppOpen(onLoaded: ((AdsManager)->Unit) = {}) {
        val request = AdRequest.Builder().build()
        AppOpenAd.load(
            context, AppData.appOpenAddId, request,
            object : AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    Log("Ad was loaded.", Logs.ADS)
                    appOpenAd = ad
                    onLoaded(this@AdsManager)
                    appOpenAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            Log(adError.message, Logs.ADS)
                            appOpenAd = null
                        }

                        override fun onAdShowedFullScreenContent() {
                            Log("Ad showed fullscreen content.", Logs.ADS)
                        }

                        override fun onAdDismissedFullScreenContent() {
                            Log("Ad dismissed fullscreen content.", Logs.ADS)
                            appOpenAd = null
                            loadAppOpen()
                        }

                        override fun onAdImpression() {
                            Log("onAdImpression.", Logs.ADS)
                            super.onAdImpression()
                        }
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log(loadAdError.message, Logs.ADS)
                }
            })
    }

    fun showInterstitial(activity: Activity){
        if (Utils.testMode) return
        if (mInterstitialAd != null) {
            mInterstitialAd!!.show(activity)
        }
    }

    fun showAppOpen(activity: Activity){
        if (Utils.testMode) return
        if (appOpenAd != null) {
            appOpenAd!!.show(activity)
        }
    }
}