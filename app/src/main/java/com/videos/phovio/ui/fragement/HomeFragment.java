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
import com.videos.phovio.model.DataObj;
import com.videos.phovio.model.Slide;
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
public class HomeFragment extends Fragment {


    private Integer type_ads = 0;
    private Integer page = 0;

    private View view;
    private PrefManager prefManager;
    private String language = "0";
    private boolean loaded = false;
    private RelativeLayout relative_layout_load_more;
    private Button button_try_again;
    private SwipeRefreshLayout swipe_refreshl_status_fragment;
    private LinearLayout linear_layout_page_error;
    private LinearLayout linear_layout_load_status_fragment;
    private RecyclerView recycler_view_status_fragment;
    private LinearLayoutManager linearLayoutManager;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

    private List<Status> fullScreenVdeos =new ArrayList<>();
    private List<Status> statusList =new ArrayList<>();
    private List<Category> categoryList =new ArrayList<>();
    private StatusAdapter statusAdapter;
    private PeekAndPop peekAndPop;
    private Integer item = 0 ;
    private Integer lines_beetween_ads = 8 ;
    private Boolean native_ads_enabled = false ;
    private List<Slide> slideList=new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        this.view =  inflater.inflate(R.layout.fragment_home, container, false);
        this.prefManager= new PrefManager(getActivity().getApplicationContext());

        this.language=prefManager.getString("LANGUAGE_DEFAULT");

        initView();
        initAction();
        loadData();
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


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


    }
    private void initAction() {
        this.swipe_refreshl_status_fragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fullScreenVdeos.clear();
                categoryList.clear();
                statusList.clear();
                slideList.clear();
                statusAdapter.notifyDataSetChanged();
                item = 0;
                page = 0;
                loading = true;
                loadData();
            }
        });
        button_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullScreenVdeos.clear();
                categoryList.clear();
                statusList.clear();
                slideList.clear();
                statusAdapter.notifyDataSetChanged();
                item = 0;
                page = 0;
                loading = true;
                loadData();
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
        this.swipe_refreshl_status_fragment=(SwipeRefreshLayout) view.findViewById(R.id.swipe_refreshl_status_fragment);
        this.linear_layout_page_error=(LinearLayout) view.findViewById(R.id.linear_layout_page_error);
        this.linear_layout_load_status_fragment=(LinearLayout) view.findViewById(R.id.linear_layout_load_status_fragment);
        this.recycler_view_status_fragment=(RecyclerView) view.findViewById(R.id.recycler_view_status_fragment);
        this.linearLayoutManager=  new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        this.peekAndPop = new PeekAndPop.Builder(getActivity())
                .parentViewGroupToDisallowTouchEvents(recycler_view_status_fragment)
                .peekLayout(R.layout.dialog_view)
                .build();
        statusAdapter =new StatusAdapter(statusList,categoryList,getActivity(),peekAndPop,slideList,fullScreenVdeos,0,"","","home");
        recycler_view_status_fragment.setHasFixedSize(true);
        recycler_view_status_fragment.setAdapter(statusAdapter);
        recycler_view_status_fragment.setLayoutManager(linearLayoutManager);
        recycler_view_status_fragment.addOnScrollListener(new RecyclerView.OnScrollListener()
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

    public void loadData(){
        swipe_refreshl_status_fragment.setRefreshing(true);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<DataObj> call = service.fisrtData(language);
        call.enqueue(new Callback<DataObj>() {
            @Override
            public void onResponse(Call<DataObj> call, Response<DataObj> response) {
                apiClient.FormatData(getActivity(),response);



                if (response.isSuccessful()){
                    if (response.body().getCategories().size()==0
                            && response.body().getSlides().size()==0
                            && response.body().getFullscreen().size()==0
                            && response.body().getStatus().size()==0
                            )
                    {
                        linear_layout_page_error.setVisibility(View.GONE);
                        recycler_view_status_fragment.setVisibility(View.GONE);
                    }else {
                        for (int i = 0; i < response.body().getCategories().size(); i++) {
                            if (i<3){
                                categoryList.add(response.body().getCategories().get(i));
                            }else {
                                categoryList.add(null);
                                break;
                            }
                        }
                        if (response.body().getCategories().size()>0){
                            statusList.add(new Status().setViewType(1));
                        }
                        for (int i = 0; i < response.body().getSlides().size(); i++) {
                            slideList.add(response.body().getSlides().get(i));
                        }
                        if (response.body().getSlides().size()>0){
                            statusList.add(new Status().setViewType(8));
                        }
                        for (int i = 0; i < response.body().getFullscreen().size(); i++) {
                            fullScreenVdeos.add(response.body().getFullscreen().get(i).setViewType(1));
                        }
                        if (response.body().getFullscreen().size()>0){
                            statusList.add(new Status().setViewType(10));
                        }

                        for (int i = 0; i < response.body().getStatus().size(); i++) {
                            statusList.add(response.body().getStatus().get(i));
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
                        page++;
                        loading=true;
                        statusAdapter.notifyDataSetChanged();
                        linear_layout_page_error.setVisibility(View.GONE);
                        recycler_view_status_fragment.setVisibility(View.VISIBLE);
                    }

                }else{
                    linear_layout_page_error.setVisibility(View.VISIBLE);
                    recycler_view_status_fragment.setVisibility(View.GONE);
                }
                statusAdapter.notifyDataSetChanged();
                swipe_refreshl_status_fragment.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<DataObj> call, Throwable t) {
            }
        });
    }
    public void loadNextStatus(){
        relative_layout_load_more.setVisibility(View.VISIBLE);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Status>> call = service.imageAll(page,"created",language);
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
