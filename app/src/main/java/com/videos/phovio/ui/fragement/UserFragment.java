package com.videos.phovio.ui.fragement;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
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
import com.videos.phovio.model.Status;
import com.videos.phovio.Provider.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class UserFragment extends Fragment {
    private RelativeLayout relative_layout_load_more;
    private Button button_try_again;
    private SwipeRefreshLayout swipe_refreshl_search_fragment;
    private LinearLayout linear_layout_page_error;
    private LinearLayout linear_layout_load_search_fragment;
    private RecyclerView recycler_view_search_fragment;
    private LinearLayoutManager linearLayoutManager;
    private List<Status> fillvideoList =new ArrayList<>();



    private List<Status> statusList =new ArrayList<Status>();



    private StatusAdapter statusAdapter;

    private View view;
    private Integer type_ads = 0;

    private Integer page = 0;
    private Boolean loaded=false;

    private Integer user;
    private String type;
    private PrefManager prefManager;
    private String language;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;
    private PeekAndPop peekAndPop;
    private int me;

    private Integer item = 0 ;
    private Integer lines_beetween_ads = 8 ;
    private Boolean native_ads_enabled = false ;
    private String name;
    private String image;


    public UserFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.prefManager= new PrefManager(getActivity().getApplicationContext());

        name = getArguments().getString("name");
        image = getArguments().getString("image");
        user = getArguments().getInt("user");
        type = getArguments().getString("type");
        try{
            me = Integer.parseInt(prefManager.getString("ID_USER"));

        }catch (Exception e){
            me=-1;
        }


        this.language=prefManager.getString("LANGUAGE_DEFAULT");

        view= inflater.inflate(R.layout.fragment_user, container, false);

        iniView(view);
        initAction();


        loadFullscreen();

        return view;
    }


    private void getStatus() {


        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Status>> call;
        if (user == me){
            call = service.meImage(page,user);
        }else{
            call= service.userImage("created",language,user,page);
        }
        swipe_refreshl_search_fragment.setRefreshing(true);

        call.enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Call<List<Status>> call, Response<List<Status>> response) {
                if (fillvideoList.size()>0){
                    statusList.add(new Status().setViewType(10));
                    statusAdapter.notifyDataSetChanged();
                }
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
                    recycler_view_search_fragment.setVisibility(View.VISIBLE);
                    linear_layout_load_search_fragment.setVisibility(View.GONE);
                    linear_layout_page_error.setVisibility(View.GONE);

                }else{
                    recycler_view_search_fragment.setVisibility(View.GONE);
                    linear_layout_load_search_fragment.setVisibility(View.GONE);
                    linear_layout_page_error.setVisibility(View.VISIBLE);
                }
                swipe_refreshl_search_fragment.setRefreshing(false);

            }
            @Override
            public void onFailure(Call<List<Status>> call, Throwable t) {
                recycler_view_search_fragment.setVisibility(View.GONE);
                linear_layout_load_search_fragment.setVisibility(View.GONE);
                linear_layout_page_error.setVisibility(View.VISIBLE);
                swipe_refreshl_search_fragment.setRefreshing(false);

            }
        });
    }

    public void loadFullscreen(){

        swipe_refreshl_search_fragment.setRefreshing(true);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Status>> call ;
        if (user == me){
            call = service.meFullScreen(page,user);
        }else{
            call= service.userFullScnreen("created",language,user,page);
        }
        call.enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Call<List<Status>> call, Response<List<Status>> response) {
                if(response.isSuccessful()){
                    if (response.body().size()!=0){
                        fillvideoList.clear();
                        for (int i=0;i<response.body().size();i++){
                            fillvideoList.add(response.body().get(i).setViewType(1));
                        }
                    }
                }
                getStatus();
            }
            @Override
            public void onFailure(Call<List<Status>> call, Throwable t) {
                getStatus();
            }
        });
    }
    private void getStatusNext() {

        linear_layout_load_search_fragment.setVisibility(View.VISIBLE);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Status>> call;
        if (user == me){
            call = service.meImage(page,user);
        }else{
            call= service.userImage("created",language,user,page);
        }

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

                    }
                    linear_layout_load_search_fragment.setVisibility(View.GONE);
                }

            }
            @Override
            public void onFailure(Call<List<Status>> call, Throwable t) {
                linear_layout_load_search_fragment.setVisibility(View.GONE);

            }
        });
    }


    public void iniView(View  view){
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
        this.swipe_refreshl_search_fragment=(SwipeRefreshLayout) view.findViewById(R.id.swipe_refreshl_search_fragment);
        this.linear_layout_page_error=(LinearLayout) view.findViewById(R.id.linear_layout_page_error);
        this.linear_layout_load_search_fragment=(LinearLayout) view.findViewById(R.id.linear_layout_load_search_fragment);
        this.recycler_view_search_fragment=(RecyclerView) view.findViewById(R.id.recycler_view_search_fragment);
        this.linearLayoutManager=  new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        this.peekAndPop = new PeekAndPop.Builder(getActivity())
                .parentViewGroupToDisallowTouchEvents(recycler_view_search_fragment)
                .peekLayout(R.layout.dialog_view)
                .build();
        statusAdapter =new StatusAdapter(statusList,null,getActivity(),peekAndPop,null,fillvideoList,user,name,image,"user");
        recycler_view_search_fragment.setHasFixedSize(true);
        recycler_view_search_fragment.setAdapter(statusAdapter);
        recycler_view_search_fragment.setLayoutManager(linearLayoutManager);

        recycler_view_search_fragment.setVisibility(View.GONE);
        linear_layout_load_search_fragment.setVisibility(View.VISIBLE);
        linear_layout_page_error.setVisibility(View.GONE);
        swipe_refreshl_search_fragment.setRefreshing(true);
    }

    public void initAction(){
        this.swipe_refreshl_search_fragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fillvideoList.clear();
                statusList.clear();
                statusAdapter.notifyDataSetChanged();
                page = 0;
                item = 0;
                loading=true;
                loadFullscreen();

            }
        });
        this.button_try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fillvideoList.clear();
                statusList.clear();
                statusAdapter.notifyDataSetChanged();
                page = 0;
                item = 0;
                loading=true;
                loadFullscreen();
            }
        });
        recycler_view_search_fragment.addOnScrollListener(new RecyclerView.OnScrollListener()
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
                            getStatusNext();

                        }
                    }
                }else{

                }
            }
        });
    }


}
