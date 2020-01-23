package com.videos.phovio.Provider;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.videos.phovio.BuildConfig;
import com.videos.phovio.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

import static com.videos.phovio.config.Global.TESTING_REWARDED_AD_ID;

/**
 * Created by Nirav Mandani on 07-12-2019.
 * Followal Solutions
 */
public class RewardedAdKeyStorage {

    private final String MY_REWARDED_AD_KEY = "MY_REWARDED_AD_KEY";
    private SharedPreferences preferences;
    private Context context;
    private Random randomGenerator;

    public RewardedAdKeyStorage(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(MY_REWARDED_AD_KEY, Context.MODE_PRIVATE);
        randomGenerator = new Random();
    }

    public void storeRewardedKeys(ArrayList<String> rewardedAdKey) {
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(rewardedAdKey);
        editor.putString("REWARDED_AD_KEY_LIST", json);
        editor.apply();
    }

    public String getRewardedAdKey() {
        Gson gson = new Gson();
        String json = preferences.getString("REWARDED_AD_KEY_LIST", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> rewardedAdKeys = gson.fromJson(json, type);
        if (rewardedAdKeys != null && !rewardedAdKeys.isEmpty()) {
            int index = randomGenerator.nextInt(rewardedAdKeys.size());
            return rewardedAdKeys.get(index);
        } else {
            return context.getString(R.string.ad_unit_id_reward);
        }
    }
}
