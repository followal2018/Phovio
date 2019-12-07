package com.videos.phovio.ui.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.videos.phovio.Adapters.Transactiondapter;
import com.videos.phovio.Provider.PrefManager;
import com.videos.phovio.R;
import com.videos.phovio.api.apiClient;
import com.videos.phovio.api.apiRest;
import com.videos.phovio.model.ApiResponse;
import com.videos.phovio.model.ApiResponseSettings;
import com.videos.phovio.model.ApiResponseValidation;
import com.videos.phovio.model.Transaction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EarningActivity extends AppCompatActivity {

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;
    private static final String TAG = "PhoneAuthActivity";
    String settings = "";
    String validationmsg = "";
    private RecyclerView recycler_view_transaction_earning_activity;
    private List<Transaction> transactionList = new ArrayList<>();
    private Transactiondapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private TextView text_view_earning_date_activity;
    private TextView text_view_earning_amount_earning_activity;
    private TextView text_view_minimum_point_to_withdraw_activity;
    private TextView text_view_earning_points_earning_activity;
    private TextView text_view_earning_usd_to_points_activity;
    private RelativeLayout relative_layout_history_payout_earning_actiivty;
    private RelativeLayout relative_layout_request_payout_earning_actiivty;
    private TextView text_view_code_earning_actiivty;
    private RelativeLayout relative_layout_copy_code_earning_actiivty;
    private ImageView image_view_info;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private RelativeLayout relative_layout_load_more;
    private SwipeRefreshLayout swipe_refreshl_earning_activity;
    private Integer page = 0;
    private Integer item = 0;
    private Button button_load_more;
    private ProgressDialog register_progress;
    private FirebaseAuth mAuth;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private ImageView imgsharecode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earning);
        phonesignup();
        initView();
        initAction();
        LoadTransactions();
        getData();

        requestWithdrawal();
        requestWithdrawalsettings();

    }

    public void phonesignup() {
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                mVerificationInProgress = false;
                Toast.makeText(EarningActivity.this, "STATE_VERIFY_SUCCESS", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                mVerificationInProgress = false;
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
//                mPhoneNumberField.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
                Toast.makeText(EarningActivity.this, "STATE_VERIFY_FAILED", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
                Toast.makeText(EarningActivity.this, "STATE_CODE_SENT", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getInstance().getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(EarningActivity.this, "STATE_SIGNIN_SUCCESS", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(EarningActivity.this, RequestActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(EarningActivity.this, "Invalid code", Toast.LENGTH_SHORT).show();

                            }
                            Toast.makeText(EarningActivity.this, "STATE_SIGNIN_FAILED", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void showDialog() {
        final Dialog dialog = new Dialog(this,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_phoneverification);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        wlp.gravity = Gravity.BOTTOM;

        window.setAttributes(wlp);
        TextView text_view_verification_ok = (TextView) dialog.findViewById(R.id.text_view_verification_ok);
        final EditText edtcountrycode = (EditText) dialog.findViewById(R.id.edtcountrycode);
        final EditText edtnumber = (EditText) dialog.findViewById(R.id.edtnumber);
        final EditText edtcode = (EditText) dialog.findViewById(R.id.edtcode);
        final TextView edtresend = (TextView) dialog.findViewById(R.id.edtresend);

        edtresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = "+" + edtcountrycode.getText().toString() + edtnumber.getText().toString();

                resendVerificationCode(number, mResendToken);
            }
        });
        text_view_verification_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.dismiss();
                if (edtcode.getVisibility() == View.VISIBLE) {
                    verifyPhoneNumberWithCode(mVerificationId, edtcode.getText().toString());
                } else {
                    edtcode.setVisibility(View.VISIBLE);
                    edtresend.setVisibility(View.VISIBLE);

                    String number = "+" + edtcountrycode.getText().toString() + edtnumber.getText().toString();
//                verifyPhoneNumberWithCode(mVerificationId,number);
                    startPhoneNumberVerification(number);
//                bp.subscribe(MainActivity.this, Global.SUBSCRIPTION_ID);
                }

            }
        });
//        dialog.setOnKeyListener(new Dialog.OnKeyListener() {
//
//            @Override
//            public boolean onKey(DialogInterface arg0, int keyCode,
//                                 KeyEvent event) {
//                // TODO Auto-generated method stub
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//
//                    dialog.dismiss();
//                }
//                return true;
//            }
//        });
        dialog.show();


    }

    private void initAction() {
        this.imgsharecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AdRequestHandle.showAd(EarningActivity.this);
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.share_image);
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Share.png";
                OutputStream out = null;
                File file = new File(path);
                try {
                    out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                path = file.getPath();
                Uri bmpUri = Uri.parse("file://" + path);
                PrefManager prefManager = new PrefManager(getApplicationContext());


//                String shareBody = "Download Phovio app and use my refer code : " + text_view_code_earning_actiivty.getText().toString().trim();
                String shareBody = "Download Phovio app from this link " + prefManager.getString("invitationLink");
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My Refer");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share With"));
            }
        });
        this.relative_layout_history_payout_earning_actiivty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EarningActivity.this, PayoutsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        this.relative_layout_request_payout_earning_actiivty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final PrefManager prefManager = new PrefManager(EarningActivity.this);
                String mobile = prefManager.getString("MOBILE");
                if (mobile == null || mobile.isEmpty()) {
                    Toasty.error(EarningActivity.this, "Please Register Your Mobile Number.").show();
                    int id = Integer.parseInt(prefManager.getString("ID_USER"));
                    String name = prefManager.getString("NAME_USER");
                    String image = prefManager.getString("IMAGE_USER");
                    Intent intent = new Intent(EarningActivity.this, EditActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("name", name);
                    intent.putExtra("image", image);
                    startActivity(intent);
                } else if (validationmsg.trim().isEmpty()) {
                    Intent intent = new Intent(EarningActivity.this, RequestActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                } else {
                    final Dialog dialog = new Dialog(EarningActivity.this,
                            R.style.Theme_Dialog);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.dialog_validation);
                    Window window = dialog.getWindow();
                    WindowManager.LayoutParams wlp = window.getAttributes();
                    getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    wlp.gravity = Gravity.BOTTOM;
                    wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                    window.setAttributes(wlp);
                    final TextView textview_info = (TextView) dialog.findViewById(R.id.textview_info);
                    TextView text_view_reward_ok = (TextView) dialog.findViewById(R.id.text_view_reward_ok);

//                textview_info.setText("Minimum withdrawal 1 usd or equivalent coins\n" +
//                        "Minimum 50 superlikes done\n" +
//                        "Minimum 3 refers\n" +
//                        "Minimum 20 video views\n" +
//                        "Minimum 1 video uploads\n" +
//                        "Minimum 1 image uploads");

                    textview_info.setText(validationmsg);

                    text_view_reward_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                        @Override
                        public boolean onKey(DialogInterface arg0, int keyCode,
                                             KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                dialog.dismiss();
                            }
                            return true;
                        }
                    });
                    dialog.show();
                }
            }
        });
        this.relative_layout_copy_code_earning_actiivty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("code", text_view_code_earning_actiivty.getText().toString().trim());
                clipboard.setPrimaryClip(clip);
                Toasty.success(getApplicationContext(), getResources().getString(R.string.reference_code_copied)).show();
            }
        });

        this.image_view_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(EarningActivity.this,
                        R.style.Theme_Dialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_info);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                wlp.gravity = Gravity.BOTTOM;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(wlp);
                final TextView textview_info = (TextView) dialog.findViewById(R.id.textview_info);
                TextView text_view_reward_ok = (TextView) dialog.findViewById(R.id.text_view_reward_ok);

//                textview_info.setText("Minimum withdrawal 1 usd or equivalent coins\n" +
//                        "Minimum 50 superlikes done\n" +
//                        "Minimum 3 refers\n" +
//                        "Minimum 20 video views\n" +
//                        "Minimum 1 video uploads\n" +
//                        "Minimum 1 image uploads");

                textview_info.setText(settings);

                text_view_reward_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            dialog.dismiss();
                        }
                        return true;
                    }
                });
                dialog.show();
            }
        });
        swipe_refreshl_earning_activity.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                transactionList.clear();
                page = 0;
                item = 0;
                loading = true;
                LoadTransactions();
                getData();
            }
        });
    }

    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.my_earning));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.imgsharecode = (ImageView) findViewById(R.id.imgsharecode);
        this.swipe_refreshl_earning_activity = (SwipeRefreshLayout) findViewById(R.id.swipe_refreshl_earning_activity);
        this.relative_layout_load_more = (RelativeLayout) findViewById(R.id.relative_layout_load_more);
        this.recycler_view_transaction_earning_activity = (RecyclerView) findViewById(R.id.recycler_view_transaction_earning_activity);

        this.text_view_code_earning_actiivty = (TextView) findViewById(R.id.text_view_code_earning_actiivty);
        this.text_view_earning_date_activity = (TextView) findViewById(R.id.text_view_earning_date_activity);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        this.text_view_earning_date_activity.setText(dateFormat.format(date));
        this.text_view_earning_amount_earning_activity = (TextView) findViewById(R.id.text_view_earning_amount_earning_activity);
        this.text_view_minimum_point_to_withdraw_activity = (TextView) findViewById(R.id.text_view_minimum_point_to_withdraw_activity);
        this.text_view_earning_points_earning_activity = (TextView) findViewById(R.id.text_view_earning_points_earning_activity);
        this.text_view_earning_usd_to_points_activity = (TextView) findViewById(R.id.text_view_earning_usd_to_points_activity);

        this.relative_layout_history_payout_earning_actiivty = (RelativeLayout) findViewById(R.id.relative_layout_history_payout_earning_actiivty);
        this.relative_layout_request_payout_earning_actiivty = (RelativeLayout) findViewById(R.id.relative_layout_request_payout_earning_actiivty);
        this.relative_layout_copy_code_earning_actiivty = (RelativeLayout) findViewById(R.id.relative_layout_copy_code_earning_actiivty);
        this.button_load_more = (Button) findViewById(R.id.button_load_more);
        this.image_view_info = (ImageView) findViewById(R.id.image_view_info);


        this.adapter = new Transactiondapter(transactionList, getApplicationContext());
        this.linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view_transaction_earning_activity.setHasFixedSize(true);
        recycler_view_transaction_earning_activity.setAdapter(adapter);
        recycler_view_transaction_earning_activity.setLayoutManager(linearLayoutManager);


    }

    public void LoadTransactions() {
        swipe_refreshl_earning_activity.setRefreshing(true);
        final PrefManager prefManager = new PrefManager(this);
        Integer id_user = 0;
        String key_user = "";
        if (prefManager.getString("LOGGED").toString().equals("TRUE")) {
            id_user = Integer.parseInt(prefManager.getString("ID_USER"));
            key_user = prefManager.getString("TOKEN_USER");
        }
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Transaction>> call = service.userTransaction(id_user, key_user);
        call.enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                swipe_refreshl_earning_activity.setRefreshing(false);
                apiClient.FormatData(EarningActivity.this, response);

                if (response.isSuccessful()) {
                    if (response.body().size() != 0) {
                        transactionList.clear();
                        for (int i = 0; i < response.body().size(); i++) {
                            transactionList.add(response.body().get(i));
                        }
                        adapter.notifyDataSetChanged();
                        recycler_view_transaction_earning_activity.setNestedScrollingEnabled(false);
                        page++;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
            }
        });
    }

    private void getData() {
        PrefManager prefManager = new PrefManager(getApplicationContext());
        Integer follower = -1;
        Integer id_user = 0;
        String key_user = "";
        if (prefManager.getString("LOGGED").toString().equals("TRUE")) {
            id_user = Integer.parseInt(prefManager.getString("ID_USER"));
            key_user = prefManager.getString("TOKEN_USER");
        }
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<ApiResponse> call = service.userEarning(id_user, key_user);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                apiClient.FormatData(EarningActivity.this, response);
                if (response.isSuccessful()) {

                    for (int i = 0; i < response.body().getValues().size(); i++) {
                        if (response.body().getValues().get(i).getName().equals("earning")) {
                            text_view_earning_amount_earning_activity.setText(String.format("%.6f$", new Double(response.body().getValues().get(i).getValue().substring(0, response.body().getValues().get(i).getValue().length() - 1))));
                        }
                        if (response.body().getValues().get(i).getName().equals("points")) {
                            text_view_earning_points_earning_activity.setText("You have " + response.body().getValues().get(i).getValue() + " Points");
                        }
                        if (response.body().getValues().get(i).getName().equals("equals")) {
                            text_view_earning_usd_to_points_activity.setText(response.body().getValues().get(i).getValue() + " Points");
                        }
                        if (response.body().getValues().get(i).getName().equals("code")) {
                            text_view_code_earning_actiivty.setText(response.body().getValues().get(i).getValue());
                        }

                    }

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    private void requestWithdrawal() {


        register_progress = new ProgressDialog(EarningActivity.this);
        register_progress.setCancelable(true);
        register_progress.setMessage(getResources().getString(R.string.operation_progress));
        register_progress.show();
        PrefManager prefManager = new PrefManager(getApplicationContext());
        Integer id_user = 0;
        String key_user = "";
        if (prefManager.getString("LOGGED").toString().equals("TRUE")) {
            id_user = Integer.parseInt(prefManager.getString("ID_USER"));
            key_user = prefManager.getString("TOKEN_USER");
        }
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<ApiResponseValidation> call = service.requestWithdrawalvalidation(id_user);
        call.enqueue(new Callback<ApiResponseValidation>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ApiResponseValidation> call, Response<ApiResponseValidation> response) {

//                Log.e("responsebody",""+response.body().toString());

                apiClient.FormatData(EarningActivity.this, response);
                if (response.isSuccessful()) {
                    validationmsg = response.body().getMessage().replaceAll(",", "\n");
                }
                register_progress.dismiss();
            }

            @Override
            public void onFailure(Call<ApiResponseValidation> call, Throwable t) {
                register_progress.dismiss();
            }
        });
    }

    private void requestWithdrawalsettings() {


//        register_progress = new ProgressDialog(EarningActivity.this);
//        register_progress.setCancelable(true);
//        register_progress.setMessage(getResources().getString(R.string.operation_progress));
//        register_progress.show();
        PrefManager prefManager = new PrefManager(getApplicationContext());
        Integer id_user = 0;
        String key_user = "";
        if (prefManager.getString("LOGGED").toString().equals("TRUE")) {
            id_user = Integer.parseInt(prefManager.getString("ID_USER"));
            key_user = prefManager.getString("TOKEN_USER");
        }
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<ApiResponseSettings> call = service.requestWithdrawalsettings(id_user, key_user);
        call.enqueue(new Callback<ApiResponseSettings>() {
            @Override
            public void onResponse(Call<ApiResponseSettings> call, Response<ApiResponseSettings> response) {
//                apiClient.FormatData(EarningActivity.this, response);
                Log.e("Response", "" + response.message());
                if (response.isSuccessful()) {
                    if (response.body().getCode().equals(200)) {
                        Log.e("response", "" + response.message());
                        settings = response.body().getValues().replaceAll(",", "\n");
                        Matcher m = Pattern.compile("withdrawal\\s(.*)\\spoints").matcher(settings);
                        while (m.find()) {
                            text_view_minimum_point_to_withdraw_activity.setText(String.format("Min Withdrawal = %s Points", m.group(1)));
                            System.out.println(m.group(1));
                        }

//                        settings=response.body().getValues();
//                        relative_layout_request_payout_earning_actiivty.setClickable(true);
                    } else {
//                        relative_layout_request_payout_earning_actiivty.setClickable(false);
                    }
                }
//                register_progress.dismiss();
            }

            @Override
            public void onFailure(Call<ApiResponseSettings> call, Throwable t) {
                String error = String.valueOf(t);
//                register_progress.dismiss();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        LoadTransactions();
    }
}
