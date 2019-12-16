package com.videos.phovio.ui.Activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.vending.billing.IInAppBillingService;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.facebook.ads.AdSettings;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.videos.phovio.Provider.PrefManager;
import com.videos.phovio.Provider.RewardedAdKeyStorage;
import com.videos.phovio.R;
import com.videos.phovio.api.apiClient;
import com.videos.phovio.api.apiRest;
import com.videos.phovio.config.Global;
import com.videos.phovio.model.ApiResponse;
import com.videos.phovio.model.Language;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.videos.phovio.config.Global.PrefKeys.PREF_STATUS_ID;
import static com.videos.phovio.config.Global.PrefKeys.PREF_STATUS_KIND;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final String LOG_TAG = "iabv3";
    // put your Google merchant id here (as stated in public profile of your Payments Merchant Center)
    // if filled library will provide protection against Freedom alike Play Market simulators
    private static final String MERCHANT_ID = null;
    IInAppBillingService mService;
    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);

        }
    };
    private ProgressBar intro_progress;
    private PrefManager prf;
    private BillingProcessor bp;
    private boolean readyToPurchase = false;

    public static void adapteActivity(Activity activity) {
        activity.finish();
    }

    public void checkuserreferal() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Log.e("pendingDynamicLinkData", "" + pendingDynamicLinkData);
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            String id = deepLink.toString().substring(deepLink.toString().indexOf("=") + 1, deepLink.toString().indexOf("&"));
                            String kind = deepLink.toString().substring(deepLink.toString().lastIndexOf("=") + 1);
                            Log.e("deepLink", "" + deepLink);
                            Log.e("deepLink", "Id : " + id);
                            Log.e("deepLink", "Kind : " + kind);
                            prf.setString(PREF_STATUS_ID, id);
                            prf.setString(PREF_STATUS_KIND, kind);
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prf = new PrefManager(getApplicationContext());
        checkuserreferal();
        AdSettings.addTestDevice("136835a8-3194-462b-9b4b-dfc1c31ef8e3");
        initBuy();
        loadLang();
        getRewardedAdKeys();
        setContentView(R.layout.activity_splash);


        intro_progress = (ProgressBar) findViewById(R.id.intro_progress);
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // If you want to modify a view in your Activity
                SplashActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        checkAccount();

                    }
                });
            }
        }, 3000);


        ImageView imageView = (ImageView) findViewById(R.id.logo_Secur);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash);
        // imageView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private void checkAccount() {

        Integer version = -1;
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (version != -1) {
            Retrofit retrofit = apiClient.getClient();
            apiRest service = retrofit.create(apiRest.class);
            Call<ApiResponse> call = service.check(version);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    updateTextViews();
                    intro_progress.setVisibility(View.GONE);

                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals(200)) {
                            if (!prf.getString("first").equals("true")) {
                                Intent intent = new Intent(SplashActivity.this, SlideActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                finish();
                                prf.setString("first", "true");
                            } else {
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                finish();
                            }
                        } else if (response.body().getCode().equals(202)) {
                            String title_update = response.body().getValues().get(0).getValue();
                            String featurs_update = response.body().getMessage();
                            View v = (View) getLayoutInflater().inflate(R.layout.update_message, null);
                            TextView update_text_view_title = (TextView) v.findViewById(R.id.update_text_view_title);
                            TextView update_text_view_updates = (TextView) v.findViewById(R.id.update_text_view_updates);
                            update_text_view_title.setText(title_update);
                            update_text_view_updates.setText(featurs_update);
                            AlertDialog.Builder builder;
                            builder = new AlertDialog.Builder(SplashActivity.this);
                            builder.setTitle("New Update")
                                    //.setMessage(response.body().getValue())
                                    .setView(v)
                                    .setPositiveButton(getResources().getString(R.string.update_now), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            final String appPackageName = getApplication().getPackageName();
                                            try {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                            } catch (android.content.ActivityNotFoundException anfe) {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                                            }
                                            finish();
                                        }
                                    })
                                    .setNegativeButton(getResources().getString(R.string.skip), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!prf.getString("first").equals("true")) {
                                                Intent intent = new Intent(SplashActivity.this, SlideActivity.class);
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                                finish();
                                                prf.setString("first", "true");
                                            } else {
                                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                                finish();
                                            }
                                        }
                                    })
                                    .setCancelable(false)
                                    .setIcon(R.drawable.ic_update)
                                    .show();
                        } else {
                            if (!prf.getString("first").equals("true")) {
                                Intent intent = new Intent(SplashActivity.this, SlideActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                finish();
                                prf.setString("first", "true");
                            } else {
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                finish();
                            }
                        }
                    } else {
                        if (!prf.getString("first").equals("true")) {
                            Intent intent = new Intent(SplashActivity.this, SlideActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();
                            prf.setString("first", "true");
                        } else {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {

                    if (!prf.getString("first").equals("true")) {
                        Intent intent = new Intent(SplashActivity.this, SlideActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        finish();
                        prf.setString("first", "true");
                    } else {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                        finish();
                    }

                }
            });
        } else {
            if (!prf.getString("first").equals("true")) {
                Intent intent = new Intent(SplashActivity.this, SlideActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
                prf.setString("first", "true");
            } else {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
            }
        }
    }

    private void getRewardedAdKeys() {

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        final PrefManager prefManager = new PrefManager(this);
        Integer id_user = 0;
        String key_user = "";
        if (prefManager.getString("LOGGED").toString().equals("TRUE")) {
            id_user = Integer.parseInt(prefManager.getString("ID_USER"));
            key_user = prefManager.getString("TOKEN_USER");
        }
        Call<ApiResponse> call = service.getRewardedAdKeys();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                updateTextViews();
                intro_progress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getCode().equals(200)) {
                        ArrayList<String> rewardedAdKeys = new ArrayList<>();
                        for (int i = 0; i < response.body().getValues().size(); i++) {
                            if (response.body().getValues().get(i).getName().equals("rewarded_ads_id")) {
                                rewardedAdKeys.add(response.body().getValues().get(i).getValue());
                            }
                        }
                        RewardedAdKeyStorage rewardedAdKeyStorage = new RewardedAdKeyStorage(SplashActivity.this);
                        rewardedAdKeyStorage.storeRewardedKeys(rewardedAdKeys);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

                t.printStackTrace();
            }
        });
    }

    private void initBuy() {
        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);


        if (!BillingProcessor.isIabServiceAvailable(this)) {
            //  showToast("In-app billing service is unavailable, please upgrade Android Market/Play to version >= 3.9.16");
        }

        bp = new BillingProcessor(this, Global.MERCHANT_KEY, MERCHANT_ID, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
                updateTextViews();
            }

            @Override
            public void onBillingError(int errorCode, @Nullable Throwable error) {
            }

            @Override
            public void onBillingInitialized() {
                readyToPurchase = true;
                updateTextViews();
            }

            @Override
            public void onPurchaseHistoryRestored() {
                for (String sku : bp.listOwnedProducts())
                    Log.d(LOG_TAG, "Owned Managed Product: " + sku);
                for (String sku : bp.listOwnedSubscriptions())
                    Log.d(LOG_TAG, "Owned Subscription: " + sku);
                updateTextViews();
            }
        });
        bp.loadOwnedPurchasesFromGoogle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConn);
    }

    private void updateTextViews() {
        PrefManager prf = new PrefManager(getApplicationContext());
        bp.loadOwnedPurchasesFromGoogle();
        if (isSubscribe(Global.SUBSCRIPTION_ID)) {
            prf.setString("SUBSCRIBED", "TRUE");
            // showToast("SUBSCRIBED");

        } else {
            prf.setString("SUBSCRIBED", "FALSE");
            // showToast("NOT SUBSCRIBED");
        }
    }

    private void showToast(String message) {
        //  Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public Bundle getPurchases() {
        if (!bp.isInitialized()) {
            return null;
        }
        try {

            return mService.getPurchases(Constants.GOOGLE_API_VERSION, getApplicationContext().getPackageName(), Constants.PRODUCT_TYPE_SUBSCRIPTION, null);
        } catch (Exception e) {
            // Toast.makeText(this, "ex", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
        return null;
    }

    public Boolean isSubscribe(String SUBSCRIPTION_ID_CHECK) {

        if (!bp.isSubscribed(Global.SUBSCRIPTION_ID))
            return false;


        Bundle b = getPurchases();
        if (b == null)
            return false;
        if (b.getInt("RESPONSE_CODE") == 0) {
            ArrayList<String> ownedSkus =
                    b.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
            ArrayList<String> purchaseDataList =
                    b.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
            ArrayList<String> signatureList =
                    b.getStringArrayList("INAPP_DATA_SIGNATURE_LIST");
            String continuationToken =
                    b.getString("INAPP_CONTINUATION_TOKEN");


            if (purchaseDataList == null) {
                return false;

            }
            if (purchaseDataList.size() == 0) {
                return false;
            }
            for (int i = 0; i < purchaseDataList.size(); ++i) {
                String purchaseData = purchaseDataList.get(i);
                String signature = signatureList.get(i);
                String sku_1 = ownedSkus.get(i);
                //Long tsLong = System.currentTimeMillis()/1000;

                try {
                    JSONObject rowOne = new JSONObject(purchaseData);
                    String productId = rowOne.getString("productId");

                    if (productId.equals(SUBSCRIPTION_ID_CHECK)) {

                        Boolean autoRenewing = rowOne.getBoolean("autoRenewing");
                        if (autoRenewing) {
                            Long tsLong = System.currentTimeMillis() / 1000;
                            Long purchaseTime = rowOne.getLong("purchaseTime") / 1000;
                            return true;
                        } else {
                            // Toast.makeText(this, "is not autoRenewing ", Toast.LENGTH_SHORT).show();
                            Long tsLong = System.currentTimeMillis() / 1000;
                            Long purchaseTime = rowOne.getLong("purchaseTime") / 1000;
                            if (tsLong > (purchaseTime + (Global.SUBSCRIPTION_DURATION * 86400))) {
                                //   Toast.makeText(this, "is Expired ", Toast.LENGTH_SHORT).show();
                                return false;
                            } else {
                                return true;
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        } else {
            return false;
        }

        return false;
    }

    public void loadLang() {
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Language>> call = service.languageAll();
        call.enqueue(new Callback<List<Language>>() {
            @Override
            public void onResponse(Call<List<Language>> call, final Response<List<Language>> response) {

            }

            @Override
            public void onFailure(Call<List<Language>> call, Throwable t) {
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }
}
