package com.videos.phovio.ui.Activities;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.videos.phovio.Provider.PrefManager;
import com.videos.phovio.R;
import com.videos.phovio.api.apiClient;
import com.videos.phovio.api.apiRest;
import com.videos.phovio.model.ApiResponse;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RequestActivity extends AppCompatActivity {

    private Spinner spinner;
    private EditText payments_details_input_input;
    private Button send_button;
    private ProgressDialog register_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        initView();
        initAction();
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
    private void initView() {


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.request_withdrawal));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        send_button = (Button) findViewById(R.id.send_button);
        spinner = (Spinner) findViewById(R.id.spinner_payments_methode);
        payments_details_input_input = (EditText) findViewById(R.id.payments_details_input_input);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.payments_methodes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    public void initAction(){
        this.send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payments_details_input_input.getText().toString().trim().length()<5){
                    Toasty.error(getApplicationContext(),getResources().getString(R.string.error_short_value)).show();
                    return;
                }
                requestWithdrawal();
            }
        });
    }
    private void requestWithdrawal() {
        register_progress = new ProgressDialog(RequestActivity.this);
        register_progress.setCancelable(true);
        register_progress.setMessage(getResources().getString(R.string.operation_progress));
        register_progress.show();
        PrefManager prefManager= new PrefManager(getApplicationContext());
        Integer id_user = 0;
        String key_user= "";
        if (prefManager.getString("LOGGED").toString().equals("TRUE")) {
            id_user=  Integer.parseInt(prefManager.getString("ID_USER"));
            key_user=  prefManager.getString("TOKEN_USER");
        }
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<ApiResponse> call = service.requestWithdrawal(id_user,key_user,spinner.getSelectedItem().toString(),payments_details_input_input.getText().toString().trim());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                apiClient.FormatData(RequestActivity.this,response);
                if (response.isSuccessful()){
                    if (response.body().getCode().equals(200)){
                        Toasty.success(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }else{
                        Toasty.error(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
                register_progress.dismiss();
            }
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                register_progress.dismiss();
            }
        });
    }

}
