package com.videos.phovio.ui.fragement;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.peekandpop.shalskar.peekandpop.PeekAndPop;
import com.videos.phovio.R;
import com.videos.phovio.Adapters.StatusAdapter;
import com.videos.phovio.model.Status;
import com.videos.phovio.Provider.DownloadStorage;
import com.videos.phovio.Provider.PrefManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tamim on 03/11/2018.
 */

public class DownloadsFragement extends Fragment {


    private RelativeLayout activity_downloads;
    private RecyclerView recycle_view_home_download;
    private ImageView imageView_empty_download;
    private SwipeRefreshLayout swipe_refreshl_home_download;
    private List<Status> statusList =new ArrayList<Status>();
    private StatusAdapter statusAdapter;

    private View view;
    private GridLayoutManager gridLayoutManager;
    private PrefManager prf;
    private PeekAndPop peekAndPop;
    private Integer item = 0 ;
    private Integer lines_beetween_ads = 8 ;
    private Boolean native_ads_enabled = false ;
    public DownloadsFragement() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            item = 0;
            getStatus();

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_downloads, container, false);
        this.prf= new PrefManager(getActivity().getApplicationContext());

        iniView(view);
        initAction();
        // getStatus();
        return view;
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
        this.activity_downloads=(RelativeLayout) view.findViewById(R.id.activity_downloads);

        this.recycle_view_home_download=(RecyclerView) view.findViewById(R.id.recycle_view_home_download);
        this.swipe_refreshl_home_download=(SwipeRefreshLayout) view.findViewById(R.id.swipe_refreshl_home_download);
        this.imageView_empty_download=(ImageView) view.findViewById(R.id.imageView_empty_download);




        this.gridLayoutManager=  new GridLayoutManager(getActivity().getApplicationContext(),1,GridLayoutManager.VERTICAL,false);


        this.recycle_view_home_download=(RecyclerView) this.view.findViewById(R.id.recycle_view_home_download);
        this.swipe_refreshl_home_download=(SwipeRefreshLayout)  this.view.findViewById(R.id.swipe_refreshl_home_download);

        this.peekAndPop = new PeekAndPop.Builder(getActivity())
                .parentViewGroupToDisallowTouchEvents(recycle_view_home_download)
                .peekLayout(R.layout.dialog_view)
                .build();
        statusAdapter =new StatusAdapter(statusList,null,getActivity(),peekAndPop,false,true);
        recycle_view_home_download.setHasFixedSize(true);
        recycle_view_home_download.setAdapter(statusAdapter);
        recycle_view_home_download.setLayoutManager(gridLayoutManager);
    }

    public void initAction(){
        this.swipe_refreshl_home_download.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                item = 0;
                getStatus();
            }
        });
    }

    public void getStatus(){

        swipe_refreshl_home_download.setRefreshing(true);
        final DownloadStorage downloadStorage= new DownloadStorage(getActivity().getApplicationContext());
        List<Status> statuses = downloadStorage.loadImagesFavorites();

        if (statuses==null){
            statuses= new ArrayList<>();
        }
        statusList.clear();
        statusList.add(new Status().setViewType(0));

        for (int i=0;i<statuses.size();i++){
            if (statuses.get(i).getViewType()!=0){
                File file = new File(statuses.get(i).getLocal());
                if (file.exists()) {
                    Status a = new Status();
                    a = statuses.get(i);
                    Log.v("MY_KIND",a.getKind());
                    statusList.add(a);
                }
            }
        }
        ArrayList<Status> new_downloads= new ArrayList<Status>();
        for (int i = 0; i < statusList.size(); i++) {
            if (statusList.get(i).getViewType()!=0) {
                new_downloads.add(statusList.get(i));
            }
        }
        downloadStorage.storeImage(new_downloads);
        if (statusList.size()!=1){
            statusAdapter.notifyDataSetChanged();
            imageView_empty_download.setVisibility(View.GONE);
            recycle_view_home_download.setVisibility(View.VISIBLE);
        }else{
            imageView_empty_download.setVisibility(View.VISIBLE);
            recycle_view_home_download.setVisibility(View.GONE);
        }
        swipe_refreshl_home_download.setRefreshing(false);

    }
}