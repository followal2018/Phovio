package com.videos.phovio.ui.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.videos.phovio.Adapters.CategorySelectAdapter;
import com.videos.phovio.Adapters.LanguageSelectAdapter;
import com.videos.phovio.Adapters.SelectableCategoryViewHolder;
import com.videos.phovio.Adapters.SelectableLanguageViewHolder;
import com.videos.phovio.Provider.PrefManager;
import com.videos.phovio.R;
import com.videos.phovio.api.ProgressRequestBody;
import com.videos.phovio.api.apiClient;
import com.videos.phovio.api.apiRest;
import com.videos.phovio.model.ApiResponse;
import com.videos.phovio.model.Category;
import com.videos.phovio.model.Language;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UploadQuoteActivity extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks,SelectableCategoryViewHolder.OnItemSelectedListener ,SelectableLanguageViewHolder.OnItemSelectedListener {

    private EditText edit_view_activity_upload_quote_status;
    private RelativeLayout relative_layout_upload_status;
    private ImageView image_view_upload_status_emoji;
    private FloatingActionButton fab_status_upload_send;
    private Integer background=1;
    private int font_index = 1;
    private int color_index = 1;
    private com.getbase.floatingactionbutton.FloatingActionButton fab_font;
    private RelativeLayout relative_layout_status_screen;
    private com.getbase.floatingactionbutton.FloatingActionButton fab_image;
    private TextView text_view_activity_upload_quote_screen;
    private com.getbase.floatingactionbutton.FloatingActionButton fab_color;
    private FloatingActionButton fab_status_emoji;
    private String color  = "C51062";
    private ProgressDialog register_progress;

    private RecyclerView recycle_view_selected_language;
    private RecyclerView recycle_view_selected_category;


    private LinearLayoutManager gridLayoutManagerCategorySelect;
    private LinearLayoutManager gridLayoutManagerLanguageSelect;

    private ArrayList<Category> categoriesListObj = new ArrayList<Category>();
    private CategorySelectAdapter categorySelectAdapter;
    private LanguageSelectAdapter languageSelectAdapter;

    private List<Language> languageList = new ArrayList<Language>();
    private FloatingActionsMenu multiple_actions_left;
    private LinearLayout linear_layout_categories;
    private LinearLayout linear_layout_langauges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        setContentView(R.layout.activity_upload_quote);


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.write_quote));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initAction();
        getCategory();
    }
    @Override
    public void onBackPressed(){
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
    public void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(UploadQuoteActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED) {


                if (ActivityCompat.shouldShowRequestPermissionRationale(UploadQuoteActivity.this,   android.Manifest.permission.READ_CONTACTS)) {
                    Intent intent_status  =  new Intent(getApplicationContext(), PermissionActivity.class);
                    startActivity(intent_status);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    finish();
                } else {
                    Intent intent_status  =  new Intent(getApplicationContext(), PermissionActivity.class);
                    startActivity(intent_status);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    finish();
                }
            }

        }
    }
    public void nextFont(){
        this.font_index++;
        if (font_index == 11){
            this.font_index=1;
        }
        String font = "font_1.ttf";
        switch (font_index){
            case 1:
                font = "font_1.ttf";
                break;
            case 2:
                font = "font_2.ttf";
                break;
            case 3:
                font = "font_3.ttf";
                break;
            case 4:
                font = "font_4.ttf";
                break;
            case 5:
                font = "font_5.ttf";
                break;
            case 6:
                font = "font_6.ttf";
                break;
            case 7:
                font = "font_7.ttf";
                break;
            case 8:
                font = "font_8.ttf";
                break;
            case 9:
                font = "font_9.ttf";
                break;
            case 10:
                font = "font_10.ttf";
                break;
        }
        edit_view_activity_upload_quote_status.setTypeface(Typeface.createFromAsset(getAssets(),font),Typeface.BOLD);
        text_view_activity_upload_quote_screen.setTypeface(Typeface.createFromAsset(getAssets(),font),Typeface.BOLD);
    }
    public void nextColor(){
        this.color_index++;
        if (color_index == 11){
            this.color_index=1;
        }

        switch (color_index){
            case 1:
                color = "C51062";
                break;
            case 2:
                color = "034182";
                break;
            case 3:
                color = "1a2634";
                break;
            case 4:
                color = "288fb4";
                break;
            case 5:
                color = "2f3c4e";
                break;
            case 6:
                color = "3d065a";
                break;
            case 7:
                color = "449187";
                break;
            case 8:
                color = "61519f";
                break;
            case 9:
                color = "f45e5e";
                break;
            case 10:
                color = "f8aa27";
                break;
        }

        relative_layout_upload_status.setBackgroundResource(R.drawable.bg_quote);
        GradientDrawable drawable = (GradientDrawable)  relative_layout_upload_status.getBackground();
        drawable.setColor(Color.parseColor("#"+color));

        relative_layout_status_screen.setBackgroundResource(R.drawable.bg_quote);
        GradientDrawable drawable2 = (GradientDrawable)  relative_layout_status_screen.getBackground();
        drawable2.setColor(Color.parseColor("#"+color));
    }
    boolean loaded = false;
    private void initAction() {

        this.fab_font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextFont();
            }
        });
        this.fab_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextColor();
            }
        });
        this.edit_view_activity_upload_quote_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edit_view_activity_upload_quote_status, InputMethodManager.SHOW_IMPLICIT);

            }
        });
        this.fab_status_upload_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addstatus();
            }
        });

        this.edit_view_activity_upload_quote_status.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edit_view_activity_upload_quote_status.getText().toString().trim().length()==0){
                    fab_status_upload_send.hide();
                }else{
                    fab_status_upload_send.show();

                }
                text_view_activity_upload_quote_screen.setText(edit_view_activity_upload_quote_status.getText());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        this.fab_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();
            }
        });
    }


    private void initView() {
        this.linear_layout_langauges=(LinearLayout) findViewById(R.id.linear_layout_langauges);
        this.linear_layout_categories=(LinearLayout) findViewById(R.id.linear_layout_categories);
        this.text_view_activity_upload_quote_screen=(TextView) findViewById(R.id.text_view_activity_upload_quote_screen);
        this.multiple_actions_left=(FloatingActionsMenu) findViewById(R.id.multiple_actions_left);
        this.fab_image=(com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_image);
        this.fab_font=(com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_font);
        this.fab_color=(com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_color);
        this.fab_status_upload_send=(FloatingActionButton) findViewById(R.id.fab_status_upload_send);
        fab_status_upload_send.hide();

        this.edit_view_activity_upload_quote_status=(EditText) findViewById(R.id.edit_view_activity_upload_quote_status);
        edit_view_activity_upload_quote_status.setTextIsSelectable(true);
        this.relative_layout_upload_status=(RelativeLayout) findViewById(R.id.relative_layout_upload_status);

        this.relative_layout_status_screen=(RelativeLayout) findViewById(R.id.relative_layout_status_screen);


        gridLayoutManagerCategorySelect = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        gridLayoutManagerLanguageSelect = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        recycle_view_selected_category= (RecyclerView) findViewById(R.id.recycle_view_selected_category);
        recycle_view_selected_language= (RecyclerView) findViewById(R.id.recycle_view_selected_language);

        relative_layout_status_screen.setBackgroundResource(R.drawable.bg_quote);
        GradientDrawable drawable2 = (GradientDrawable)  relative_layout_status_screen.getBackground();
        drawable2.setColor(Color.parseColor("#"+color));

        relative_layout_upload_status.setBackgroundResource(R.drawable.bg_quote);
        GradientDrawable drawable = (GradientDrawable)  relative_layout_upload_status.getBackground();
        drawable.setColor(Color.parseColor("#"+color));

    }

    public void shareImage(){

        Bitmap bitmap;
        relative_layout_status_screen.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(relative_layout_status_screen.getDrawingCache());
        relative_layout_status_screen.setDrawingCacheEnabled(false);


        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Download/King_status/";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();

        Random rand = new Random();

        int  n = rand.nextInt(999999) + 100000;

        File file = new File(dir, "screen_"+n+".png");
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MediaScannerConnection.scanFile(getApplicationContext(), new String[] { file_path+ "screen_"+n+".png" },
                null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            final Uri contentUri = Uri.fromFile(new File(file_path + "screen_"+n+".png"));
            scanIntent.setData(contentUri);
            sendBroadcast(scanIntent);
        } else {
            final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
            sendBroadcast(intent);
        }
        Toasty.info(getApplicationContext(), "Status has been saved as an image", Toast.LENGTH_SHORT, true).show();

        Uri imageUri = FileProvider.getUriForFile(UploadQuoteActivity.this, getApplicationContext().getPackageName() + ".provider",file);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

        final String final_text = getResources().getString(R.string.download_more_from_link);
        shareIntent.putExtra(Intent.EXTRA_TEXT,final_text );
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(Intent.createChooser(shareIntent, "Shared via " + getResources().getString(R.string.app_name) ));
        } catch (android.content.ActivityNotFoundException ex) {
            Toasty.error(getApplicationContext(), "No App installed", Toast.LENGTH_SHORT, true).show();
        }

    }
    public void addstatus(){


        PrefManager prf= new PrefManager(getApplicationContext());
        // if (prf.getString("LOGGED").toString().equals("TRUE")){

        byte[] data = new byte[0];
        String status_final ="";
        try {
            data = edit_view_activity_upload_quote_status.getText().toString().getBytes("UTF-8");
            status_final = Base64.encodeToString(data, Base64.DEFAULT);

        } catch (UnsupportedEncodingException e) {
            status_final = edit_view_activity_upload_quote_status.getText().toString();
            e.printStackTrace();
        }

        String id_ser=  prf.getString("ID_USER");
        String key_ser=  prf.getString("TOKEN_USER");

        fab_status_upload_send.hide();
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<ApiResponse> call = service.uploadQuote(id_ser,key_ser,status_final,color,font_index,getSelectedLanguages(),getSelectedCategories());//(prf.getString("ID_USER").toString()
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getCode()==200){
                        Toasty.success(getApplication(),getResources().getString(R.string.status_upload_success),Toast.LENGTH_LONG).show();
                        finish();
                    }
                }else{
                    Toasty.error(getApplicationContext(), getResources().getString(R.string.no_connexion)+" - Plrease retry",Toast.LENGTH_SHORT).show();
                    fab_status_upload_send.show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toasty.error(getApplicationContext(), getResources().getString(R.string.no_connexion)+" - Plrease retry",Toast.LENGTH_SHORT).show();
                fab_status_upload_send.show();
            }
        });

    }
    private void getCategory() {
        register_progress= ProgressDialog.show(this, null,getResources().getString(R.string.operation_progress), true);

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Category>> call = service.categoriesImageAll();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(response.isSuccessful()){
                        categoriesListObj.clear();
                        for (int i = 0; i < response.body().size(); i++) {

                            categoriesListObj.add(response.body().get(i));
                        }
                        categorySelectAdapter = new CategorySelectAdapter(UploadQuoteActivity.this, categoriesListObj, true, UploadQuoteActivity.this);
                        recycle_view_selected_category.setHasFixedSize(true);
                        recycle_view_selected_category.setAdapter(categorySelectAdapter);
                        recycle_view_selected_category.setLayoutManager(gridLayoutManagerCategorySelect);
                        if (response.body().size()>0) {
                            linear_layout_categories.setVisibility(View.VISIBLE);
                        }
                }else {

                }
                register_progress.dismiss();
                getLanguages();

            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                register_progress.dismiss();
                getLanguages();

            }
        });
    }
    private void getLanguages(){
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Language>> call = service.languageAll();
        call.enqueue(new Callback<List<Language>>() {
            @Override
            public void onResponse(Call<List<Language>> call, Response<List<Language>> response) {
                if(response.isSuccessful()) {
                        List<String> colortitles = new ArrayList<String>();
                        languageList.clear();
                        for (int i = 0; i < response.body().size(); i++) {
                            if (i != 0) {
                                languageList.add(response.body().get(i));
                                colortitles.add(response.body().get(i).getLanguage());
                            }
                        }
                        languageSelectAdapter = new LanguageSelectAdapter(UploadQuoteActivity.this, languageList, true, UploadQuoteActivity.this);
                        recycle_view_selected_language.setHasFixedSize(true);
                        recycle_view_selected_language.setAdapter(languageSelectAdapter);
                        recycle_view_selected_language.setLayoutManager(gridLayoutManagerLanguageSelect);
                    if (response.body().size()>1) {
                        linear_layout_langauges.setVisibility(View.VISIBLE);
                    }

                    //fab_save_upload.show();
                }
                register_progress.dismiss();
            }
            @Override
            public void onFailure(Call<List<Language>> call, Throwable t) {
                register_progress.dismiss();

            }
        });
    }

    @Override
    public void onItemSelected(Language item) {

    }

    @Override
    public void onItemSelected(Category item) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
    }

    @Override
    public void onProgressUpdate(int percentage) {

    }

    @Override
    public void onError() {

    }
    public String getSelectedCategories(){
        String categories = "";
        for (int i = 0; i < categorySelectAdapter.getSelectedItems().size(); i++) {
            categories+="_"+categorySelectAdapter.getSelectedItems().get(i).getId();
        }
        Log.v("categories",categories);

        return categories;
    }
    public String getSelectedLanguages(){
        String colors = "";
        for (int i = 0; i < languageSelectAdapter.getSelectedItems().size(); i++) {
            colors+="_"+languageSelectAdapter.getSelectedItems().get(i).getId();
        }
        Log.v("colors",colors);
        return colors;
    }
    @Override
    public void onFinish() {

    }

}
