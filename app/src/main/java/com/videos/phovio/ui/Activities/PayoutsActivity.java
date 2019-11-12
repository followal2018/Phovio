package com.videos.phovio.ui.Activities;

import android.content.Intent;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.videos.phovio.Adapters.PayoutAdapter;
import com.videos.phovio.Provider.PrefManager;
import com.videos.phovio.R;
import com.videos.phovio.api.apiClient;
import com.videos.phovio.api.apiRest;
import com.videos.phovio.model.Payout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PayoutsActivity extends AppCompatActivity {

    private List<Payout> payoutList = new ArrayList<>();
    private ImageView imageView_empty_payout;
    private Button button_try_again;
    private SwipeRefreshLayout swipe_refreshl_image_payout;
    private LinearLayout linear_layout_page_error;
    private RecyclerView recycler_view_image_payout;
    private LinearLayoutManager linearLayoutManager;
    private PayoutAdapter adapter;
    private String from  = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payouts);
        Bundle bundle = getIntent().getExtras() ;
        try {
            this.from =  bundle.getString("from");

        }catch (Exception e){
            this.from =  null;

        }
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.withdrawals));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initAction();
        LoadTransactions();
    }

    private void initAction() {
        swipe_refreshl_image_payout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadTransactions();
            }
        });
        button_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadTransactions();
            }
        });
    }

    private void initView() {
        this.imageView_empty_payout=(ImageView) findViewById(R.id.imageView_empty_payout);
        this.button_try_again =(Button) findViewById(R.id.button_try_again);
        this.swipe_refreshl_image_payout=(SwipeRefreshLayout) findViewById(R.id.swipe_refreshl_image_payout);
        this.linear_layout_page_error=(LinearLayout) findViewById(R.id.linear_layout_page_error);
        this.recycler_view_image_payout=(RecyclerView) findViewById(R.id.recycler_view_image_payout);
        this.linearLayoutManager=  new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        this.adapter =  new PayoutAdapter(payoutList,getApplicationContext());
        this.linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view_image_payout.setHasFixedSize(true);
        recycler_view_image_payout.setAdapter(adapter);
        recycler_view_image_payout.setLayoutManager(linearLayoutManager);
    }
    @Override
    public void onBackPressed(){
        if (from!=null){
            Intent intent = new Intent(PayoutsActivity.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
            finish();
        }else{
            super.onBackPressed();
            overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
        }
        return;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (from!=null){
                    Intent intent = new Intent(PayoutsActivity.this,MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                    finish();
                }else{
                    super.onBackPressed();
                    overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void LoadTransactions(){
        swipe_refreshl_image_payout.setRefreshing(true);
        imageView_empty_payout.setVisibility(View.GONE);
        recycler_view_image_payout.setVisibility(View.GONE);
        linear_layout_page_error.setVisibility(View.GONE);
        swipe_refreshl_image_payout.setRefreshing(true);
        final PrefManager prefManager = new PrefManager(this);
        Integer id_user = 0;
        String key_user= "";
        if (prefManager.getString("LOGGED").toString().equals("TRUE")) {
            id_user=  Integer.parseInt(prefManager.getString("ID_USER"));
            key_user=  prefManager.getString("TOKEN_USER");
        }
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Payout>> call = service.userWithdrawals(id_user,key_user);
        call.enqueue(new Callback<List<Payout>>() {
            @Override
            public void onResponse(Call<List<Payout>> call, Response<List<Payout>> response) {
                swipe_refreshl_image_payout.setRefreshing(false);
                apiClient.FormatData(PayoutsActivity.this,response);

                if(response.isSuccessful()){
                    if (response.body().size()!=0){
                        payoutList.clear();
                        for (int i=0;i<response.body().size();i++){
                            payoutList.add(response.body().get(i));
                        }
                        adapter.notifyDataSetChanged();
                        imageView_empty_payout.setVisibility(View.GONE);
                        recycler_view_image_payout.setVisibility(View.VISIBLE);
                        linear_layout_page_error.setVisibility(View.GONE);
                    }else {
                        imageView_empty_payout.setVisibility(View.VISIBLE);
                        recycler_view_image_payout.setVisibility(View.GONE);
                        linear_layout_page_error.setVisibility(View.GONE);
                    }
                }else{
                    imageView_empty_payout.setVisibility(View.GONE);
                    recycler_view_image_payout.setVisibility(View.GONE);
                    linear_layout_page_error.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<List<Payout>> call, Throwable t) {
                imageView_empty_payout.setVisibility(View.GONE);
                recycler_view_image_payout.setVisibility(View.GONE);
                linear_layout_page_error.setVisibility(View.VISIBLE);
            }
        });

    }

}
