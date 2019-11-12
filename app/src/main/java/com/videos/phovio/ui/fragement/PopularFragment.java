package com.videos.phovio.ui.fragement;



import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.peekandpop.shalskar.peekandpop.PeekAndPop;
import com.videos.phovio.R;
import com.videos.phovio.Adapters.StatusAdapter;
import com.videos.phovio.api.apiClient;
import com.videos.phovio.api.apiRest;
import com.videos.phovio.model.Category;
import com.videos.phovio.model.Status;
import com.videos.phovio.Provider.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopularFragment extends Fragment {
    private Integer type_ads = 0;

    private Integer page = 0;

    private View view;
    private PrefManager prefManager;
    private String language = "0";
    private boolean loaded = false;
    private RelativeLayout relative_layout_load_more;
    private Button button_try_again;
    private SwipeRefreshLayout swipe_refreshl_popular_fragment;
    private LinearLayout linear_layout_page_error;
    private LinearLayout linear_layout_load_popular_fragment;
    private RecyclerView recycler_view_popular_fragment;
    private LinearLayoutManager linearLayoutManager;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

    private List<Status> statusList =new ArrayList<>();
    private List<Category> categoryList =new ArrayList<>();
    private StatusAdapter statusAdapter;
    private PeekAndPop peekAndPop;
    private Integer item = 0 ;
    private Integer lines_beetween_ads = 8 ;
    private Boolean native_ads_enabled = false ;


    public PopularFragment() {
        // Required empty public constructor
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            if (!loaded){
                loadStatus();
                loaded = true;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        this.view =  inflater.inflate(R.layout.fragment_popular, container, false);
        this.prefManager= new PrefManager(getActivity().getApplicationContext());

        this.language=prefManager.getString("LANGUAGE_DEFAULT");

        initView();
        initAction();

        return view;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }


    private void initAction() {
        this.swipe_refreshl_popular_fragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                loading = true;
                item = 0;
                loadStatus();
            }
        });
        button_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 0;
                item = 0;
                loading = true;

                loadStatus();
            }
        });
    }

    private void initView() {
        if (getResources().getString(R.string.FACEBOOK_ADS_ENABLED_NATIVE).equals("true")){
            native_ads_enabled=true;
            lines_beetween_ads=Integer.parseInt(getResources().getString(R.string.NATIVE_ADS_ITEM_BETWWEN_ADS));
        }
        PrefManager prefManager= new PrefManager(getActivity().getApplicationContext());
        if (prefManager.getString("SUBSCRIBED").equals("TRUE")) {
            native_ads_enabled=false;
        }
        this.relative_layout_load_more=(RelativeLayout) view.findViewById(R.id.relative_layout_load_more);
        this.button_try_again =(Button) view.findViewById(R.id.button_try_again);
        this.swipe_refreshl_popular_fragment=(SwipeRefreshLayout) view.findViewById(R.id.swipe_refreshl_popular_fragment);
        this.linear_layout_page_error=(LinearLayout) view.findViewById(R.id.linear_layout_page_error);
        this.linear_layout_load_popular_fragment=(LinearLayout) view.findViewById(R.id.linear_layout_load_popular_fragment);
        this.recycler_view_popular_fragment=(RecyclerView) view.findViewById(R.id.recycler_view_popular_fragment);
        this.linearLayoutManager=  new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        this.peekAndPop = new PeekAndPop.Builder(getActivity())
                .parentViewGroupToDisallowTouchEvents(recycler_view_popular_fragment)
                .peekLayout(R.layout.dialog_view)
                .build();
        statusAdapter =new StatusAdapter(statusList,null,getActivity(),peekAndPop,true);
        recycler_view_popular_fragment.setHasFixedSize(true);
        recycler_view_popular_fragment.setAdapter(statusAdapter);
        recycler_view_popular_fragment.setLayoutManager(linearLayoutManager);
        recycler_view_popular_fragment.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {

                    visibleItemCount    = linearLayoutManager.getChildCount();
                    totalItemCount      = linearLayoutManager.getItemCount();
                    pastVisiblesItems   = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            loadNextStatus();
                        }
                    }
                }else{

                }
            }
        });
    }

    public void loadStatus(){
        recycler_view_popular_fragment.setVisibility(View.GONE);
        linear_layout_load_popular_fragment.setVisibility(View.VISIBLE);
        linear_layout_page_error.setVisibility(View.GONE);
        swipe_refreshl_popular_fragment.setRefreshing(true);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Status>> call = service.imageAll(page, "downloads",language);
        call.enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Call<List<Status>> call, Response<List<Status>> response) {
                apiClient.FormatData(getActivity(),response);
                if(response.isSuccessful()){
                    statusList.clear();
                    if (response.body().size()!=0){
                        statusList.add(new Status().setViewType(0));
                        for (int i=0;i<response.body().size();i++){
                            statusList.add(response.body().get(i));
                            if (native_ads_enabled){
                                item++;
                                if (item == lines_beetween_ads ){
                                    item= 0;
                                    if (getResources().getString(R.string.NATIVE_ADS_TYPE).equals("admob")) {
                                        statusList.add(new Status().setViewType(11));
                                    }else if (getResources().getString(R.string.NATIVE_ADS_TYPE).equals("facebook")){
                                        statusList.add(new Status().setViewType(6));
                                    } else if (getResources().getString(R.string.NATIVE_ADS_TYPE).equals("both")){
                                        if (type_ads == 0) {
                                            statusList.add(new Status().setViewType(11));
                                            type_ads = 1;
                                        }else if (type_ads == 1){
                                            statusList.add(new Status().setViewType(6));
                                            type_ads = 0;
                                        }
                                    }
                                }
                            }
                        }
                        statusAdapter.notifyDataSetChanged();
                        page++;
                        loaded=true;
                    }else {

                    }
                    recycler_view_popular_fragment.setVisibility(View.VISIBLE);
                    linear_layout_load_popular_fragment.setVisibility(View.GONE);
                    linear_layout_page_error.setVisibility(View.GONE);

                }else{
                    recycler_view_popular_fragment.setVisibility(View.GONE);
                    linear_layout_load_popular_fragment.setVisibility(View.GONE);
                    linear_layout_page_error.setVisibility(View.VISIBLE);
                }
                swipe_refreshl_popular_fragment.setRefreshing(false);

            }
            @Override
            public void onFailure(Call<List<Status>> call, Throwable t) {
                recycler_view_popular_fragment.setVisibility(View.GONE);
                linear_layout_load_popular_fragment.setVisibility(View.GONE);
                linear_layout_page_error.setVisibility(View.VISIBLE);
                swipe_refreshl_popular_fragment.setRefreshing(false);
            }
        });
    }
    public void loadNextStatus(){
        relative_layout_load_more.setVisibility(View.VISIBLE);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Status>> call = service.imageAll(page,"downloads",language);
        call.enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Call<List<Status>> call, Response<List<Status>> response) {
                if(response.isSuccessful()){
                    if (response.body().size()!=0){
                        for (int i=0;i<response.body().size();i++){
                            statusList.add(response.body().get(i));
                            if (native_ads_enabled){
                                item++;
                                if (item == lines_beetween_ads ){
                                    item= 0;
                                    if (getResources().getString(R.string.NATIVE_ADS_TYPE).equals("admob")) {
                                        statusList.add(new Status().setViewType(11));
                                    }else if (getResources().getString(R.string.NATIVE_ADS_TYPE).equals("facebook")){
                                        statusList.add(new Status().setViewType(6));
                                    } else if (getResources().getString(R.string.NATIVE_ADS_TYPE).equals("both")){
                                        if (type_ads == 0) {
                                            statusList.add(new Status().setViewType(11));
                                            type_ads = 1;
                                        }else if (type_ads == 1){
                                            statusList.add(new Status().setViewType(6));
                                            type_ads = 0;
                                        }
                                    }
                                }
                            }
                        }
                        statusAdapter.notifyDataSetChanged();
                        page++;
                        loading=true;

                    }else {

                    }
                    relative_layout_load_more.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<List<Status>> call, Throwable t) {
                relative_layout_load_more.setVisibility(View.GONE);
            }
        });
    }

}
