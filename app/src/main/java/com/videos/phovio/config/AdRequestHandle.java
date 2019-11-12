package com.videos.phovio.config;

import android.content.Context;
import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;


/**
 * Created by Vishal on 03-09-2019.
 */

public class AdRequestHandle {
    public static InterstitialAd mInterstitialAd;
    public static String Counter = "Counter";
    public static NativeAd nativeAdfb;

    public static void initAd(Context context) {
//        mInterstitialAd = new InterstitialAd(context, AppContext.getInstance().getPreference().getString(Constants.FB_Interstitial_Cetting_call_sms_wp));
        mInterstitialAd = new InterstitialAd(context,"488896505197015_488906218529377");
        requestNewInterstitial();
        loadNativeAd(context);
    }

    public static void requestNewInterstitial() {
        if (!mInterstitialAd.isAdLoaded()) {

            mInterstitialAd.loadAd();
        }
    }

    public static void loadNativeAd(Context context) {

        nativeAdfb = new NativeAd(context, "375796986412762_403454090313718");

        nativeAdfb.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Race condition, load() called again before last ad was displayed
                if (nativeAdfb == null || nativeAdfb != ad) {
                    return;
                }
                // Inflate Native Ad into Container
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });

        // Request an ad
        nativeAdfb.loadAd();
    }

    public static void showAd(final Context context) {
        if (mInterstitialAd != null && mInterstitialAd.isAdLoaded()) {
            mInterstitialAd.show();
            mInterstitialAd.setAdListener(new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    // Interstitial ad displayed callback
                    Log.e("onInterstitialDisplayed",""+ad);
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    // Interstitial dismissed callback
                    Log.e("onInterstitialDismissed",""+ad);

                    initAd(context);
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    Log.e("onErrorads",""+ad);

                    // Ad error callback
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Interstitial ad is loaded and ready to be displayed
                    // Show the ad
                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Ad clicked callback
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    Log.e("onLoggingImpression",""+ad);

                    // Ad impression logged callback
                }
            });


        }else
        {
            initAd(context);

        }
    }

//    public static void showCounterAd(final Context context) {
//        sharePreference = new Preferences(context);
//        int count = sharePreference.getInt(Counter, 0);
//        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
//            count++;
//            sharePreference.putInt(AdRequestHandle.Counter, count);
//        } else {
//            initAd(context);
//            sharePreference.putInt(AdRequestHandle.Counter, 0);
//        }
//        if (count == 5) {
//            if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
//                mInterstitialAd.show();
//                mInterstitialAd.setAdListener(new AdListener() {
//                    @Override
//                    public void onAdClosed() {
//                        super.onAdClosed();
//                        initAd(context);
//                        sharePreference.putInt(AdRequestHandle.Counter, 0);
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(int i) {
//                        super.onAdFailedToLoad(i);
//                        initAd(context);
//                    }
//                });
//            }
//        }
//    }
}
