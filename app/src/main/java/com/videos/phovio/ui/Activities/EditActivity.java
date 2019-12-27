package com.videos.phovio.ui.Activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.videos.phovio.Provider.PrefManager;
import com.videos.phovio.R;
import com.videos.phovio.api.ProgressRequestBody;
import com.videos.phovio.api.apiClient;
import com.videos.phovio.api.apiRest;
import com.videos.phovio.model.ApiResponse;
import com.videos.phovio.utils.ImageCompress;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

public class EditActivity extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks {

    private static final int CAMERA_REQUEST_IMAGE_1 = 3001;
    Dialog verificationDialog;
    private EditText edit_input_email;
    private EditText edit_input_name;
    private EditText edit_input_facebook;
    private EditText edit_input_twitter;
    private EditText edit_input_instragram;
    private EditText edit_input_mobile_number;
    private EditText edit_input_otp;
    private TextInputLayout edit_input_layout_instragram;
    private TextInputLayout edit_input_layout_twitter;
    private TextInputLayout edit_input_layout_facebook;
    private TextInputLayout edit_input_layout_name;
    private TextInputLayout edit_input_layout_email;
    private TextInputLayout edit_input_layout_mobile_number;
    private Button edit_button;
    private Button add_mobile_number_button;
    private ProgressDialog register_progress;
    private int id;
    private String name;
    private String image;
    private String facebook;
    private String email;
    private String instagram;
    private String twitter;
    private String mobile;
    private ImageView image_view_user_profile;
    private TextView text_view_name_user;
    private ProgressDialog register_progress_load;
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private int PICK_IMAGE = 1002;
    private String videoUrl;
    private ProgressDialog pd;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                edit_input_otp.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(EditActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
            mResendToken = forceResendingToken;

        }
    };

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        this.id = bundle.getInt("id");
        this.name = bundle.getString("name");
        this.image = bundle.getString("image");
        setContentView(R.layout.activity_edit);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //  SelectWallpaper();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.edit_my_profile));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        initView();

        setUser();
        initAction();
        createVerificationDialog();

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

    public void initView() {
        this.text_view_name_user = (TextView) findViewById(R.id.text_view_name_user);
        this.edit_input_email = (EditText) findViewById(R.id.edit_input_email);
        this.edit_input_name = (EditText) findViewById(R.id.edit_input_name);
        this.edit_input_facebook = (EditText) findViewById(R.id.edit_input_facebook);
        this.edit_input_twitter = (EditText) findViewById(R.id.edit_input_twitter);
        this.edit_input_instragram = (EditText) findViewById(R.id.edit_input_instragram);
        this.edit_input_mobile_number = (EditText) findViewById(R.id.edit_input_mobile_number);
        this.edit_input_layout_email = (TextInputLayout) findViewById(R.id.edit_input_layout_email);
        this.edit_input_layout_name = (TextInputLayout) findViewById(R.id.edit_input_layout_name);
        this.edit_input_layout_facebook = (TextInputLayout) findViewById(R.id.edit_input_layout_facebook);
        this.edit_input_layout_twitter = (TextInputLayout) findViewById(R.id.edit_input_layout_twitter);
        this.edit_input_layout_instragram = (TextInputLayout) findViewById(R.id.edit_input_layout_instragram);
        this.edit_input_layout_mobile_number = (TextInputLayout) findViewById(R.id.edit_input_layout_mobile_number);
        pd = new ProgressDialog(EditActivity.this);
        pd.setMessage("Uploading Image");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCancelable(false);
        this.image_view_user_profile = (ImageView) findViewById(R.id.image_view_user_profile);
        this.edit_button = (Button) findViewById(R.id.edit_button);
        this.add_mobile_number_button = (Button) findViewById(R.id.add_mobile_number_button);


        image_view_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && null != data) {


            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            videoUrl = picturePath;
            videoUrl = new ImageCompress(EditActivity.this).compressImage(videoUrl);

            File file = new File(videoUrl);
            Log.e("EditActivity:->", "File Size :-> " + file.length());
            Log.v("SIZE", file.getName() + "");
//            text_upload_title.setText(file.getName().replace(".mp4", "").replace(".MP4", ""));


            upload(CAMERA_REQUEST_IMAGE_1);

        } else {

            Log.i("SonaSys", "resultCode: " + resultCode);
            switch (resultCode) {
                case 0:
                    Log.i("SonaSys", "User cancelled");
                    break;
                case -1:
                    break;
            }
        }
    }

    public void upload(final int CODE) {
        File file1 = new File(videoUrl);
        int file_size = Integer.parseInt(String.valueOf(file1.length() / 1024 / 1024));
        if (file_size > 20) {
            Toasty.error(getApplicationContext(), "Max file size allowed 20M", Toast.LENGTH_LONG).show();
        }
        Log.v("SIZE", file1.getName() + "");


//        pd.show();
        PrefManager prf = new PrefManager(getApplicationContext());

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);

        //File creating from selected URL
        final File file = new File(videoUrl);


        ProgressRequestBody requestFile = new ProgressRequestBody(file, EditActivity.this);

        MultipartBody.Part body = MultipartBody.Part.createFormData("profile_picture", file.getName(), requestFile);
        String id_ser = prf.getString("ID_USER");
        String key_ser = prf.getString("TOKEN_USER");

//        Call<ApiResponse> request = service.uploadImage(body, id_ser, key_ser, edit_text_upload_title.getText().toString().trim(), edit_text_upload_description.getText().toString().trim(), getSelectedLanguages(), getSelectedCategories());
        Call<ApiResponse> request = service.editUser(body, Integer.parseInt(id_ser), key_ser, edit_input_name.getText().toString(), edit_input_email.getText().toString(), edit_input_facebook.getText().toString(), edit_input_twitter.getText().toString(), edit_input_instragram.getText().toString(), edit_input_mobile_number.getText().toString());

        request.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (response.isSuccessful()) {
//                    Toasty.success(getApplication(), getResources().getString(R.string.image_upload_success), Toast.LENGTH_LONG).show();
//                    finish();
                } else {
//                    Toasty.error(getApplication(), getResources().getString(R.string.no_connexion), Toast.LENGTH_LONG).show();

                }
                // file.delete();
                // getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
//                pd.dismiss();
//                pd.cancel();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toasty.error(getApplication(), getResources().getString(R.string.no_connexion), Toast.LENGTH_LONG).show();
//                pd.dismiss();
//                pd.cancel();
            }
        });
    }

    private void SelectImage() {
        if (ContextCompat.checkSelfPermission(EditActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            i.setType("image/*");
            startActivityForResult(i, PICK_IMAGE);
        }
    }

    public void initAction() {
        this.edit_input_email.addTextChangedListener(new SupportTextWatcher(this.edit_input_email));
        this.edit_input_name.addTextChangedListener(new SupportTextWatcher(this.edit_input_name));
        this.edit_input_facebook.addTextChangedListener(new SupportTextWatcher(this.edit_input_facebook));
        this.edit_input_twitter.addTextChangedListener(new SupportTextWatcher(this.edit_input_twitter));
        this.edit_input_instragram.addTextChangedListener(new SupportTextWatcher(this.edit_input_instragram));
        this.edit_input_mobile_number.addTextChangedListener(new SupportTextWatcher(this.edit_input_layout_mobile_number));
        this.edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        this.add_mobile_number_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMobileNumber();
            }
        });
    }

    public void submit() {

        if (!validatName()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }
        if (!validateFacebook()) {
            return;
        }
        if (!validateTwitter()) {
            return;
        }
        if (!validateInstagram()) {
            return;
        }
        if (!validateMobileNumber()) {
            return;
        }
        if (!isMobileAdded()) {
            return;
        }

        editUser();

    }

    private void addMobileNumber() {
        if (validateMobileNumber()) {
            sendVerificationCode(edit_input_mobile_number.getText().toString().trim());
            showVerificationDialog();
        }
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private void verifyVerificationCode(String code) {
        verificationDialog.dismiss();
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(EditActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            edit_input_layout_mobile_number.setEnabled(false);
                            add_mobile_number_button.setVisibility(View.GONE);
                            Toasty.info(EditActivity.this, "Mobile Verified Successfully").show();
                            FirebaseAuth.getInstance().signOut();
                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }

    private void editUser() {
        final PrefManager prf = new PrefManager(getApplicationContext());
        if (prf.getString("LOGGED").toString().equals("TRUE")) {
            String user = prf.getString("ID_USER");
            String key = prf.getString("TOKEN_USER");

            register_progress = ProgressDialog.show(this, null, getString(R.string.progress_login));
            Retrofit retrofit = apiClient.getClient();
            apiRest service = retrofit.create(apiRest.class);
            MultipartBody.Part body = MultipartBody.Part.createFormData("profile_picture", "", new RequestBody() {
                @Override
                public MediaType contentType() {
                    return null;
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {

                }
            });

            Call<ApiResponse> call = service.editUser(body, Integer.parseInt(user), key, edit_input_name.getText().toString(), edit_input_email.getText().toString(), edit_input_facebook.getText().toString(), edit_input_twitter.getText().toString(), edit_input_instragram.getText().toString(), edit_input_mobile_number.getText().toString());
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        prf.setString("NAME_USER", edit_input_name.getText().toString());
                        prf.setString("MOBILE", edit_input_mobile_number.getText().toString());
                        Toasty.success(getApplicationContext(), getResources().getString(R.string.message_sended), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toasty.error(getApplicationContext(), getString(R.string.no_connexion), Toast.LENGTH_SHORT).show();
                    }
                    register_progress.dismiss();
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    register_progress.dismiss();
                    Toasty.error(getApplicationContext(), getString(R.string.no_connexion), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validatName() {
        if (edit_input_name.getText().toString().trim().isEmpty() || edit_input_name.getText().length() < 3) {
            edit_input_layout_name.setError(getString(R.string.error_short_value));
            requestFocus(edit_input_name);
            return false;
        } else {
            edit_input_layout_name.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        if (edit_input_email.getText().toString().trim().length() == 0) {
            return true;
        }
        String email = edit_input_email.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            edit_input_layout_email.setError(getString(R.string.error_mail_valide));
            requestFocus(edit_input_email);
            return false;
        } else {
            edit_input_layout_email.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateMobileNumber() {
        String mobile = edit_input_mobile_number.getText().toString().trim();
        if (mobile.isEmpty() || !isValidMobile(mobile)) {
            edit_input_layout_mobile_number.setError(getString(R.string.error_mobile_valide));
            requestFocus(edit_input_mobile_number);
            return false;
        } else {
            edit_input_layout_mobile_number.setErrorEnabled(false);
        }

        return true;
    }

    private boolean isMobileAdded() {
        if (add_mobile_number_button.getVisibility() == View.VISIBLE) {
            edit_input_layout_mobile_number.setError(getString(R.string.error_add_mobile_valide));
            requestFocus(edit_input_mobile_number);
            return false;
        } else {
            edit_input_layout_mobile_number.setErrorEnabled(false);
        }

        return true;
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateInstagram() {
        if (edit_input_instragram.getText().toString().trim().length() == 0) {
            return true;
        }
        if (!URLUtil.isValidUrl(edit_input_instragram.getText().toString())) {
            edit_input_layout_instragram.setError(getString(R.string.invalide_url));
            requestFocus(edit_input_instragram);

            return false;
        } else {
            edit_input_layout_instragram.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateTwitter() {
        if (edit_input_twitter.getText().toString().trim().length() == 0) {
            return true;
        }
        if (!URLUtil.isValidUrl(edit_input_twitter.getText().toString())) {
            edit_input_layout_twitter.setError(getString(R.string.invalide_url));
            requestFocus(edit_input_twitter);

            return false;
        } else {
            edit_input_layout_twitter.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateFacebook() {
        if (edit_input_facebook.getText().toString().trim().length() == 0) {
            return true;
        }
        if (!URLUtil.isValidUrl(edit_input_facebook.getText().toString())) {
            edit_input_layout_facebook.setError(getString(R.string.invalide_url));
            requestFocus(edit_input_facebook);

            return false;
        } else {
            edit_input_layout_facebook.setErrorEnabled(false);
            return true;
        }
    }

    private void getUser() {
        register_progress_load = ProgressDialog.show(this, null, getString(R.string.loading_user_data));
        edit_input_name.setText(name);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<ApiResponse> call = service.getUser(id, id);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {

                    for (int i = 0; i < response.body().getValues().size(); i++) {
                        Timber.e("EditProfile :-> Value Name -> " + response.body().getValues().get(i).getName());
                        Timber.e("EditProfile :-> Value Value -> " + response.body().getValues().get(i).getValue());

                        if (response.body().getValues().get(i).getName().equals("facebook")) {
                            facebook = response.body().getValues().get(i).getValue();
                            if (facebook != null) {
                                if (!facebook.isEmpty()) {
//                                    if (facebook.startsWith("http://") || facebook.startsWith("https://")) {
                                    edit_input_facebook.setText(facebook.replace("\"", ""));
//                                    }
                                }
                            }
                        }
                        if (response.body().getValues().get(i).getName().equals("twitter")) {
                            twitter = response.body().getValues().get(i).getValue();
                            if (twitter != null) {

                                if (!twitter.isEmpty()) {
//                                    if (twitter.startsWith("http://") || twitter.startsWith("https://")) {
                                    edit_input_twitter.setText(twitter.replace("\"", ""));

//                                    }
                                }
                            }
                        }
                        if (response.body().getValues().get(i).getName().equals("instagram")) {

                            instagram = response.body().getValues().get(i).getValue();
                            if (instagram != null) {

                                if (!instagram.isEmpty()) {
//                                    if (instagram.startsWith("http://") || instagram.startsWith("https://")) {

                                    edit_input_instragram.setText(instagram.replace("\"", ""));
//                                    }
                                }
                            }
                        }
                        if (response.body().getValues().get(i).getName().equals("email")) {
                            email = response.body().getValues().get(i).getValue();
                            if (email != null) {
                                if (!email.isEmpty()) {
                                    edit_input_email.setText(email.replace("\"", ""));
                                    edit_input_email.setEnabled(false);

                                }
                            }
                        }
                        if (response.body().getValues().get(i).getName().equals("mobile")) {
                            mobile = response.body().getValues().get(i).getValue();
                            if (mobile != null) {
                                if (!mobile.isEmpty()) {
                                    edit_input_mobile_number.setText(mobile.replace("\"", ""));
                                    add_mobile_number_button.setVisibility(View.GONE);
                                }
                            }
                        }
                        if (response.body().getValues().get(i).getName().equals("profile_picture")) {
                            Picasso.with(EditActivity.this)
                                    .load(response.body().getValues().get(i).getValue())
                                    .error(R.drawable.profile)
                                    .placeholder(R.drawable.profile)
                                    .centerCrop()
                                    .resize(100, 80)
                                    .into(image_view_user_profile);
                        }

                    }

                }
                register_progress_load.hide();
                register_progress_load.dismiss();

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

                register_progress_load.hide();
                register_progress_load.dismiss();


            }
        });
    }

    public void setUser() {
        this.text_view_name_user.setText(name);
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                image_view_user_profile.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                image_view_user_profile.setImageResource(R.mipmap.ic_launcher);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(this)
                .load(image)
                .error(R.drawable.profile)
                .placeholder(R.drawable.profile)
                .centerCrop()
                .resize(100, 80)
                .into(target);
        getUser();
    }

    public void createVerificationDialog() {
        verificationDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        verificationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        verificationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        verificationDialog.setContentView(R.layout.dialog_verify_otp);
        verificationDialog.setCancelable(false);
        edit_input_otp = verificationDialog.findViewById(R.id.edtOtp);
        TextView txtPositive = verificationDialog.findViewById(R.id.txtPositive);
        TextView txtNegative = verificationDialog.findViewById(R.id.txtNegative);
        TextView tvresend = verificationDialog.findViewById(R.id.tvresend);
        txtPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edit_input_otp.getText().toString().trim().isEmpty()) {
                    verifyVerificationCode(edit_input_otp.getText().toString().trim());
                }
            }
        });

        txtNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificationDialog.dismiss();
            }
        });
        tvresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendVerificationCode(edit_input_otp.getText().toString().trim(), mResendToken);
                Toast.makeText(EditActivity.this, "OTP Resent Successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showVerificationDialog() {
        verificationDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressUpdate(int percentage) {
        pd.setProgress(percentage);
    }

    @Override
    public void onError() {
        pd.dismiss();
        pd.cancel();
    }

    @Override
    public void onFinish() {
        pd.dismiss();
        pd.cancel();

    }

    private class SupportTextWatcher implements TextWatcher {
        private View view;

        private SupportTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edit_input_email:
                    validateEmail();
                    break;
                case R.id.edit_input_name:
                    validatName();
                    break;
                case R.id.edit_input_facebook:
                    validateFacebook();
                    break;
                case R.id.edit_input_twitter:
                    validateTwitter();
                    break;
                case R.id.edit_input_instragram:
                    validateInstagram();
                    break;
                case R.id.edit_input_mobile_number:
                    validateMobileNumber();
                    break;

            }
        }
    }
}
