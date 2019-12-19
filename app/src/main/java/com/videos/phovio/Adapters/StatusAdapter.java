package com.videos.phovio.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.github.vivchar.viewpagerindicator.ViewPagerIndicator;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.like.LikeButton;
import com.peekandpop.shalskar.peekandpop.PeekAndPop;
import com.squareup.picasso.Picasso;
import com.videos.phovio.Provider.DownloadStorage;
import com.videos.phovio.Provider.FavoritesStorage;
import com.videos.phovio.Provider.PrefManager;
import com.videos.phovio.Provider.RewardedAdKeyStorage;
import com.videos.phovio.R;
import com.videos.phovio.api.apiClient;
import com.videos.phovio.api.apiRest;
import com.videos.phovio.model.ApiResponse;
import com.videos.phovio.model.Category;
import com.videos.phovio.model.Slide;
import com.videos.phovio.model.Status;
import com.videos.phovio.model.User;
import com.videos.phovio.ui.Activities.AllFullScreenCategoryActivity;
import com.videos.phovio.ui.Activities.AllFullScreenFollowActivity;
import com.videos.phovio.ui.Activities.AllFullScreenSearchActivity;
import com.videos.phovio.ui.Activities.AllFullScreenUserActivity;
import com.videos.phovio.ui.Activities.AllPortaitVideosActivity;
import com.videos.phovio.ui.Activities.GifActivity;
import com.videos.phovio.ui.Activities.ImageActivity;
import com.videos.phovio.ui.Activities.PlayerActivity;
import com.videos.phovio.ui.Activities.QuoteActivity;
import com.videos.phovio.ui.Activities.VideoActivity;
import com.videos.phovio.ui.view.ClickableViewPager;
import com.videos.phovio.utils.ShareUtils;
import com.whygraphics.gifview.gif.GIFView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Tamim on 08/10/2017.
 */


public class StatusAdapter extends RecyclerView.Adapter {
    private static final String WHATSAPP_ID = "com.whatsapp";
    private static final String FACEBOOK_ID = "com.facebook.katana";
    private static final String MESSENGER_ID = "com.facebook.orca";
    private static final String INSTAGRAM_ID = "com.instagram.android";
    private static final String SHARE_ID = "com.android.all";
    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    RewardedAd rewardedAd;
    boolean Isrewardcompleted = false;
    Integer SuperlikePostId = 0;
    private Integer id;
    private String type = "home";
    private String name = "";
    private String image = "";
    private Boolean downloads = false;
    private Boolean favorites = false;
    private PeekAndPop peekAndPop;
    private List<Status> fullScreenVdeos = new ArrayList<>();
    private List<Status> statusList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();
    private Activity activity;
    private InterstitialAd mInterstitialAd;
    private LinearLayoutManager linearLayoutManager;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;
    private DataSource.Factory mediaDataSourceFactory;
    private ImageView ivHideControllerButton;
    private Timeline.Window window;
    private SubscribeAdapter subscribeAdapter;
    private List<User> userList;
    private List<Slide> slideList = new ArrayList<>();
    private SlideAdapter slide_adapter;
    private SearchUserAdapter searchUserAdapter;
    private LinearLayoutManager linearLayoutManagerSearch;
    private ShareUtils shareUtils;

    public StatusAdapter(final List<Status> statusList, List<Category> categoryList, final Activity activity, final PeekAndPop peekAndPop) {
        this.shareUtils = new ShareUtils(activity);
        this.statusList = statusList;
        this.categoryList = categoryList;
        this.activity = activity;
        this.peekAndPop = peekAndPop;
        mInterstitialAd = new InterstitialAd(activity.getApplication());
        mInterstitialAd.setAdUnitId(activity.getString(R.string.ad_unit_id_interstitial));
        requestNewInterstitial();
        loadRewardedAd();
        peekAndPop.addHoldAndReleaseView(R.id.like_button_fav_image_dialog);
        peekAndPop.addHoldAndReleaseView(R.id.like_button_messenger_image_dialog);
        peekAndPop.addHoldAndReleaseView(R.id.like_button_facebook_image_dialog);
        peekAndPop.addHoldAndReleaseView(R.id.like_button_instagram_image_dialog);
        peekAndPop.addHoldAndReleaseView(R.id.like_button_share_image_dialog);
        peekAndPop.addHoldAndReleaseView(R.id.like_button_whatsapp_image_dialog);
        peekAndPop.addHoldAndReleaseView(R.id.like_button_copy_image_dialog);

        peekAndPop.setOnHoldAndReleaseListener(new PeekAndPop.OnHoldAndReleaseListener() {
            @Override
            public void onHold(View view, int i) {
                Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(40);
            }

            @Override
            public void onLeave(View view, int i) {
            }

            @Override
            public void onRelease(View view, final int position) {
                final DownloadFileFromURL downloadFileFromURL = new DownloadFileFromURL();
                switch (view.getId()) {
                    case R.id.like_button_copy_image_dialog:
                        if (mInterstitialAd.isLoaded()) {
                            if (check()) {
                                mInterstitialAd.show();
                                mInterstitialAd.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        super.onAdClosed();
                                        requestNewInterstitial();
                                        String text = null;
                                        try {
                                            byte[] data = Base64.decode(statusList.get(position).getTitle(), Base64.DEFAULT);
                                            text = new String(data, "UTF-8");
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip = ClipData.newPlainText("quote" + statusList.get(position).getId(), text);
                                        clipboard.setPrimaryClip(clip);
                                        Toasty.success(activity, activity.getResources().getString(R.string.status_copied_success)).show();
                                        addShare(position);
                                    }
                                });
                            } else {
                                String text = null;
                                try {
                                    byte[] data = Base64.decode(statusList.get(position).getTitle(), Base64.DEFAULT);
                                    text = new String(data, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("", text);
                                clipboard.setPrimaryClip(clip);
                                Toasty.success(activity, activity.getResources().getString(R.string.status_copied_success)).show();
                                addShare(position);

                            }
                        } else {
                            String text = null;
                            try {
                                byte[] data = Base64.decode(statusList.get(position).getTitle(), Base64.DEFAULT);
                                text = new String(data, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("", text);
                            clipboard.setPrimaryClip(clip);
                            Toasty.success(activity, activity.getResources().getString(R.string.status_copied_success)).show();
                            addShare(position);

                        }

                        break;
                    case R.id.like_button_facebook_image_dialog:
                        if (mInterstitialAd.isLoaded()) {
                            if (check()) {
                                mInterstitialAd.show();
                                mInterstitialAd.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        super.onAdClosed();
                                        requestNewInterstitial();

                                        /*if (!statusList.get(position).isDownloading()) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                                downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, FACEBOOK_ID);
                                            else
                                                downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, FACEBOOK_ID);
                                        }*/
                                        if (statusList.get(position).getKind().equals("quote")) {
                                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), FACEBOOK_ID, null, statusList.get(position).getTitle());
                                        } else if(statusList.get(position).getKind().equals("video")){
                                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), FACEBOOK_ID, statusList.get(position).getThumbnail(), statusList.get(position).getTitle());
                                        } else  {
                                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), FACEBOOK_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                                        }
                                    }
                                });
                            } else {

                                /*if (!statusList.get(position).isDownloading()) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                        downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, FACEBOOK_ID);
                                    else
                                        downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, FACEBOOK_ID);
                                }*/

                                if (statusList.get(position).getKind().equals("quote")) {
                                    shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), FACEBOOK_ID, null, statusList.get(position).getTitle());
                                } else if(statusList.get(position).getKind().equals("video")){
                                    shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), FACEBOOK_ID, statusList.get(position).getThumbnail(), statusList.get(position).getTitle());
                                } else  {
                                    shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), FACEBOOK_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                                }
                            }
                        } else {

                            /*if (!statusList.get(position).isDownloading()) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                    downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, FACEBOOK_ID);
                                else
                                    downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, FACEBOOK_ID);
                            }*/
                            if (statusList.get(position).getKind().equals("quote")) {
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), FACEBOOK_ID, null, statusList.get(position).getTitle());
                            } else if(statusList.get(position).getKind().equals("video")){
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), FACEBOOK_ID, statusList.get(position).getThumbnail(), statusList.get(position).getTitle());
                            } else  {
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), FACEBOOK_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                            }
                        }

                        break;
                    case R.id.like_button_messenger_image_dialog:
                        if (mInterstitialAd.isLoaded()) {
                            if (check()) {
                                mInterstitialAd.show();
                                mInterstitialAd.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        super.onAdClosed();
                                        requestNewInterstitial();
                                        if (statusList.get(position).getKind().equals("quote")) {
                                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), MESSENGER_ID, null, statusList.get(position).getTitle());
                                        } else if(statusList.get(position).getKind().equals("video")){
                                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), MESSENGER_ID, statusList.get(position).getThumbnail(), statusList.get(position).getTitle());
                                        } else  {
                                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), MESSENGER_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                                        }
                                        /*if (statusList.get(position).getKind().equals("quote")) {
                                            shareTextWith(position, MESSENGER_ID);
                                        } else {
                                            if (!statusList.get(position).isDownloading()) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                                    downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, MESSENGER_ID);
                                                else
                                                    downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, MESSENGER_ID);
                                            }
                                        }*/
                                    }
                                });
                            } else {
                                if (statusList.get(position).getKind().equals("quote")) {
                                    shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), MESSENGER_ID, null, statusList.get(position).getTitle());
                                } else if(statusList.get(position).getKind().equals("video")){
                                    shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), MESSENGER_ID, statusList.get(position).getThumbnail(), statusList.get(position).getTitle());
                                } else  {
                                    shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), MESSENGER_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                                }
                               /* if (statusList.get(position).getKind().equals("quote")) {
                                    shareTextWith(position, MESSENGER_ID);
                                } else {
                                    if (!statusList.get(position).isDownloading()) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                            downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, MESSENGER_ID);
                                        else
                                            downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, MESSENGER_ID);
                                    }
                                }*/
                            }
                        } else {
                            if (statusList.get(position).getKind().equals("quote")) {
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), MESSENGER_ID, null, statusList.get(position).getTitle());
                            } else if(statusList.get(position).getKind().equals("video")){
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), MESSENGER_ID, statusList.get(position).getThumbnail(), statusList.get(position).getTitle());
                            } else  {
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), MESSENGER_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                            }
                           /* if (statusList.get(position).getKind().equals("quote")) {
                                shareTextWith(position, MESSENGER_ID);
                            } else {
                                if (!statusList.get(position).isDownloading()) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                        downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, MESSENGER_ID);
                                    else
                                        downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, MESSENGER_ID);
                                }
                            }*/
                        }

                        break;
                    case R.id.like_button_whatsapp_image_dialog:
                        if (mInterstitialAd.isLoaded()) {
                            if (check()) {
                                mInterstitialAd.show();
                                mInterstitialAd.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        super.onAdClosed();
                                        requestNewInterstitial();
                                       /* if (statusList.get(position).getKind().equals("quote")) {
                                            shareTextWith(position, WHATSAPP_ID);
                                        } else {
                                            if (!statusList.get(position).isDownloading()) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                                    downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                                                else
                                                    downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                                            }
                                        }*/
                                        if (statusList.get(position).getKind().equals("quote")) {
                                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), WHATSAPP_ID, null, statusList.get(position).getTitle());
                                        } else if(statusList.get(position).getKind().equals("video")){
                                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), WHATSAPP_ID, statusList.get(position).getThumbnail(), statusList.get(position).getTitle());
                                        } else  {
                                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), WHATSAPP_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                                        }
                                    }
                                });
                            } else {
                                /*if (statusList.get(position).getKind().equals("quote")) {
                                    shareTextWith(position, WHATSAPP_ID);
                                } else {
                                    if (!statusList.get(position).isDownloading()) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                            downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                                        else
                                            downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                                    }
                                }*/
                                if (statusList.get(position).getKind().equals("quote")) {
                                    shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), WHATSAPP_ID, null, statusList.get(position).getTitle());
                                } else if(statusList.get(position).getKind().equals("video")){
                                    shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), WHATSAPP_ID, statusList.get(position).getThumbnail(), statusList.get(position).getTitle());
                                } else  {
                                    shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), WHATSAPP_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                                }
                            }
                        } else {
                            /*if (statusList.get(position).getKind().equals("quote")) {
                                shareTextWith(position, WHATSAPP_ID);
                            } else {
                                if (!statusList.get(position).isDownloading()) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                        downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                                    else
                                        downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                                }
                            }*/
                            if (statusList.get(position).getKind().equals("quote")) {
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), WHATSAPP_ID, null, statusList.get(position).getTitle());
                            } else if(statusList.get(position).getKind().equals("video")){
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), WHATSAPP_ID, statusList.get(position).getThumbnail(), statusList.get(position).getTitle());
                            } else  {
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), WHATSAPP_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                            }
                        }


                        break;
                    case R.id.like_button_instagram_image_dialog:


                        if (mInterstitialAd.isLoaded()) {
                            if (check()) {
                                mInterstitialAd.show();
                                mInterstitialAd.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        super.onAdClosed();
                                        requestNewInterstitial();
                                        /*if (!statusList.get(position).isDownloading()) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                                downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, INSTAGRAM_ID);
                                            else
                                                downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, INSTAGRAM_ID);
                                        }*/
                                        if (statusList.get(position).getKind().equals("quote")) {
                                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), INSTAGRAM_ID, null, statusList.get(position).getTitle());
                                        } else if(statusList.get(position).getKind().equals("video")){
                                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), INSTAGRAM_ID, statusList.get(position).getThumbnail(), statusList.get(position).getTitle());
                                        } else  {
                                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), INSTAGRAM_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                                        }
                                    }
                                });
                            } else {
                                /*if (!statusList.get(position).isDownloading()) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                        downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, INSTAGRAM_ID);
                                    else
                                        downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, INSTAGRAM_ID);
                                }*/
                                if (statusList.get(position).getKind().equals("quote")) {
                                    shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), INSTAGRAM_ID, null, statusList.get(position).getTitle());
                                } else if(statusList.get(position).getKind().equals("video")){
                                    shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), INSTAGRAM_ID, statusList.get(position).getThumbnail(), statusList.get(position).getTitle());
                                } else  {
                                    shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), INSTAGRAM_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                                }
                            }
                        } else {
                            /*if (!statusList.get(position).isDownloading()) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                    downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, INSTAGRAM_ID);
                                else
                                    downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, INSTAGRAM_ID);
                            }*/
                            if (statusList.get(position).getKind().equals("quote")) {
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), INSTAGRAM_ID, null, statusList.get(position).getTitle());
                            } else if(statusList.get(position).getKind().equals("video")){
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), INSTAGRAM_ID, statusList.get(position).getThumbnail(), statusList.get(position).getTitle());
                            } else  {
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), INSTAGRAM_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                            }
                        }


                        break;
                    case R.id.like_button_share_image_dialog:


                        if (mInterstitialAd.isLoaded()) {
                            if (check()) {
                                mInterstitialAd.show();
                                mInterstitialAd.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        super.onAdClosed();
                                        requestNewInterstitial();
                                       /* if (statusList.get(position).getKind().equals("quote")) {
//                                            shareTextWith(position, SHARE_ID);
                                            createSharableLink(position, SHARE_ID);
                                        } else {
//                                            if (!statusList.get(position).isDownloading()) {
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//                                                    downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, SHARE_ID);
//                                                else
//                                                    downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, SHARE_ID);
//                                            }
                                            createSharableLink(position, SHARE_ID);
                                        }*/
                                        if (statusList.get(position).getKind().equals("quote")) {
                                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), SHARE_ID, null, statusList.get(position).getTitle());
                                        } else if(statusList.get(position).getKind().equals("video")){
                                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), SHARE_ID, statusList.get(position).getThumbnail(), statusList.get(position).getTitle());
                                        } else  {
                                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), SHARE_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                                        }
                                    }
                                });
                            } else {
                                /*if (statusList.get(position).getKind().equals("quote")) {
//                                    shareTextWith(position, SHARE_ID);
                                    createSharableLink(position, SHARE_ID);
                                } else {
//                                    if (!statusList.get(position).isDownloading()) {
//                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//                                            downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, SHARE_ID);
//                                        else
//                                            downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, SHARE_ID);
//                                    }
                                    createSharableLink(position, SHARE_ID);
                                }*/
                                if (statusList.get(position).getKind().equals("quote")) {
                                    shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), SHARE_ID, null, statusList.get(position).getTitle());
                                } else if(statusList.get(position).getKind().equals("video")){
                                    shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), SHARE_ID, statusList.get(position).getThumbnail(), statusList.get(position).getTitle());
                                } else  {
                                    shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), SHARE_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                                }
                            }
                        } else {
                            /*if (statusList.get(position).getKind().equals("quote")) {
//                                shareTextWith(position, SHARE_ID);
                                createSharableLink(position, SHARE_ID);
                            } else {
                                *//*if (!statusList.get(position).isDownloading()) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                        downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, SHARE_ID);
                                    else
                                        downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, SHARE_ID);
                                }*//*
                                createSharableLink(position, SHARE_ID);
                            }*/
                            if (statusList.get(position).getKind().equals("quote")) {
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), SHARE_ID, null, statusList.get(position).getTitle());
                            } else if(statusList.get(position).getKind().equals("video")){
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), SHARE_ID, statusList.get(position).getThumbnail(), statusList.get(position).getTitle());
                            } else  {
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), SHARE_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                            }
                        }
                        break;
                    case R.id.like_button_fav_image_dialog:
                        final FavoritesStorage storageFavorites = new FavoritesStorage(activity.getApplicationContext());

                        List<Status> favorites_list = storageFavorites.loadImagesFavorites();
                        Boolean exist = false;
                        if (favorites_list == null) {
                            favorites_list = new ArrayList<>();
                        }
                        for (int i = 0; i < favorites_list.size(); i++) {
                            if (favorites_list.get(i).getId().equals(statusList.get(position).getId())) {
                                exist = true;
                            }
                        }
                        if (exist == false) {
                            ArrayList<Status> audios = new ArrayList<Status>();
                            for (int i = 0; i < favorites_list.size(); i++) {
                                audios.add(favorites_list.get(i));
                            }
                            audios.add(statusList.get(position));
                            storageFavorites.storeImage(audios);
                        } else {
                            ArrayList<Status> new_favorites = new ArrayList<Status>();
                            for (int i = 0; i < favorites_list.size(); i++) {
                                if (!favorites_list.get(i).getId().equals(statusList.get(position).getId())) {
                                    new_favorites.add(favorites_list.get(i));

                                }
                            }
                            if (favorites == true) {
                                statusList.remove(position);
                                notifyDataSetChanged();
                                //holder.ripple_view_wallpaper_item.setVisibility(View.GONE);
                            }
                            storageFavorites.storeImage(new_favorites);

                        }
                        notifyDataSetChanged();
                        break;

                }
            }


        });
        peekAndPop.setOnGeneralActionListener(new PeekAndPop.OnGeneralActionListener() {
            @Override
            public void onPeek(View view, int position) {

                LikeButton like_button_fav_image_dialog = (LikeButton) peekAndPop.getPeekView().findViewById(R.id.like_button_fav_image_dialog);

                final FavoritesStorage storageFavorites = new FavoritesStorage(activity.getApplicationContext());
                List<Status> statuses = storageFavorites.loadImagesFavorites();
                Boolean exist = false;
                if (statuses == null) {
                    statuses = new ArrayList<>();
                }
                for (int i = 0; i < statuses.size(); i++) {
                    if (statuses.get(i).getId().equals(statusList.get(position).getId())) {
                        exist = true;
                    }
                }
                if (exist == false) {
                    like_button_fav_image_dialog.setLiked(false);
                } else {
                    like_button_fav_image_dialog.setLiked(true);
                }


                LikeButton like_button_copy_image_dialog = (LikeButton) peekAndPop.getPeekView().findViewById(R.id.like_button_copy_image_dialog);
                LikeButton like_button_facebook_image_dialog = (LikeButton) peekAndPop.getPeekView().findViewById(R.id.like_button_facebook_image_dialog);
                LikeButton like_button_instagram_image_dialog = (LikeButton) peekAndPop.getPeekView().findViewById(R.id.like_button_instagram_image_dialog);
                RelativeLayout relative_layout_quote = (RelativeLayout) peekAndPop.getPeekView().findViewById(R.id.relative_layout_quote);
                RelativeLayout relative_layout_media = (RelativeLayout) peekAndPop.getPeekView().findViewById(R.id.relative_layout_media);
                RelativeLayout relative_layout_dialog_title = (RelativeLayout) peekAndPop.getPeekView().findViewById(R.id.relative_layout_dialog_title);
                ImageView circle_image_view_dialog_image = (ImageView) peekAndPop.getPeekView().findViewById(R.id.circle_image_view_dialog_image);
                GIFView gif_view_dialog_view_gif = (GIFView) peekAndPop.getPeekView().findViewById(R.id.gif_view_dialog_view_gif);
                final ImageView image_view_dialog_view_image = (ImageView) peekAndPop.getPeekView().findViewById(R.id.image_view_dialog_view_image);
                simpleExoPlayerView = (SimpleExoPlayerView) peekAndPop.getPeekView().findViewById(R.id.video_view_dialog_image);
                TextView text_view_view_dialog_user = (TextView) peekAndPop.getPeekView().findViewById(R.id.text_view_view_dialog_user);
                TextView text_view_view_dialog_title = (TextView) peekAndPop.getPeekView().findViewById(R.id.text_view_view_dialog_title);
                TextView text_view_quote_status = (TextView) peekAndPop.getPeekView().findViewById(R.id.text_view_quote_status);
                TextView text_view_downloads = (TextView) peekAndPop.getPeekView().findViewById(R.id.text_view_downloads);

                Picasso.with(activity.getApplicationContext()).load(statusList.get(position).getUserimage()).error(R.drawable.profile).placeholder(R.drawable.profile).into(circle_image_view_dialog_image);
                text_view_view_dialog_user.setText(statusList.get(position).getUser());
                text_view_view_dialog_title.setText(statusList.get(position).getTitle());
                text_view_downloads.setText(format(statusList.get(position).getDownloads()));

                if (statusList.get(position).getKind().equals("video") || statusList.get(position).getKind().equals("fullscreen")) {
                    like_button_copy_image_dialog.setVisibility(View.GONE);
                    like_button_facebook_image_dialog.setVisibility(View.VISIBLE);
                    like_button_instagram_image_dialog.setVisibility(View.VISIBLE);
                    relative_layout_quote.setVisibility(View.GONE);
                    relative_layout_media.setVisibility(View.VISIBLE);
                    relative_layout_dialog_title.setVisibility(View.VISIBLE);
                    simpleExoPlayerView.setVisibility(View.VISIBLE);
                    image_view_dialog_view_image.setVisibility(View.GONE);
                    gif_view_dialog_view_gif.setVisibility(View.GONE);
                    shouldAutoPlay = true;
                    bandwidthMeter = new DefaultBandwidthMeter();
                    mediaDataSourceFactory = new DefaultDataSourceFactory(activity.getApplicationContext(), Util.getUserAgent(activity.getApplication(), "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
                    window = new Timeline.Window();
                    ivHideControllerButton = (ImageView) peekAndPop.getPeekView().findViewById(R.id.exo_controller);
                    initializePlayer(position);
                } else if (statusList.get(position).getKind().equals("gif")) {
                    like_button_facebook_image_dialog.setVisibility(View.VISIBLE);
                    like_button_instagram_image_dialog.setVisibility(View.VISIBLE);
                    like_button_copy_image_dialog.setVisibility(View.GONE);
                    relative_layout_quote.setVisibility(View.GONE);
                    relative_layout_media.setVisibility(View.VISIBLE);
                    relative_layout_dialog_title.setVisibility(View.VISIBLE);
                    gif_view_dialog_view_gif.setGifResource("url:" + statusList.get(position).getOriginal());
                    simpleExoPlayerView.setVisibility(View.GONE);
                    Picasso.with(activity.getApplicationContext()).load(statusList.get(position).getThumbnail()).error(R.drawable.bg_transparant).placeholder(R.drawable.bg_transparant).into(image_view_dialog_view_image);

                    image_view_dialog_view_image.setVisibility(View.GONE);
                    gif_view_dialog_view_gif.setVisibility(View.VISIBLE);
                    gif_view_dialog_view_gif.setOnSettingGifListener(new GIFView.OnSettingGifListener() {
                        @Override
                        public void onSuccess(GIFView view, Exception e) {
                            image_view_dialog_view_image.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(GIFView view, Exception e) {
                        }
                    });
                } else if (statusList.get(position).getKind().equals("image")) {
                    like_button_facebook_image_dialog.setVisibility(View.VISIBLE);
                    like_button_instagram_image_dialog.setVisibility(View.VISIBLE);
                    like_button_copy_image_dialog.setVisibility(View.GONE);
                    relative_layout_quote.setVisibility(View.GONE);
                    relative_layout_media.setVisibility(View.VISIBLE);
                    relative_layout_dialog_title.setVisibility(View.VISIBLE);
                    gif_view_dialog_view_gif.setVisibility(View.GONE);
                    simpleExoPlayerView.setVisibility(View.GONE);
                    image_view_dialog_view_image.setVisibility(View.VISIBLE);
                    Picasso.with(activity.getApplicationContext()).load(statusList.get(position).getOriginal()).error(R.drawable.bg_transparant).placeholder(R.drawable.bg_transparant).into(image_view_dialog_view_image);
                } else if (statusList.get(position).getKind().equals("quote")) {
                    like_button_copy_image_dialog.setVisibility(View.VISIBLE);
                    like_button_facebook_image_dialog.setVisibility(View.GONE);
                    like_button_instagram_image_dialog.setVisibility(View.GONE);
                    relative_layout_dialog_title.setVisibility(View.GONE);
                    relative_layout_quote.setVisibility(View.VISIBLE);
                    relative_layout_media.setVisibility(View.GONE);

                    String text = null;
                    try {
                        byte[] data = Base64.decode(statusList.get(position).getTitle(), Base64.DEFAULT);
                        text = new String(data, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    text_view_quote_status.setText(text);
                    relative_layout_quote.setBackgroundResource(R.drawable.bg_quote_background);
                    Typeface tf = Typeface.createFromAsset(activity.getAssets(), "font_" + statusList.get(position).getFont() + ".ttf");
                    text_view_quote_status.setTypeface(tf);
                    GradientDrawable drawable = (GradientDrawable) relative_layout_quote.getBackground();
                    drawable.setColor(Color.parseColor("#" + statusList.get(position).getColor()));
                }
            }

            @Override
            public void onPop(View view, int position) {
                try {
                    releasePlayer();
                    bandwidthMeter = null;
                    mediaDataSourceFactory = null;
                    window = null;
                } catch (Exception e) {

                }
                addView(position);

            }
        });
    }

    public StatusAdapter(final List<Status> statusList, List<Category> categoryList, final Activity activity, final PeekAndPop peekAndPop, Boolean favorites_) {
        this(statusList, categoryList, activity, peekAndPop);
        this.favorites = favorites_;
    }

    public StatusAdapter(final List<Status> statusList, List<Category> categoryList, final Activity activity, final PeekAndPop peekAndPop, List<Slide> slideList_, List<Status> _fullScreenVdeos, Integer _id, String _name, String _image, String _type) {
        this(statusList, categoryList, activity, peekAndPop);
        this.slideList = slideList_;
        this.fullScreenVdeos = _fullScreenVdeos;
        this.id = _id;
        this.name = _name;
        this.image = _image;
        this.type = _type;
    }

    public StatusAdapter(final List<Status> statusList, List<Category> categoryList, final Activity activity, final PeekAndPop peekAndPop, List<Slide> slideList_, List<Status> _fullScreenVdeos, Integer _id, String _name, String _image, String _type, List<User> userList_) {
        this(statusList, categoryList, activity, peekAndPop);
        this.userList = userList_;
        this.slideList = slideList_;
        this.fullScreenVdeos = _fullScreenVdeos;
        this.id = _id;
        this.name = _name;
        this.image = _image;
        this.type = _type;
    }

    public StatusAdapter(final List<Status> statusList, List<Category> categoryList, final Activity activity, final PeekAndPop peekAndPop, Boolean favorites_, Boolean downloads_) {
        this(statusList, categoryList, activity, peekAndPop);
        this.favorites = favorites_;
        this.downloads = downloads_;
    }

    public StatusAdapter(final List<Status> statusList, List<Category> categoryList, final Activity activity, final PeekAndPop peekAndPop, Boolean favorites_, Boolean downloads_, List<User> userList_, List<Status> _fullScreenVdeos, String _name, String _type) {
        this(statusList, categoryList, activity, peekAndPop);
        this.favorites = favorites_;
        this.downloads = downloads_;
        this.userList = userList_;
        this.fullScreenVdeos = _fullScreenVdeos;
        this.type = _type;
        this.name = _name;
    }

    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    private void loadRewardedAd() {
        if (rewardedAd == null || !rewardedAd.isLoaded()) {
//            isLoading = true;
            RewardedAdKeyStorage rewardedAdKeyStorage = new RewardedAdKeyStorage(activity);
            rewardedAd = new RewardedAd(activity, rewardedAdKeyStorage.getRewardedAdKey());

            rewardedAd.loadAd(
                    new PublisherAdRequest.Builder().addTestDevice("F512225BC55B6A45A3A6A6EF6377EF8E")
                            .addTestDevice("F131SDDBC55B6A45A3A6A6EF6377EF8E")
                            .addTestDevice("WSDSDSDESDB6A45A3A6A6EF63S77EF8E")
                            .addTestDevice("F1212121ESDB6A45A3A6A6EF63S77EF8E")
                            .addTestDevice("ASDSADSSADSASDA45A3A6A6EF6377EF8E").build(),
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onRewardedAdLoaded() {
                            // Ad successfully loaded.
//                            MainActivity.this.isLoading = false;
//                            Toast.makeText(activity, "onRewardedAdLoaded", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onRewardedAdFailedToLoad(int errorCode) {
                            // Ad failed to load.
//                            MainActivity.this.isLoading = false;
//                            Toast.makeText(activity, "onRewardedAdFailedToLoad=" + errorCode, Toast.LENGTH_SHORT)
//                                    .show();
                        }
                    });
        }
    }

    private void showRewardedVideo(final Integer superlikePostId, final Integer userid, final Integer position) {
//        showVideoButton.setVisibility(View.INVISIBLE);
        if (rewardedAd.isLoaded()) {
            RewardedAdCallback adCallback =
                    new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {
                            // Ad opened.
//                            Toast.makeText(getActivity(), "onRewardedAdOpened", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            if (Isrewardcompleted == false) {
                                Toasty.error(activity.getApplicationContext(), "You have to watch full video to earn points", Toast.LENGTH_SHORT, true).show();
                            }
                            // Ad closed.
//                            Toast.makeText(getActivity(), "onRewardedAdClosed", Toast.LENGTH_SHORT).show();
                            // Preload the next rewarded ad.
                            loadRewardedAd();
                        }

                        @Override
                        public void onUserEarnedReward(RewardItem rewardItem) {


                            Isrewardcompleted = true;
                            Toasty.success(activity.getApplicationContext(), "Success", Toast.LENGTH_SHORT, true).show();
                            AddSuperLikePoints(superlikePostId, userid, position);
                        }

                        @Override
                        public void onRewardedAdFailedToShow(int errorCode) {

                            // Ad failed to display
//                            Toast.makeText(getActivity(), "onRewardedAdFailedToShow", Toast.LENGTH_SHORT)
//                                    .show();
                        }
                    };
            rewardedAd.show(activity, adCallback);
        } else {
            Toasty.error(activity.getApplicationContext(), "Ads will Available in Next 15 Min.", Toast.LENGTH_SHORT, true).show();
        }
    }

    public void AddSuperLikePoints(Integer postid, Integer userid, final Integer position) {
        final PrefManager prefManager = new PrefManager(activity);
        Integer id_user = 0;
        String key_user = "";
        if (prefManager.getString("LOGGED").toString().equals("TRUE")) {
            id_user = Integer.parseInt(prefManager.getString("ID_USER"));
            key_user = prefManager.getString("TOKEN_USER");
        }
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);

        Call<ApiResponse> call = service.AddSuperlike(id_user, key_user, postid.toString());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
//                swipe_refreshl_earning_activity.setRefreshing(false);
                apiClient.FormatData(activity, response);

                if (response.isSuccessful()) {

                    setSuperLikedata(position);
                    //  AddSuperLikePoints(statusList.get(position).getId(), statusList.get(position).getUserid());

//                    int SuperLikeCount = Integer.parseInt(prefManager.getString("SuperLikeCount"));
//                    prefManager.setInt("SuperLikeCount", SuperLikeCount++);
//                    Log.e("SuperLikeCount", "" + Integer.parseInt(prefManager.getString("SuperLikeCount")));
//                    if (responsese.body().size() != 0) {
//                        transactionList.clear();
//                        for (int i = 0; i < response.body().size(); i++) {
//                            transactionList.add(response.body().get(i));
//                        }
//                        adapter.notifyDataSetChanged();
//                        recycler_view_transaction_earning_activity.setNestedScrollingEnabled(false);
//                        page++;
//                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    private void initializePlayer(Integer position) {

        simpleExoPlayerView.requestFocus();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(activity, trackSelector);

        simpleExoPlayerView.setPlayer(player);

        player.setPlayWhenReady(shouldAutoPlay);
        simpleExoPlayerView.setControllerHideOnTouch(false);
        simpleExoPlayerView.setUseController(false);
        simpleExoPlayerView.setControllerAutoShow(false);
/*        MediaSource mediaSource = new HlsMediaSource(Uri.parse("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"),
                mediaDataSourceFactory, mainHandler, null);*/

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(statusList.get(position).getOriginal()),
                mediaDataSourceFactory, extractorsFactory, null, null);
        if (downloads) {
//            Log.v("this is path",statusList.get(position).getPath());
            Uri imageUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", new File(statusList.get(position).getLocal()));
            mediaSource = new ExtractorMediaSource(imageUri,
                    mediaDataSourceFactory, extractorsFactory, null, null);
        }


        player.prepare(mediaSource);
        simpleExoPlayerView.hideController();

        ivHideControllerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleExoPlayerView.hideController();
            }
        });
    }

    private void releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {

            case 0: {
                View v0 = inflater.inflate(R.layout.item_empty, parent, false);
                viewHolder = new StatusAdapter.EmptyHolder(v0);
                break;
            }
            case 1: {
                View v1 = inflater.inflate(R.layout.item_categories, parent, false);
                viewHolder = new StatusAdapter.CategoriesHolder(v1);
                break;
            }
            case 2: {
                View v2 = inflater.inflate(R.layout.item_video, parent, false);
                viewHolder = new StatusAdapter.VideoHolder(v2);
                break;
            }
            case 3: {
                View v3 = inflater.inflate(R.layout.item_image, parent, false);
                viewHolder = new StatusAdapter.ImageHolder(v3);
                break;
            }
            case 4: {
                View v4 = inflater.inflate(R.layout.item_gif, parent, false);
                viewHolder = new StatusAdapter.GifHolder(v4);
                break;
            }
            case 5: {
                View v5 = inflater.inflate(R.layout.item_quote, parent, false);
                viewHolder = new StatusAdapter.QuoteHolder(v5);
                break;
            }
            case 6: {
                View v6 = inflater.inflate(R.layout.item_facebook_ads, parent, false);
                viewHolder = new FacebookNativeHolder(v6);
                break;
            }
            case 7: {
                View v7 = inflater.inflate(R.layout.item_subscriptions, parent, false);
                viewHolder = new SubscriptionHolder(v7);
                break;
            }
            case 8: {
                View v8 = inflater.inflate(R.layout.item_slide, parent, false);
                viewHolder = new SlideHolder(v8);
                break;
            }
            case 9: {
                View v9 = inflater.inflate(R.layout.item_users_search, parent, false);
                viewHolder = new SearchUserListHolder(v9);
                break;
            }
            case 10: {
                View v10 = inflater.inflate(R.layout.item_portrait_list, parent, false);
                viewHolder = new PortraitVideoListHolder(v10);
                break;
            }
            case 11: {
                View v11 = inflater.inflate(R.layout.item_admob_native_ads, parent, false);
                viewHolder = new AdmobNativeHolder(v11);
                break;
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_parent, final int position) {
        switch (getItemViewType(position)) {
            case 0: {

                break;
            }
            case 1: {
                final StatusAdapter.CategoriesHolder holder = (StatusAdapter.CategoriesHolder) holder_parent;
                holder.categoryVideoAdapter.notifyDataSetChanged();

            }
            break;
            case 2: {

                final StatusAdapter.VideoHolder holder = (StatusAdapter.VideoHolder) holder_parent;
                peekAndPop.addLongClickView(holder.image_view_thumbnail_item_video, position);

                Picasso.with(activity.getApplicationContext()).load(statusList.get(position).getThumbnail()).error(R.drawable.bg_transparant).placeholder(R.drawable.bg_transparant).into(holder.image_view_thumbnail_item_video);
                Picasso.with(activity.getApplicationContext()).load(statusList.get(position).getUserimage()).error(R.drawable.profile).placeholder(R.drawable.profile).into(holder.circle_image_view_item_video_user);

                holder.text_view_downloads_item_video.setText(format(statusList.get(position).getDownloads()));
                holder.text_view_views_item_video.setText(format(statusList.get(position).getViews()));
                holder.text_view_created_item_video.setText(statusList.get(position).getCreated());

                holder.text_view_item_video_name_user.setText(statusList.get(position).getUser());
                holder.text_view_item_video_title.setText(statusList.get(position).getTitle());
                if (statusList.get(position).getReview())
                    holder.relative_layout_item_video_review.setVisibility(View.VISIBLE);
                else
                    holder.relative_layout_item_video_review.setVisibility(View.GONE);
                if (downloads) {
                    holder.image_view_delete_item_video.setVisibility(View.VISIBLE);
                    holder.image_view_fav_item_video.setVisibility(View.GONE);

                } else {
                    holder.image_view_delete_item_video.setVisibility(View.GONE);
                    holder.image_view_fav_item_video.setVisibility(View.VISIBLE);
                }
                if (!statusList.get(position).isDownloading()) {
                    holder.relative_layout_progress_item_video.setVisibility(View.GONE);
                } else {
                    holder.relative_layout_progress_item_video.setVisibility(View.VISIBLE);
                    holder.progress_bar_item_video.setProgress(statusList.get(position).getProgress());
                    holder.text_view_progress_item_video.setText("Downloading : " + statusList.get(position).getProgress() + " %");
                }
                final FavoritesStorage storageFavorites = new FavoritesStorage(activity.getApplicationContext());
                List<Status> statuses = storageFavorites.loadImagesFavorites();
                Boolean exist = false;
                if (statuses == null) {
                    statuses = new ArrayList<>();
                }
                for (int i = 0; i < statuses.size(); i++) {
                    if (statuses.get(i).getId().equals(statusList.get(position).getId())) {
                        exist = true;
                    }
                }
                if (exist) {
                    holder.image_view_fav_item_video.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_black));
                } else {
                    holder.image_view_fav_item_video.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_border));
                }

                holder.image_view_superlike_item_image.setVisibility(View.GONE);
                holder.image_view_superlike_item_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        PrefManager prefManager = new PrefManager(activity);
                        String SuperLikeIds = prefManager.getString("SuperLikeUserIds");
                        Date today = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        if (prefManager.getString("SuperLikeDate").equalsIgnoreCase(sdf.format(today))) {
                            if (SuperLikeIds.contains(statusList.get(position).getUserid().toString())) {
                                Toasty.error(activity, "Already Superlike this user post", Toast.LENGTH_SHORT, true).show();
                            } else {
                                showDialog(statusList.get(position).getId(), statusList.get(position).getUserid(), position);
                            }
                        } else {
                            prefManager.setInt("SuperLikeCount", 0);
                            prefManager.setString("SuperLikeUserIds", "");
                            prefManager.setString("SuperLikeDate", sdf.format(today));
//                            setSuperLikedata(position);
//                            AddSuperLikePoints(statusList.get(position).getId(), statusList.get(position).getUserid());
                            showDialog(statusList.get(position).getId(), statusList.get(position).getUserid(), position);
                        }

                    }
                });
                holder.image_view_thumbnail_item_video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, VideoActivity.class);
                        if (statusList.get(position).getKind().equals("video")) {
                            intent = new Intent(activity, VideoActivity.class);
                        } else if (statusList.get(position).getKind().equals("fullscreen")) {
                            intent = new Intent(activity, PlayerActivity.class);
                        }
                        intent.putExtra("id", statusList.get(position).getId());
                        intent.putExtra("title", statusList.get(position).getTitle());
                        intent.putExtra("kind", statusList.get(position).getKind());
                        intent.putExtra("description", statusList.get(position).getDescription());
                        intent.putExtra("review", statusList.get(position).getReview());
                        intent.putExtra("comment", statusList.get(position).getComment());
                        intent.putExtra("comments", statusList.get(position).getComments());
                        intent.putExtra("downloads", statusList.get(position).getDownloads());
                        intent.putExtra("views", statusList.get(position).getViews());
                        intent.putExtra("font", statusList.get(position).getFont());

                        intent.putExtra("user", statusList.get(position).getUser());
                        intent.putExtra("userid", statusList.get(position).getUserid());
                        intent.putExtra("userimage", statusList.get(position).getUserimage());
                        intent.putExtra("thumbnail", statusList.get(position).getThumbnail());
                        intent.putExtra("original", statusList.get(position).getOriginal());
                        intent.putExtra("type", statusList.get(position).getType());
                        intent.putExtra("extension", statusList.get(position).getExtension());
                        intent.putExtra("color", statusList.get(position).getColor());
                        intent.putExtra("created", statusList.get(position).getCreated());
                        intent.putExtra("tags", statusList.get(position).getTags());
                        intent.putExtra("like", statusList.get(position).getLike());
                        intent.putExtra("love", statusList.get(position).getLove());
                        intent.putExtra("woow", statusList.get(position).getWoow());
                        intent.putExtra("angry", statusList.get(position).getAngry());
                        intent.putExtra("sad", statusList.get(position).getSad());
                        intent.putExtra("haha", statusList.get(position).getHaha());
                        intent.putExtra("local", statusList.get(position).getLocal());
                        intent.putExtra("position", position);
                        intent.putExtra("superLikeCount", statusList.get(position).getSuperLikeCount());

                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                    }
                });
                holder.image_view_fav_item_video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Status> favorites_list = storageFavorites.loadImagesFavorites();
                        Boolean exist = false;
                        if (favorites_list == null) {
                            favorites_list = new ArrayList<>();
                        }
                        for (int i = 0; i < favorites_list.size(); i++) {
                            if (favorites_list.get(i).getId().equals(statusList.get(position).getId())) {
                                exist = true;
                            }
                        }
                        if (exist == false) {
                            ArrayList<Status> audios = new ArrayList<Status>();
                            for (int i = 0; i < favorites_list.size(); i++) {
                                audios.add(favorites_list.get(i));
                            }
                            audios.add(statusList.get(position));
                            storageFavorites.storeImage(audios);
                            holder.image_view_fav_item_video.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_black));
                        } else {
                            ArrayList<Status> new_favorites = new ArrayList<Status>();
                            for (int i = 0; i < favorites_list.size(); i++) {
                                if (!favorites_list.get(i).getId().equals(statusList.get(position).getId())) {
                                    new_favorites.add(favorites_list.get(i));

                                }
                            }
                            if (favorites == true) {
                                Log.v("DOWNLOADED", "favorites==true");
                                statusList.remove(position);
                                notifyDataSetChanged();
                                //holder.ripple_view_wallpaper_item.setVisibility(View.GONE);
                            }
                            storageFavorites.storeImage(new_favorites);
                            holder.image_view_fav_item_video.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_border));
                        }
                    }
                });
                final DownloadFileFromURL downloadFileFromURL = new DownloadFileFromURL();
                holder.image_view_share_item_video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mInterstitialAd.isLoaded()) {
                            if (check()) {
                                mInterstitialAd.show();
                                mInterstitialAd.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        super.onAdClosed();
                                        requestNewInterstitial();
//                                        if (!statusList.get(position).isDownloading()) {
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//                                                downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, SHARE_ID);
//                                            else
//                                                downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, SHARE_ID);
//                                        }
                                        /*createSharableLink(position, SHARE_ID);*/
                                        shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), SHARE_ID, statusList.get(position).getThumbnail(), statusList.get(position).getTitle());
                                    }
                                });
                            } else {

//                                if (!statusList.get(position).isDownloading()) {
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//                                        downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, SHARE_ID);
//                                    else
//                                        downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, SHARE_ID);
//                                }
                                /*createSharableLink(position, SHARE_ID);*/
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), SHARE_ID, statusList.get(position).getThumbnail(), statusList.get(position).getTitle());
                            }
                        } else {

//                            if (!statusList.get(position).isDownloading()) {
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//                                    downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, SHARE_ID);
//                                else
//                                    downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, SHARE_ID);
//                            }
                            /*createSharableLink(position, SHARE_ID);*/
                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), SHARE_ID, statusList.get(position).getThumbnail(), statusList.get(position).getTitle());
                        }
                    }
                });

                holder.image_view_whatsapp_item_video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mInterstitialAd.isLoaded()) {
                            if (check()) {
                                mInterstitialAd.show();
                                mInterstitialAd.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        super.onAdClosed();
                                        requestNewInterstitial();
                                        /*if (!statusList.get(position).isDownloading()) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                                downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                                            else
                                                downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                                        }*/
                                        shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), WHATSAPP_ID, statusList.get(position).getThumbnail(), statusList.get(position).getTitle());
                                    }
                                });
                            } else {
                                /*if (!statusList.get(position).isDownloading()) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                        downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                                    else
                                        downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                                }*/
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), WHATSAPP_ID, statusList.get(position).getThumbnail(), statusList.get(position).getTitle());
                            }
                        } else {
                            /*if (!statusList.get(position).isDownloading()) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                    downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                                else
                                    downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                            }*/
                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), WHATSAPP_ID, statusList.get(position).getThumbnail(), statusList.get(position).getTitle());
                        }
                    }

                });
                holder.image_view_delete_item_video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final DownloadStorage downloadStorage = new DownloadStorage(activity.getApplicationContext());

                        List<Status> downloadedListStatus = downloadStorage.loadImagesFavorites();
                        Boolean exist = false;
                        if (downloadedListStatus == null) {
                            downloadedListStatus = new ArrayList<>();
                        }
                        for (int i = 0; i < downloadedListStatus.size(); i++) {
                            if (downloadedListStatus.get(i).getId().equals(statusList.get(position).getId())) {
                                exist = true;
                            }
                        }
                        if (exist == true) {
                            String pathlocal = statusList.get(position).getLocal();
                            ArrayList<Status> new_dwonloads = new ArrayList<Status>();
                            for (int i = 0; i < downloadedListStatus.size(); i++) {
                                if (!downloadedListStatus.get(i).getId().equals(statusList.get(position).getId())) {
                                    new_dwonloads.add(downloadedListStatus.get(i));

                                }
                            }
                            if (downloads == true) {
                                statusList.remove(position);
                                notifyDataSetChanged();
                            }
                            downloadStorage.storeImage(new_dwonloads);
                            Uri imageUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", new File(pathlocal));
                            File file = new File(pathlocal);
                            if (file.exists()) {
                                file.delete();
                                activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                            }
                        }
                    }
                });
            }
            break;
            case 3: {
                final StatusAdapter.ImageHolder holder = (StatusAdapter.ImageHolder) holder_parent;
                peekAndPop.addLongClickView(holder.image_view_thumbnail_item_image, position);

                Picasso.with(activity.getApplicationContext()).load(statusList.get(position).getThumbnail()).error(R.drawable.bg_transparant).placeholder(R.drawable.bg_transparant).into(holder.image_view_thumbnail_item_image);
                Picasso.with(activity.getApplicationContext()).load(statusList.get(position).getUserimage()).error(R.drawable.profile).placeholder(R.drawable.profile).into(holder.circle_image_view_item_image_user);
                holder.text_view_downloads_item_image.setText(format(statusList.get(position).getDownloads()));
                holder.text_view_views_item_image.setText(format(statusList.get(position).getViews()));
                holder.text_view_created_item_image.setText(statusList.get(position).getCreated());
                holder.text_view_item_image_name_user.setText(statusList.get(position).getUser());
                holder.text_view_item_image_title.setText(statusList.get(position).getTitle());

                if (statusList.get(position).getReview())
                    holder.relative_layout_item_image_review.setVisibility(View.VISIBLE);
                else
                    holder.relative_layout_item_image_review.setVisibility(View.GONE);
                if (downloads) {
                    holder.image_view_delete_item_image.setVisibility(View.VISIBLE);
                    holder.image_view_fav_item_image.setVisibility(View.GONE);

                } else {
                    holder.image_view_delete_item_image.setVisibility(View.GONE);
                    holder.image_view_fav_item_image.setVisibility(View.VISIBLE);
                }
                if (!statusList.get(position).isDownloading()) {
                    holder.relative_layout_progress_item_image.setVisibility(View.GONE);
                } else {
                    holder.relative_layout_progress_item_image.setVisibility(View.VISIBLE);
                    holder.progress_bar_item_image.setProgress(statusList.get(position).getProgress());
                    holder.text_view_progress_item_image.setText("Downloading : " + statusList.get(position).getProgress() + " %");
                }
                holder.image_view_thumbnail_item_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, ImageActivity.class);
                        intent.putExtra("id", statusList.get(position).getId());
                        intent.putExtra("title", statusList.get(position).getTitle());
                        intent.putExtra("kind", statusList.get(position).getKind());
                        intent.putExtra("description", statusList.get(position).getDescription());
                        intent.putExtra("review", statusList.get(position).getReview());
                        intent.putExtra("comment", statusList.get(position).getComment());
                        intent.putExtra("comments", statusList.get(position).getComments());
                        intent.putExtra("downloads", statusList.get(position).getDownloads());
                        intent.putExtra("views", statusList.get(position).getViews());
                        intent.putExtra("font", statusList.get(position).getFont());

                        intent.putExtra("user", statusList.get(position).getUser());
                        intent.putExtra("local", statusList.get(position).getLocal());
                        intent.putExtra("userid", statusList.get(position).getUserid());
                        intent.putExtra("userimage", statusList.get(position).getUserimage());
                        intent.putExtra("thumbnail", statusList.get(position).getThumbnail());
                        intent.putExtra("original", statusList.get(position).getOriginal());
                        intent.putExtra("type", statusList.get(position).getType());
                        intent.putExtra("extension", statusList.get(position).getExtension());
                        intent.putExtra("color", statusList.get(position).getColor());
                        intent.putExtra("created", statusList.get(position).getCreated());
                        intent.putExtra("tags", statusList.get(position).getTags());
                        intent.putExtra("like", statusList.get(position).getLike());
                        intent.putExtra("love", statusList.get(position).getLove());
                        intent.putExtra("woow", statusList.get(position).getWoow());
                        intent.putExtra("angry", statusList.get(position).getAngry());
                        intent.putExtra("sad", statusList.get(position).getSad());
                        intent.putExtra("haha", statusList.get(position).getHaha());
                        intent.putExtra("local", statusList.get(position).getLocal());
                        intent.putExtra("position", position);
                        intent.putExtra("superLikeCount", statusList.get(position).getSuperLikeCount());

                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                    }
                });
                final FavoritesStorage storageFavorites = new FavoritesStorage(activity.getApplicationContext());
                List<Status> statuses = storageFavorites.loadImagesFavorites();
                Boolean exist = false;
                if (statuses == null) {
                    statuses = new ArrayList<>();
                }
                for (int i = 0; i < statuses.size(); i++) {
                    if (statuses.get(i).getId().equals(statusList.get(position).getId())) {
                        exist = true;
                    }
                }
                if (exist) {
                    holder.image_view_fav_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_black));
                } else {
                    holder.image_view_fav_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_border));
                }
                holder.image_view_superlike_item_image.setVisibility(View.GONE);
                holder.image_view_superlike_item_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PrefManager prefManager = new PrefManager(activity);
//                        Log.e("SuperLikeCount", "" + prefManager.getInt("SuperLikeCount"));
//
//                        Calendar now = Calendar.getInstance();
//                        now.add(Calendar.HOUR, 3);
//                        System.out.println(now.getTime());
//
//                        long milisecond = now.getTimeInMillis();
//                        Log.e("milisecond", "" + milisecond);
//                        prefManager.setString("LastSuperLikeTime", "" + milisecond);


//                        long LastSuperLikeTime = Long.parseLong(prefManager.getString("LastSuperLikeTime"));

//                        showDialogTimer();
                        if (prefManager.getInt("SuperLikeCount") == 5) {

                            showDialogTimer();

                        } else {

                            prefManager = new PrefManager(activity);
                            String SuperLikeIds = prefManager.getString("SuperLikeUserIds");
                            Date today = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            if (prefManager.getString("SuperLikeDate").equalsIgnoreCase(sdf.format(today))) {
                                if (SuperLikeIds.contains(statusList.get(position).getUserid().toString())) {
                                    Toasty.error(activity, "Already Superlike this user post", Toast.LENGTH_SHORT, true).show();
                                } else {
                                    showDialog(statusList.get(position).getId(), statusList.get(position).getUserid(), position);
                                }
                            } else {
                                prefManager.setInt("SuperLikeCount", 0);
                                prefManager.setString("SuperLikeUserIds", "");
                                prefManager.setString("SuperLikeDate", sdf.format(today));
//                            setSuperLikedata(position);
//                            AddSuperLikePoints(statusList.get(position).getId(), statusList.get(position).getUserid());
                                showDialog(statusList.get(position).getId(), statusList.get(position).getUserid(), position);
                            }
                        }


                    }
                });

                holder.image_view_fav_item_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Status> favorites_list = storageFavorites.loadImagesFavorites();
                        Boolean exist = false;
                        if (favorites_list == null) {
                            favorites_list = new ArrayList<>();
                        }
                        for (int i = 0; i < favorites_list.size(); i++) {
                            if (favorites_list.get(i).getId().equals(statusList.get(position).getId())) {
                                exist = true;
                            }
                        }
                        if (exist == false) {
                            ArrayList<Status> audios = new ArrayList<Status>();
                            for (int i = 0; i < favorites_list.size(); i++) {
                                audios.add(favorites_list.get(i));
                            }
                            audios.add(statusList.get(position));
                            storageFavorites.storeImage(audios);
                            holder.image_view_fav_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_black));
                        } else {
                            ArrayList<Status> new_favorites = new ArrayList<Status>();
                            for (int i = 0; i < favorites_list.size(); i++) {
                                if (!favorites_list.get(i).getId().equals(statusList.get(position).getId())) {
                                    new_favorites.add(favorites_list.get(i));

                                }
                            }
                            if (favorites == true) {
                                Log.v("DOWNLOADED", "favorites==true");
                                statusList.remove(position);
                                notifyDataSetChanged();
                                //holder.ripple_view_wallpaper_item.setVisibility(View.GONE);
                            }
                            storageFavorites.storeImage(new_favorites);
                            holder.image_view_fav_item_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_border));
                        }
                    }
                });
                final DownloadFileFromURL downloadFileFromURL = new DownloadFileFromURL();
                holder.image_view_share_item_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mInterstitialAd.isLoaded()) {
                            if (check()) {
                                mInterstitialAd.show();
                                mInterstitialAd.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        super.onAdClosed();
                                        requestNewInterstitial();
//                                        if (!statusList.get(position).isDownloading()) {
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//                                                downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, SHARE_ID);
//                                            else
//                                                downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, SHARE_ID);
//                                        }
                                        /*createSharableLink(position, SHARE_ID);*/
                                        shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), SHARE_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                                    }
                                });
                            } else {

//                                if (!statusList.get(position).isDownloading()) {
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//                                        downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, SHARE_ID);
//                                    else
//                                        downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, SHARE_ID);
//                                }
                                /*createSharableLink(position, SHARE_ID);*/
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), SHARE_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                            }
                        } else {

//                            if (!statusList.get(position).isDownloading()) {
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//                                    downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, SHARE_ID);
//                                else
//                                    downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, SHARE_ID);
//                            }
                            /*createSharableLink(position, SHARE_ID);*/
                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), SHARE_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                        }
                    }
                });

                holder.image_view_whatsapp_item_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mInterstitialAd.isLoaded()) {
                            if (check()) {
                                mInterstitialAd.show();
                                mInterstitialAd.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        super.onAdClosed();
                                        requestNewInterstitial();
                                        /*if (!statusList.get(position).isDownloading()) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                                downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                                            else
                                                downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                                        }*/
                                        shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), WHATSAPP_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                                    }
                                });
                            } else {
                                /*if (!statusList.get(position).isDownloading()) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                        downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                                    else
                                        downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                                }*/
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), WHATSAPP_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                            }
                        } else {
                            /*if (!statusList.get(position).isDownloading()) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                    downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                                else
                                    downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                            }*/
                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), WHATSAPP_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                        }
                    }

                });
                holder.image_view_delete_item_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final DownloadStorage downloadStorage = new DownloadStorage(activity.getApplicationContext());

                        List<Status> downloadedListStatus = downloadStorage.loadImagesFavorites();
                        Boolean exist = false;
                        if (downloadedListStatus == null) {
                            downloadedListStatus = new ArrayList<>();
                        }
                        for (int i = 0; i < downloadedListStatus.size(); i++) {
                            if (downloadedListStatus.get(i).getId().equals(statusList.get(position).getId())) {
                                exist = true;
                            }
                        }
                        if (exist == true) {
                            String pathlocal = statusList.get(position).getLocal();
                            ArrayList<Status> new_dwonloads = new ArrayList<Status>();
                            for (int i = 0; i < downloadedListStatus.size(); i++) {
                                if (!downloadedListStatus.get(i).getId().equals(statusList.get(position).getId())) {
                                    new_dwonloads.add(downloadedListStatus.get(i));

                                }
                            }
                            if (downloads == true) {
                                statusList.remove(position);
                                notifyDataSetChanged();
                            }
                            downloadStorage.storeImage(new_dwonloads);
                            Uri imageUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", new File(pathlocal));
                            File file = new File(pathlocal);
                            if (file.exists()) {
                                file.delete();
                                activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                            }
                        }
                    }
                });
            }
            break;
            case 4: {
                final StatusAdapter.GifHolder holder = (StatusAdapter.GifHolder) holder_parent;
                peekAndPop.addLongClickView(holder.image_view_thumbnail_item_gif, position);

                Picasso.with(activity.getApplicationContext()).load(statusList.get(position).getThumbnail()).error(R.drawable.bg_transparant).placeholder(R.drawable.bg_transparant).into(holder.image_view_thumbnail_item_gif);
                Picasso.with(activity.getApplicationContext()).load(statusList.get(position).getUserimage()).error(R.drawable.profile).placeholder(R.drawable.profile).into(holder.circle_image_view_item_gif_user);
                holder.text_view_downloads_item_gif.setText(format(statusList.get(position).getDownloads()));
                holder.text_view_views_item_gif.setText(format(statusList.get(position).getViews()));
                holder.text_view_created_item_gif.setText(statusList.get(position).getCreated());
                holder.text_view_item_gif_name_user.setText(statusList.get(position).getUser());
                holder.text_view_item_gif_title.setText(statusList.get(position).getTitle());

                if (statusList.get(position).getReview())
                    holder.relative_layout_item_gif_review.setVisibility(View.VISIBLE);
                else
                    holder.relative_layout_item_gif_review.setVisibility(View.GONE);
                if (downloads) {
                    holder.image_view_delete_item_gif.setVisibility(View.VISIBLE);
                    holder.image_view_fav_item_gif.setVisibility(View.GONE);

                } else {
                    holder.image_view_delete_item_gif.setVisibility(View.GONE);
                    holder.image_view_fav_item_gif.setVisibility(View.VISIBLE);
                }
                if (!statusList.get(position).isDownloading()) {
                    holder.relative_layout_progress_item_gif.setVisibility(View.GONE);
                } else {
                    holder.relative_layout_progress_item_gif.setVisibility(View.VISIBLE);
                    holder.progress_bar_item_gif.setProgress(statusList.get(position).getProgress());
                    holder.text_view_progress_item_gif.setText("Downloading : " + statusList.get(position).getProgress() + " %");
                }
                holder.image_view_superlike_item_image.setVisibility(View.GONE);

                holder.image_view_superlike_item_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        PrefManager prefManager = new PrefManager(activity);
                        String SuperLikeIds = prefManager.getString("SuperLikeUserIds");
                        Date today = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        if (prefManager.getString("SuperLikeDate").equalsIgnoreCase(sdf.format(today))) {
                            if (SuperLikeIds.contains(statusList.get(position).getUserid().toString())) {
                                Toasty.error(activity, "Already Superlike this user post", Toast.LENGTH_SHORT, true).show();
                            } else {
                                showDialog(statusList.get(position).getId(), statusList.get(position).getUserid(), position);
                            }
                        } else {
                            prefManager.setInt("SuperLikeCount", 0);
                            prefManager.setString("SuperLikeUserIds", "");
                            prefManager.setString("SuperLikeDate", sdf.format(today));
//                            setSuperLikedata(position);
//                            AddSuperLikePoints(statusList.get(position).getId(), statusList.get(position).getUserid());
                            showDialog(statusList.get(position).getId(), statusList.get(position).getUserid(), position);
                        }
                    }
                });
                holder.image_view_thumbnail_item_gif.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, GifActivity.class);
                        intent.putExtra("id", statusList.get(position).getId());
                        intent.putExtra("title", statusList.get(position).getTitle());
                        intent.putExtra("kind", statusList.get(position).getKind());
                        intent.putExtra("description", statusList.get(position).getDescription());
                        intent.putExtra("review", statusList.get(position).getReview());
                        intent.putExtra("comment", statusList.get(position).getComment());
                        intent.putExtra("comments", statusList.get(position).getComments());
                        intent.putExtra("downloads", statusList.get(position).getDownloads());
                        intent.putExtra("views", statusList.get(position).getViews());
                        intent.putExtra("font", statusList.get(position).getFont());

                        intent.putExtra("user", statusList.get(position).getUser());
                        intent.putExtra("userid", statusList.get(position).getUserid());
                        intent.putExtra("userimage", statusList.get(position).getUserimage());
                        intent.putExtra("thumbnail", statusList.get(position).getThumbnail());
                        intent.putExtra("original", statusList.get(position).getOriginal());
                        intent.putExtra("type", statusList.get(position).getType());
                        intent.putExtra("extension", statusList.get(position).getExtension());
                        intent.putExtra("color", statusList.get(position).getColor());
                        intent.putExtra("created", statusList.get(position).getCreated());
                        intent.putExtra("tags", statusList.get(position).getTags());
                        intent.putExtra("like", statusList.get(position).getLike());
                        intent.putExtra("love", statusList.get(position).getLove());
                        intent.putExtra("woow", statusList.get(position).getWoow());
                        intent.putExtra("angry", statusList.get(position).getAngry());
                        intent.putExtra("sad", statusList.get(position).getSad());
                        intent.putExtra("haha", statusList.get(position).getHaha());
                        intent.putExtra("local", statusList.get(position).getLocal());
                        intent.putExtra("position", position);
                        intent.putExtra("superLikeCount", statusList.get(position).getSuperLikeCount());

                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                    }
                });
                final FavoritesStorage storageFavorites = new FavoritesStorage(activity.getApplicationContext());
                List<Status> statuses = storageFavorites.loadImagesFavorites();
                Boolean exist = false;
                if (statuses == null) {
                    statuses = new ArrayList<>();
                }
                for (int i = 0; i < statuses.size(); i++) {
                    if (statuses.get(i).getId().equals(statusList.get(position).getId())) {
                        exist = true;
                    }
                }
                if (exist) {
                    holder.image_view_fav_item_gif.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_black));
                } else {
                    holder.image_view_fav_item_gif.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_border));
                }
                holder.image_view_fav_item_gif.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Status> favorites_list = storageFavorites.loadImagesFavorites();
                        Boolean exist = false;
                        if (favorites_list == null) {
                            favorites_list = new ArrayList<>();
                        }
                        for (int i = 0; i < favorites_list.size(); i++) {
                            if (favorites_list.get(i).getId().equals(statusList.get(position).getId())) {
                                exist = true;
                            }
                        }
                        if (exist == false) {
                            ArrayList<Status> audios = new ArrayList<Status>();
                            for (int i = 0; i < favorites_list.size(); i++) {
                                audios.add(favorites_list.get(i));
                            }
                            audios.add(statusList.get(position));
                            storageFavorites.storeImage(audios);
                            holder.image_view_fav_item_gif.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_black));
                        } else {
                            ArrayList<Status> new_favorites = new ArrayList<Status>();
                            for (int i = 0; i < favorites_list.size(); i++) {
                                if (!favorites_list.get(i).getId().equals(statusList.get(position).getId())) {
                                    new_favorites.add(favorites_list.get(i));

                                }
                            }
                            if (favorites == true) {
                                Log.v("DOWNLOADED", "favorites==true");
                                statusList.remove(position);
                                notifyDataSetChanged();
                                //holder.ripple_view_wallpaper_item.setVisibility(View.GONE);
                            }
                            storageFavorites.storeImage(new_favorites);
                            holder.image_view_fav_item_gif.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_border));
                        }
                    }
                });
                holder.image_view_share_item_gif.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mInterstitialAd.isLoaded()) {
                            if (check()) {
                                mInterstitialAd.show();
                                mInterstitialAd.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        super.onAdClosed();
                                        requestNewInterstitial();
//                                        shareTextWith(position, SHARE_ID);
                                        /*createSharableLink(position, SHARE_ID);*/
                                        shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), SHARE_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                                    }
                                });
                            } else {
//                                shareTextWith(position, SHARE_ID);
                                /*createSharableLink(position, SHARE_ID);*/
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), SHARE_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                            }
                        } else {
//                            shareTextWith(position, SHARE_ID);
                            /*createSharableLink(position, SHARE_ID);*/
                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), SHARE_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                        }
                    }
                });
                final DownloadFileFromURL downloadFileFromURL = new DownloadFileFromURL();

                holder.image_view_whatsapp_item_gif.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mInterstitialAd.isLoaded()) {
                            if (check()) {
                                mInterstitialAd.show();
                                mInterstitialAd.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        super.onAdClosed();
                                        requestNewInterstitial();
                                        /*if (!statusList.get(position).isDownloading()) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                                downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                                            else
                                                downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                                        }*/
                                        shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), WHATSAPP_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                                    }
                                });
                            } else {
                                /*if (!statusList.get(position).isDownloading()) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                        downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                                    else
                                        downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                                }*/
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), WHATSAPP_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                            }
                        } else {
                            /*if (!statusList.get(position).isDownloading()) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                    downloadFileFromURL.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                                else
                                    downloadFileFromURL.execute(statusList.get(position).getOriginal(), statusList.get(position).getTitle(), statusList.get(position).getExtension(), position, WHATSAPP_ID);
                            }*/
                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), WHATSAPP_ID, statusList.get(position).getOriginal(), statusList.get(position).getTitle());
                        }
                    }

                });
                holder.image_view_delete_item_gif.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final DownloadStorage downloadStorage = new DownloadStorage(activity.getApplicationContext());

                        List<Status> downloadedListStatus = downloadStorage.loadImagesFavorites();
                        Boolean exist = false;
                        if (downloadedListStatus == null) {
                            downloadedListStatus = new ArrayList<>();
                        }
                        for (int i = 0; i < downloadedListStatus.size(); i++) {
                            if (downloadedListStatus.get(i).getId().equals(statusList.get(position).getId())) {
                                exist = true;
                            }
                        }
                        if (exist == true) {
                            String pathlocal = statusList.get(position).getLocal();
                            ArrayList<Status> new_dwonloads = new ArrayList<Status>();
                            for (int i = 0; i < downloadedListStatus.size(); i++) {
                                if (!downloadedListStatus.get(i).getId().equals(statusList.get(position).getId())) {
                                    new_dwonloads.add(downloadedListStatus.get(i));

                                }
                            }
                            if (downloads == true) {
                                statusList.remove(position);
                                notifyDataSetChanged();
                            }
                            downloadStorage.storeImage(new_dwonloads);
                            Uri imageUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", new File(pathlocal));
                            File file = new File(pathlocal);
                            if (file.exists()) {
                                file.delete();
                                activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                            }
                        }
                    }
                });
            }
            break;
            case 5: {
                final StatusAdapter.QuoteHolder holder = (StatusAdapter.QuoteHolder) holder_parent;
                peekAndPop.addLongClickView(holder.relative_layout_background_item_quote, position);

                byte[] data = android.util.Base64.decode(statusList.get(position).getTitle(), android.util.Base64.DEFAULT);
                final String final_text = new String(data, Charset.forName("UTF-8"));

                holder.text_view_item_quote_status.setText(final_text);
                holder.relative_layout_background_item_quote.setBackgroundResource(R.drawable.bg_quote_background);
                Typeface tf = Typeface.createFromAsset(activity.getAssets(), "font_" + statusList.get(position).getFont() + ".ttf");
                holder.text_view_item_quote_status.setTypeface(tf);
                GradientDrawable drawable = (GradientDrawable) holder.relative_layout_background_item_quote.getBackground();
                drawable.setColor(Color.parseColor("#" + statusList.get(position).getColor()));
                Picasso.with(activity.getApplicationContext()).load(statusList.get(position).getUserimage()).error(R.drawable.profile).placeholder(R.drawable.profile).into(holder.circle_image_view_item_quote_user);
                holder.text_view_downloads_item_quote.setText(format(statusList.get(position).getDownloads()));
                holder.text_view_views_item_quote.setText(format(statusList.get(position).getViews()));
                holder.text_view_item_quote_name_user.setText(statusList.get(position).getUser());
                holder.text_view_item_quote_title.setText(statusList.get(position).getTitle());

                if (statusList.get(position).getReview())
                    holder.relative_layout_item_quote_review.setVisibility(View.VISIBLE);
                else
                    holder.relative_layout_item_quote_review.setVisibility(View.GONE);
                if (downloads) {
                    holder.image_view_delete_item_quote.setVisibility(View.VISIBLE);
                    holder.image_view_fav_item_quote.setVisibility(View.GONE);

                } else {
                    holder.image_view_delete_item_quote.setVisibility(View.GONE);
                    holder.image_view_fav_item_quote.setVisibility(View.VISIBLE);
                }
                if (!statusList.get(position).isDownloading()) {
                    holder.relative_layout_progress_item_quote.setVisibility(View.GONE);
                } else {
                    holder.relative_layout_progress_item_quote.setVisibility(View.VISIBLE);
                    holder.progress_bar_item_quote.setProgress(statusList.get(position).getProgress());
                    holder.text_view_progress_item_quote.setText("Downloading : " + statusList.get(position).getProgress() + " %");
                }
                holder.image_view_superlike_item_image.setVisibility(View.GONE);
                holder.image_view_superlike_item_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PrefManager prefManager = new PrefManager(activity);
                        String SuperLikeIds = prefManager.getString("SuperLikeUserIds");
                        Date today = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        if (prefManager.getString("SuperLikeDate").equalsIgnoreCase(sdf.format(today))) {
                            if (SuperLikeIds.contains(statusList.get(position).getUserid().toString())) {
                                Toasty.error(activity, "Already Superlike this user post", Toast.LENGTH_SHORT, true).show();
                            } else {
                                showDialog(statusList.get(position).getId(), statusList.get(position).getUserid(), position);
                            }
                        } else {
                            prefManager.setInt("SuperLikeCount", 0);
                            prefManager.setString("SuperLikeUserIds", "");
                            prefManager.setString("SuperLikeDate", sdf.format(today));
//                            setSuperLikedata(position);
//                            AddSuperLikePoints(statusList.get(position).getId(), statusList.get(position).getUserid());
                            showDialog(statusList.get(position).getId(), statusList.get(position).getUserid(), position);
                        }
                    }
                });
                holder.relative_layout_background_item_quote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, QuoteActivity.class);
                        intent.putExtra("id", statusList.get(position).getId());
                        intent.putExtra("title", statusList.get(position).getTitle());
                        intent.putExtra("kind", statusList.get(position).getKind());
                        intent.putExtra("description", statusList.get(position).getDescription());
                        intent.putExtra("review", statusList.get(position).getReview());
                        intent.putExtra("comment", statusList.get(position).getComment());
                        intent.putExtra("comments", statusList.get(position).getComments());
                        intent.putExtra("downloads", statusList.get(position).getDownloads());
                        intent.putExtra("views", statusList.get(position).getViews());
                        intent.putExtra("font", statusList.get(position).getFont());
                        intent.putExtra("user", statusList.get(position).getUser());
                        intent.putExtra("userid", statusList.get(position).getUserid());
                        intent.putExtra("userimage", statusList.get(position).getUserimage());
                        intent.putExtra("thumbnail", statusList.get(position).getThumbnail());
                        intent.putExtra("original", statusList.get(position).getOriginal());
                        intent.putExtra("type", statusList.get(position).getType());
                        intent.putExtra("extension", statusList.get(position).getExtension());
                        intent.putExtra("color", statusList.get(position).getColor());
                        intent.putExtra("created", statusList.get(position).getCreated());
                        intent.putExtra("tags", statusList.get(position).getTags());
                        intent.putExtra("like", statusList.get(position).getLike());
                        intent.putExtra("love", statusList.get(position).getLove());
                        intent.putExtra("woow", statusList.get(position).getWoow());
                        intent.putExtra("angry", statusList.get(position).getAngry());
                        intent.putExtra("sad", statusList.get(position).getSad());
                        intent.putExtra("haha", statusList.get(position).getHaha());
                        intent.putExtra("local", statusList.get(position).getLocal());
                        intent.putExtra("position", position);
                        intent.putExtra("superLikeCount", statusList.get(position).getSuperLikeCount());

                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                    }
                });
                final FavoritesStorage storageFavorites = new FavoritesStorage(activity.getApplicationContext());
                List<Status> statuses = storageFavorites.loadImagesFavorites();
                Boolean exist = false;
                if (statuses == null) {
                    statuses = new ArrayList<>();
                }
                for (int i = 0; i < statuses.size(); i++) {
                    if (statuses.get(i).getId().equals(statusList.get(position).getId())) {
                        exist = true;
                    }
                }
                if (exist) {
                    holder.image_view_fav_item_quote.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_black));
                } else {
                    holder.image_view_fav_item_quote.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_border));
                }
                holder.image_view_fav_item_quote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Status> favorites_list = storageFavorites.loadImagesFavorites();
                        Boolean exist = false;
                        if (favorites_list == null) {
                            favorites_list = new ArrayList<>();
                        }
                        for (int i = 0; i < favorites_list.size(); i++) {
                            if (favorites_list.get(i).getId().equals(statusList.get(position).getId())) {
                                exist = true;
                            }
                        }
                        if (exist == false) {
                            ArrayList<Status> audios = new ArrayList<Status>();
                            for (int i = 0; i < favorites_list.size(); i++) {
                                audios.add(favorites_list.get(i));
                            }
                            audios.add(statusList.get(position));
                            storageFavorites.storeImage(audios);
                            holder.image_view_fav_item_quote.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_black));
                        } else {
                            ArrayList<Status> new_favorites = new ArrayList<Status>();
                            for (int i = 0; i < favorites_list.size(); i++) {
                                if (!favorites_list.get(i).getId().equals(statusList.get(position).getId())) {
                                    new_favorites.add(favorites_list.get(i));

                                }
                            }
                            if (favorites == true) {
                                Log.v("DOWNLOADED", "favorites==true");
                                statusList.remove(position);
                                notifyDataSetChanged();
                                //holder.ripple_view_wallpaper_item.setVisibility(View.GONE);
                            }
                            storageFavorites.storeImage(new_favorites);
                            holder.image_view_fav_item_quote.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favorite_border));
                        }
                    }
                });
                final DownloadFileFromURL downloadFileFromURL = new DownloadFileFromURL();
                holder.image_view_share_item_quote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mInterstitialAd.isLoaded()) {
                            if (check()) {
                                mInterstitialAd.show();
                                mInterstitialAd.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        super.onAdClosed();
                                        requestNewInterstitial();
//                                        shareTextWith(position, SHARE_ID);
                                        /*createSharableLink(position, SHARE_ID);*/
                                        shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), SHARE_ID, null, statusList.get(position).getTitle());
                                    }
                                });
                            } else {

//                                shareTextWith(position, SHARE_ID);
                               /* createSharableLink(position, SHARE_ID);*/
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), SHARE_ID, null, statusList.get(position).getTitle());
                            }
                        } else {

//                            shareTextWith(position, SHARE_ID);
                           /* createSharableLink(position, SHARE_ID);*/
                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), SHARE_ID, null, statusList.get(position).getTitle());
                        }
                    }
                });

                holder.image_view_whatsapp_item_quote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mInterstitialAd.isLoaded()) {
                            if (check()) {
                                mInterstitialAd.show();
                                mInterstitialAd.setAdListener(new AdListener() {
                                    @Override
                                    public void onAdClosed() {
                                        super.onAdClosed();
                                        requestNewInterstitial();
                                        /*shareTextWith(position, WHATSAPP_ID);*/
                                        shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), WHATSAPP_ID, null, statusList.get(position).getTitle());

                                    }
                                });
                            } else {
                                /*shareTextWith(position, WHATSAPP_ID);*/
                                shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), WHATSAPP_ID, null, statusList.get(position).getTitle());

                            }
                        } else {
                            /*shareTextWith(position, WHATSAPP_ID);*/
                            shareUtils.shareStatus(statusList.get(position).getId(), statusList.get(position).getKind(), WHATSAPP_ID, null, statusList.get(position).getTitle());

                        }
                    }

                });
                holder.image_view_delete_item_quote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final DownloadStorage downloadStorage = new DownloadStorage(activity.getApplicationContext());

                        List<Status> downloadedListStatus = downloadStorage.loadImagesFavorites();
                        Boolean exist = false;
                        if (downloadedListStatus == null) {
                            downloadedListStatus = new ArrayList<>();
                        }
                        for (int i = 0; i < downloadedListStatus.size(); i++) {
                            if (downloadedListStatus.get(i).getId().equals(statusList.get(position).getId())) {
                                exist = true;
                            }
                        }
                        if (exist == true) {
                            String pathlocal = statusList.get(position).getLocal();
                            ArrayList<Status> new_dwonloads = new ArrayList<Status>();
                            for (int i = 0; i < downloadedListStatus.size(); i++) {
                                if (!downloadedListStatus.get(i).getId().equals(statusList.get(position).getId())) {
                                    new_dwonloads.add(downloadedListStatus.get(i));

                                }
                            }
                            if (downloads == true) {
                                statusList.remove(position);
                                notifyDataSetChanged();
                            }
                            downloadStorage.storeImage(new_dwonloads);
                            Uri imageUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", new File(pathlocal));
                            File file = new File(pathlocal);
                            if (file.exists()) {
                                file.delete();
                                activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                            }
                        }
                    }
                });
            }
            break;
            case 7: {

                final SubscriptionHolder holder = (SubscriptionHolder) holder_parent;
                this.linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                this.subscribeAdapter = new SubscribeAdapter(userList, activity);
                holder.recycle_view_follow_items.setHasFixedSize(true);
                holder.recycle_view_follow_items.setAdapter(subscribeAdapter);
                holder.recycle_view_follow_items.setLayoutManager(linearLayoutManager);
                subscribeAdapter.notifyDataSetChanged();
                break;
            }
            case 8: {
                final SlideHolder holder = (SlideHolder) holder_parent;

                slide_adapter = new SlideAdapter(activity, slideList);
                holder.view_pager_slide.setAdapter(slide_adapter);
                holder.view_pager_slide.setOffscreenPageLimit(1);

                holder.view_pager_slide.setClipToPadding(false);
                holder.view_pager_slide.setPageMargin(0);
                holder.view_pager_indicator.setupWithViewPager(holder.view_pager_slide);
                holder.view_pager_slide.setCurrentItem(slideList.size() / 2);
                slide_adapter.notifyDataSetChanged();
                break;
            }
            case 9: {

                final SearchUserListHolder holder = (SearchUserListHolder) holder_parent;
                this.linearLayoutManagerSearch = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                this.searchUserAdapter = new SearchUserAdapter(userList, activity);
                holder.recycle_view_users_items.setHasFixedSize(true);
                holder.recycle_view_users_items.setAdapter(searchUserAdapter);
                holder.recycle_view_users_items.setLayoutManager(linearLayoutManagerSearch);
                searchUserAdapter.notifyDataSetChanged();
                break;
            }
            case 10: {
                final PortraitVideoListHolder holder = (PortraitVideoListHolder) holder_parent;
                switch (type) {
                    case "home":
                        holder.text_view_item_full_screen_title.setText("Fullscreen videos");
                        break;
                    case "category":
                        holder.text_view_item_full_screen_title.setText(name + " Fullscreen videos");
                        break;
                    case "user":
                        holder.text_view_item_full_screen_title.setText(name + " Fullscreen videos");
                        break;
                    case "search":
                        holder.text_view_item_full_screen_title.setText(name + " Fullscreen videos");
                        break;
                    case "follow":
                        holder.text_view_item_full_screen_title.setText(name + " Fullscreen videos");
                        break;
                }
                holder.portraitVideoAdapter.notifyDataSetChanged();
                holder.relative_layout_show_all_portrait_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (type) {
                            case "home":
                                Intent intent = new Intent(activity, AllPortaitVideosActivity.class);
                                activity.startActivity(intent);
                                activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                                break;
                            case "category":
                                Intent intent1 = new Intent(activity, AllFullScreenCategoryActivity.class);
                                intent1.putExtra("id", id);
                                intent1.putExtra("title", name);
                                activity.startActivity(intent1);
                                activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                                break;
                            case "user":
                                Intent intent2 = new Intent(activity, AllFullScreenUserActivity.class);
                                intent2.putExtra("id", id);
                                intent2.putExtra("name", name);
                                intent2.putExtra("image", image);
                                activity.startActivity(intent2);
                                activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                                break;
                            case "search":
                                Intent intent3 = new Intent(activity, AllFullScreenSearchActivity.class);
                                intent3.putExtra("query", name);
                                activity.startActivity(intent3);
                                activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                                break;
                            case "follow":
                                Intent intent4 = new Intent(activity, AllFullScreenFollowActivity.class);
                                activity.startActivity(intent4);
                                activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                                break;
                        }

                    }
                });
            }
            break;
        }


    }

    public void setSuperLikedata(int position) {

        PrefManager prefManager = new PrefManager(activity);

        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR, 3);
        System.out.println(now.getTime());

        long milisecond = now.getTimeInMillis();
        Log.e("milisecond", "" + milisecond);
        prefManager.setString("LastSuperLikeTime", "" + milisecond);


        int SuperLikeCount = prefManager.getInt("SuperLikeCount") + 1;
        prefManager.setInt("SuperLikeCount", SuperLikeCount);
        Log.e("SuperLikeCount", "" + prefManager.getInt("SuperLikeCount"));

        prefManager.setString("SuperLikeUserIds", statusList.get(position).getUserid().toString());
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        prefManager.setString("SuperLikeDate", sdf.format(today));


    }

    private String formatMilliSecondsToTime(long milliseconds) {

        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : "
                + twoDigitString(seconds);
    }

    private String twoDigitString(long number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    public void showDialogTimer() {
        final Dialog dialog = new Dialog(activity,
                R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_rewardadtimer);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        activity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        final TextView textviewtimer = (TextView) dialog.findViewById(R.id.textviewtimer);
        TextView text_view_reward_ok = (TextView) dialog.findViewById(R.id.text_view_reward_ok);

        Calendar now = Calendar.getInstance();
        now = Calendar.getInstance();
        PrefManager prefManager = new PrefManager(activity);
        long LastSuperLikeTime = Long.parseLong(prefManager.getString("LastSuperLikeTime"));

        long difference = LastSuperLikeTime - now.getTimeInMillis();

        new CountDownTimer(difference, 1000) {

            public void onTick(long millisUntilFinished) {
                // mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);

                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;


                long elapsedHours = millisUntilFinished / hoursInMilli;
                millisUntilFinished = millisUntilFinished % hoursInMilli;

                long elapsedMinutes = millisUntilFinished / minutesInMilli;
                millisUntilFinished = millisUntilFinished % minutesInMilli;

                long elapsedSeconds = millisUntilFinished / secondsInMilli;


                String yy = String.format("%02d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);

                Log.e("TImerrrrr-11111111---", "" + yy);
                textviewtimer.setText("Ads will display after : " + yy);

            }

            public void onFinish() {
                //  mTextField.setText("done!");
            }
        }.start();


        text_view_reward_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                showRewardedVideo(SuperlikePostId, userid, position);
//                bp.subscribe(MainActivity.this, Global.SUBSCRIPTION_ID);
            }
        });
        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    dialog.dismiss();
                }
                return true;
            }
        });
        dialog.show();

    }

    public void showDialog(final Integer SuperlikePostId, final Integer userid, final Integer position) {
        final Dialog dialog = new Dialog(activity,
                R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_rewardads);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        activity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        TextView text_view_reward_ok = (TextView) dialog.findViewById(R.id.text_view_reward_ok);

        text_view_reward_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showRewardedVideo(SuperlikePostId, userid, position);
//                bp.subscribe(MainActivity.this, Global.SUBSCRIPTION_ID);
            }
        });
        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    dialog.dismiss();
                }
                return true;
            }
        });
        dialog.show();

    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (statusList.get(position).getKind() != null) {
            switch (statusList.get(position).getKind()) {
                case "video":
                    return 2;
                case "image":
                    return 3;
                case "gif":
                    return 4;
                case "quote":
                    return 5;
                case "fullscreen":
                    return 2;
            }
        }
        return statusList.get(position).getViewType();
    }

    public void addShare(final Integer position) {
        final PrefManager prefManager = new PrefManager(activity);
        Integer id_user = 0;
        String key_user = "";
        if (prefManager.getString("LOGGED").toString().equals("TRUE")) {
            id_user = Integer.parseInt(prefManager.getString("ID_USER"));
            key_user = prefManager.getString("TOKEN_USER");
        }
        if (!prefManager.getString(statusList.get(position).getId().toString() + "_share").equals("true")) {
            prefManager.setString(statusList.get(position).getId().toString() + "_share", "true");
            Retrofit retrofit = apiClient.getClient();
            apiRest service = retrofit.create(apiRest.class);
            Call<Integer> call = service.addShare(statusList.get(position).getId(), id_user, key_user);
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                    if (response.isSuccessful()) {
                        statusList.get(position).setDownloads(response.body());
                        notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {

                }
            });
        }
    }

    public void addView(final Integer position) {
        final PrefManager prefManager = new PrefManager(activity);
        Integer id_user = 0;
        String key_user = "";
        if (prefManager.getString("LOGGED").toString().equals("TRUE")) {
            id_user = Integer.parseInt(prefManager.getString("ID_USER"));
            key_user = prefManager.getString("TOKEN_USER");
        }
        if (!prefManager.getString(statusList.get(position).getId().toString() + "_view").equals("true")) {
            prefManager.setString(statusList.get(position).getId().toString() + "_view", "true");
            Retrofit retrofit = apiClient.getClient();
            apiRest service = retrofit.create(apiRest.class);
            String ishash = "";
            try {
                ishash = VideoActivity.sha1((statusList.get(position).getId() + 55463938) + "");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            Call<Integer> call = service.addView(ishash, id_user, key_user);
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                    if (response.isSuccessful()) {
                        statusList.get(position).setViews(response.body());
                        notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                }
            });
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    public boolean check() {
        PrefManager prf = new PrefManager(activity.getApplicationContext());
        if (!prf.getString("SUBSCRIBED").equals("FALSE")) {
            return false;
        }
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());

        if (prf.getString("LAST_DATE_ADS").equals("")) {
            prf.setString("LAST_DATE_ADS", strDate);
        } else {
            String toyBornTime = prf.getString("LAST_DATE_ADS");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                Date oldDate = dateFormat.parse(toyBornTime);
                System.out.println(oldDate);

                Date currentDate = new Date();

                long diff = currentDate.getTime() - oldDate.getTime();
                long seconds = diff / 1000;

                if (seconds > Integer.parseInt(activity.getResources().getString(R.string.AD_MOB_TIME))) {
                    prf.setString("LAST_DATE_ADS", strDate);
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void shareTextWith(Integer position, String APP_ID) {
        String text = null;
        try {
            byte[] data = Base64.decode(statusList.get(position).getTitle(), Base64.DEFAULT);
            text = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String shareBody = text;
        shareBody += " \n\n  " + activity.getResources().getString(R.string.download_more_from_link);

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        if (!SHARE_ID.equals(APP_ID)) {
            sharingIntent.setPackage(APP_ID);
        }
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        activity.startActivity(Intent.createChooser(sharingIntent, activity.getResources().getString(R.string.share_via)));
        addShare(position);
    }

    public void createSharableLink(final int position, final String APP_ID) {

        String link = "https://phovio.page.link/?statusid=" + statusList.get(position).getId() + "&kind=" + statusList.get(position).getKind();
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix("https://phovio.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.videos.phovio")
                                .setMinimumVersion(0)
                                .build())
                .buildShortDynamicLink()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("onFailure", e.toString());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                    @Override
                    public void onSuccess(ShortDynamicLink shortDynamicLink) {
                        Uri mInvitationUrl = shortDynamicLink.getShortLink();
                        String invitationLink = mInvitationUrl.toString();
                        shareLinkWith(invitationLink, position, APP_ID);
                    }
                });
    }

    public void shareLinkWith(String invitationLink, int position, String APP_ID) {
        String text = null;
        try {
            byte[] data = Base64.decode(statusList.get(position).getTitle(), Base64.DEFAULT);
            text = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }  catch (Exception e) {
            text = "";
            e.printStackTrace();

        }

        String shareBody = text;
        shareBody += " \n\n  " + invitationLink;

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        if (!SHARE_ID.equals(APP_ID)) {
            sharingIntent.setPackage(APP_ID);
        }
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        activity.startActivity(Intent.createChooser(sharingIntent, activity.getResources().getString(R.string.share_via)));
        addShare(position);
    }

    /**
     * Populates a {@link UnifiedNativeAdView} object with data from a given
     * {@link UnifiedNativeAd}.
     *
     * @param nativeAd the object containing the ad's assets
     * @param adView   the view to be populated
     */
    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        com.google.android.gms.ads.formats.MediaView mediaView = adView.findViewById(R.id.ad_media);

        mediaView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                if (child instanceof ImageView) {
                    ImageView imageView = (ImageView) child;
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
            }
        });
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {
            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    super.onVideoEnd();
                }
            });
        } else {

        }
    }

    public static class ImageHolder extends RecyclerView.ViewHolder {


        private final RelativeLayout relative_layout_item_image_review;
        private final RelativeLayout relative_layout_item_image;
        private final ImageView image_view_delete_item_image;
        private final ImageView image_view_fav_item_image;
        private final ImageView image_view_share_item_image;
        private final ImageView image_view_whatsapp_item_image;
        private final ImageView image_view_icon_item_image;
        private final ImageView image_view_superlike_item_image;
        private final TextView text_view_progress_item_image;
        private final RelativeLayout relative_layout_progress_item_image;
        private final ProgressBar progress_bar_item_image;
        private final CircleImageView circle_image_view_item_image_user;
        private final TextView text_view_item_image_name_user;
        private final TextView text_view_item_image_title;
        private final ImageView image_view_thumbnail_item_image;
        private final TextView text_view_downloads_item_image;
        private final TextView text_view_views_item_image;
        private final TextView text_view_created_item_image;

        public ImageHolder(View itemView) {
            super(itemView);
            this.relative_layout_item_image_review = (RelativeLayout) itemView.findViewById(R.id.relative_layout_item_image_review);
            this.relative_layout_item_image = (RelativeLayout) itemView.findViewById(R.id.relative_layout_item_image);
            this.image_view_delete_item_image = (ImageView) itemView.findViewById(R.id.image_view_delete_item_image);
            this.image_view_fav_item_image = (ImageView) itemView.findViewById(R.id.image_view_fav_item_image);
            this.image_view_share_item_image = (ImageView) itemView.findViewById(R.id.image_view_share_item_image);
            this.image_view_whatsapp_item_image = (ImageView) itemView.findViewById(R.id.image_view_whatsapp_item_image);
            this.image_view_superlike_item_image = (ImageView) itemView.findViewById(R.id.image_view_superlike_item_image);
            this.image_view_icon_item_image = (ImageView) itemView.findViewById(R.id.image_view_icon_item_image);
            this.text_view_progress_item_image = (TextView) itemView.findViewById(R.id.text_view_progress_item_image);
            this.relative_layout_progress_item_image = (RelativeLayout) itemView.findViewById(R.id.relative_layout_progress_item_image);
            this.progress_bar_item_image = (ProgressBar) itemView.findViewById(R.id.progress_bar_item_image);
            this.circle_image_view_item_image_user = (CircleImageView) itemView.findViewById(R.id.circle_image_view_item_image_user);
            this.text_view_item_image_name_user = (TextView) itemView.findViewById(R.id.text_view_item_image_name_user);
            this.text_view_item_image_title = (TextView) itemView.findViewById(R.id.text_view_item_image_title);
            this.image_view_thumbnail_item_image = (ImageView) itemView.findViewById(R.id.image_view_thumbnail_item_image);
            this.text_view_downloads_item_image = (TextView) itemView.findViewById(R.id.text_view_downloads_item_image);
            this.text_view_created_item_image = (TextView) itemView.findViewById(R.id.text_view_created_item_image);
            this.text_view_views_item_image = (TextView) itemView.findViewById(R.id.text_view_views_item_image);
        }
    }

    public static class GifHolder extends RecyclerView.ViewHolder {


        private final RelativeLayout relative_layout_item_gif_review;
        private final RelativeLayout relative_layout_item_gif;
        private final ImageView image_view_delete_item_gif;
        private final ImageView image_view_fav_item_gif;
        private final ImageView image_view_share_item_gif;
        private final ImageView image_view_whatsapp_item_gif;
        private final ImageView image_view_icon_item_gif;
        private final TextView text_view_progress_item_gif;
        private final RelativeLayout relative_layout_progress_item_gif;
        private final ProgressBar progress_bar_item_gif;
        private final CircleImageView circle_image_view_item_gif_user;
        private final TextView text_view_item_gif_name_user;
        private final TextView text_view_item_gif_title;
        private final ImageView image_view_thumbnail_item_gif;
        private final TextView text_view_downloads_item_gif;
        private final TextView text_view_views_item_gif;
        private final TextView text_view_created_item_gif;
        private final ImageView image_view_superlike_item_image;

        public GifHolder(View itemView) {
            super(itemView);
            this.relative_layout_item_gif_review = (RelativeLayout) itemView.findViewById(R.id.relative_layout_item_gif_review);
            this.relative_layout_item_gif = (RelativeLayout) itemView.findViewById(R.id.relative_layout_item_gif);
            this.image_view_delete_item_gif = (ImageView) itemView.findViewById(R.id.image_view_delete_item_gif);
            this.image_view_fav_item_gif = (ImageView) itemView.findViewById(R.id.image_view_fav_item_gif);
            this.image_view_share_item_gif = (ImageView) itemView.findViewById(R.id.image_view_share_item_gif);
            this.image_view_whatsapp_item_gif = (ImageView) itemView.findViewById(R.id.image_view_whatsapp_item_gif);
            this.image_view_icon_item_gif = (ImageView) itemView.findViewById(R.id.image_view_icon_item_gif);
            this.text_view_progress_item_gif = (TextView) itemView.findViewById(R.id.text_view_progress_item_gif);
            this.relative_layout_progress_item_gif = (RelativeLayout) itemView.findViewById(R.id.relative_layout_progress_item_gif);
            this.progress_bar_item_gif = (ProgressBar) itemView.findViewById(R.id.progress_bar_item_gif);
            this.circle_image_view_item_gif_user = (CircleImageView) itemView.findViewById(R.id.circle_image_view_item_gif_user);
            this.text_view_item_gif_name_user = (TextView) itemView.findViewById(R.id.text_view_item_gif_name_user);
            this.text_view_item_gif_title = (TextView) itemView.findViewById(R.id.text_view_item_gif_title);
            this.image_view_thumbnail_item_gif = (ImageView) itemView.findViewById(R.id.image_view_thumbnail_item_gif);
            this.image_view_superlike_item_image = (ImageView) itemView.findViewById(R.id.image_view_superlike_item_image);
            this.text_view_downloads_item_gif = (TextView) itemView.findViewById(R.id.text_view_downloads_item_gif);
            this.text_view_created_item_gif = (TextView) itemView.findViewById(R.id.text_view_created_item_gif);
            this.text_view_views_item_gif = (TextView) itemView.findViewById(R.id.text_view_views_item_gif);
        }
    }

    public static class QuoteHolder extends RecyclerView.ViewHolder {

        private final ImageView image_view_superlike_item_image;

        private final RelativeLayout relative_layout_item_quote_review;
        private final RelativeLayout relative_layout_item_quote;
        private final ImageView image_view_delete_item_quote;
        private final ImageView image_view_fav_item_quote;
        private final ImageView image_view_share_item_quote;
        private final ImageView image_view_whatsapp_item_quote;
        private final ImageView image_view_icon_item_quote;
        private final TextView text_view_progress_item_quote;
        private final RelativeLayout relative_layout_progress_item_quote;
        private final ProgressBar progress_bar_item_quote;
        private final CircleImageView circle_image_view_item_quote_user;
        private final TextView text_view_item_quote_name_user;
        private final TextView text_view_item_quote_title;
        private final TextView text_view_downloads_item_quote;
        private final TextView text_view_views_item_quote;
        private final TextView text_view_item_quote_status;
        private final RelativeLayout relative_layout_background_item_quote;

        public QuoteHolder(View itemView) {
            super(itemView);
            this.relative_layout_item_quote_review = (RelativeLayout) itemView.findViewById(R.id.relative_layout_item_quote_review);
            this.relative_layout_item_quote = (RelativeLayout) itemView.findViewById(R.id.relative_layout_item_quote);
            this.image_view_delete_item_quote = (ImageView) itemView.findViewById(R.id.image_view_delete_item_quote);
            this.image_view_fav_item_quote = (ImageView) itemView.findViewById(R.id.image_view_fav_item_quote);
            this.image_view_share_item_quote = (ImageView) itemView.findViewById(R.id.image_view_share_item_quote);
            this.image_view_whatsapp_item_quote = (ImageView) itemView.findViewById(R.id.image_view_whatsapp_item_quote);
            this.image_view_superlike_item_image = (ImageView) itemView.findViewById(R.id.image_view_superlike_item_image);
            this.image_view_icon_item_quote = (ImageView) itemView.findViewById(R.id.image_view_icon_item_quote);
            this.text_view_progress_item_quote = (TextView) itemView.findViewById(R.id.text_view_progress_item_quote);
            this.relative_layout_progress_item_quote = (RelativeLayout) itemView.findViewById(R.id.relative_layout_progress_item_quote);
            this.progress_bar_item_quote = (ProgressBar) itemView.findViewById(R.id.progress_bar_item_quote);
            this.circle_image_view_item_quote_user = (CircleImageView) itemView.findViewById(R.id.circle_image_view_item_quote_user);
            this.text_view_item_quote_name_user = (TextView) itemView.findViewById(R.id.text_view_item_quote_name_user);
            this.text_view_item_quote_title = (TextView) itemView.findViewById(R.id.text_view_item_quote_title);
            this.relative_layout_background_item_quote = (RelativeLayout) itemView.findViewById(R.id.relative_layout_background_item_quote);
            this.text_view_item_quote_status = (TextView) itemView.findViewById(R.id.text_view_item_quote_status);
            this.text_view_downloads_item_quote = (TextView) itemView.findViewById(R.id.text_view_downloads_item_quote);
            this.text_view_views_item_quote = (TextView) itemView.findViewById(R.id.text_view_views_item_quote);
        }
    }

    public static class VideoHolder extends RecyclerView.ViewHolder {


        private final RelativeLayout relative_layout_item_video_review;
        private final RelativeLayout relative_layout_item_video;
        private final ImageView image_view_delete_item_video;
        private final ImageView image_view_fav_item_video;
        private final ImageView image_view_share_item_video;
        private final ImageView image_view_whatsapp_item_video;
        private final ImageView image_view_icon_item_video;
        private final ImageView image_view_superlike_item_image;
        private final TextView text_view_progress_item_video;
        private final RelativeLayout relative_layout_progress_item_video;
        private final ProgressBar progress_bar_item_video;
        private final CircleImageView circle_image_view_item_video_user;
        private final TextView text_view_item_video_name_user;
        private final TextView text_view_item_video_title;
        private final ImageView image_view_thumbnail_item_video;
        private final TextView text_view_downloads_item_video;
        private final TextView text_view_views_item_video;
        private final TextView text_view_created_item_video;

        public VideoHolder(View itemView) {
            super(itemView);
            this.relative_layout_item_video_review = (RelativeLayout) itemView.findViewById(R.id.relative_layout_item_video_review);
            this.relative_layout_item_video = (RelativeLayout) itemView.findViewById(R.id.relative_layout_item_video);
            this.image_view_delete_item_video = (ImageView) itemView.findViewById(R.id.image_view_delete_item_video);
            this.image_view_fav_item_video = (ImageView) itemView.findViewById(R.id.image_view_fav_item_video);
            this.image_view_share_item_video = (ImageView) itemView.findViewById(R.id.image_view_share_item_video);
            this.image_view_whatsapp_item_video = (ImageView) itemView.findViewById(R.id.image_view_whatsapp_item_video);
            this.image_view_icon_item_video = (ImageView) itemView.findViewById(R.id.image_view_icon_item_video);
            this.image_view_superlike_item_image = (ImageView) itemView.findViewById(R.id.image_view_superlike_item_image);
            this.text_view_progress_item_video = (TextView) itemView.findViewById(R.id.text_view_progress_item_video);
            this.relative_layout_progress_item_video = (RelativeLayout) itemView.findViewById(R.id.relative_layout_progress_item_video);
            this.progress_bar_item_video = (ProgressBar) itemView.findViewById(R.id.progress_bar_item_video);
            this.circle_image_view_item_video_user = (CircleImageView) itemView.findViewById(R.id.circle_image_view_item_video_user);
            this.text_view_item_video_name_user = (TextView) itemView.findViewById(R.id.text_view_item_video_name_user);
            this.text_view_item_video_title = (TextView) itemView.findViewById(R.id.text_view_item_video_title);
            this.image_view_thumbnail_item_video = (ImageView) itemView.findViewById(R.id.image_view_thumbnail_item_video);
            this.text_view_downloads_item_video = (TextView) itemView.findViewById(R.id.text_view_downloads_item_video);
            this.text_view_created_item_video = (TextView) itemView.findViewById(R.id.text_view_created_item_video);
            this.text_view_views_item_video = (TextView) itemView.findViewById(R.id.text_view_views_item_video);
        }
    }

    public static class SubscriptionHolder extends RecyclerView.ViewHolder {
        private final RecyclerView recycle_view_follow_items;

        public SubscriptionHolder(View view) {
            super(view);
            recycle_view_follow_items = (RecyclerView) itemView.findViewById(R.id.recycle_view_follow_items);
        }
    }

    public static class SearchUserListHolder extends RecyclerView.ViewHolder {
        private final RecyclerView recycle_view_users_items;

        public SearchUserListHolder(View view) {
            super(view);
            recycle_view_users_items = (RecyclerView) itemView.findViewById(R.id.recycle_view_users_items);
        }
    }

    public class CategoriesHolder extends RecyclerView.ViewHolder {
        private final LinearLayoutManager linearLayoutManager;
        private final CategoryAdapter categoryVideoAdapter;
        public RecyclerView recycler_view_item_categories;

        public CategoriesHolder(View view) {
            super(view);
            this.recycler_view_item_categories = (RecyclerView) itemView.findViewById(R.id.recycler_view_item_categories);
            this.linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
            this.categoryVideoAdapter = new CategoryAdapter(categoryList, activity);
            recycler_view_item_categories.setHasFixedSize(true);
            recycler_view_item_categories.setAdapter(categoryVideoAdapter);
            recycler_view_item_categories.setLayoutManager(linearLayoutManager);
        }
    }

    public class EmptyHolder extends RecyclerView.ViewHolder {


        public EmptyHolder(View view) {
            super(view);

        }
    }

    /**
     * Background Async Task to download file
     */
    class DownloadFileFromURL extends AsyncTask<Object, String, String> {

        private int position;
        private String old = "-100";
        private boolean runing = true;
        private String share_app;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        public boolean dir_exists(String dir_path) {
            boolean ret = false;
            File dir = new File(dir_path);
            if (dir.exists() && dir.isDirectory())
                ret = true;
            return ret;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            runing = false;
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(Object... f_url) {
            int count;
            try {
                URL url = new URL((String) f_url[0]);
                String title = (String) f_url[1];
                String extension = (String) f_url[2];
                this.position = (int) f_url[3];
                this.share_app = (String) f_url[4];
                String id = statusList.get(position).getId().toString();


                statusList.get(position).setDownloading(true);
                URLConnection conection = url.openConnection();
                conection.setRequestProperty("Accept-Encoding", "identity");

                conection.connect();
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);


                String dir_path = Environment.getExternalStorageDirectory().toString() + "/StatusVideos/";

                if (!dir_exists(dir_path)) {
                    File directory = new File(dir_path);
                    if (directory.mkdirs()) {
                        Log.v("dir", "is created 1");
                    } else {
                        Log.v("dir", "not created 1");

                    }
                    if (directory.mkdir()) {
                        Log.v("dir", "is created 2");
                    } else {
                        Log.v("dir", "not created 2");

                    }
                } else {
                    Log.v("dir", "is exist");
                }
                File file = new File(dir_path + title.toString().replace("/", "_") + "_" + id + "." + extension);
                if (!file.exists()) {
                    // Output stream
                    OutputStream output = new FileOutputStream(dir_path + title.toString().replace("/", "_") + "_" + id + "." + extension);

                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                        // writing data to file
                        output.write(data, 0, count);
                        if (!runing) {
                            Log.v("v", "not rurning");
                        }
                    }

                    output.flush();


                    output.close();
                    input.close();
                    MediaScannerConnection.scanFile(activity.getApplicationContext(), new String[]{dir_path + title.toString().replace("/", "_") + "_" + id + "." + extension},
                            null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {

                                }
                            });
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        final Uri contentUri = Uri.fromFile(new File(dir_path + title.toString().replace("/", "_") + "_" + id + "." + extension));
                        scanIntent.setData(contentUri);
                        activity.sendBroadcast(scanIntent);
                    } else {
                        final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
                        activity.sendBroadcast(intent);
                    }
                }
                statusList.get(position).setPath(dir_path + title.toString().replace("/", "_") + "_" + id + "." + extension);
            } catch (Exception e) {
                Log.v("ex", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            try {
                if (!progress[0].equals(old)) {
                    statusList.get(position).setProgress(Integer.valueOf(progress[0]));
                    notifyDataSetChanged();
                    old = progress[0];
                    Log.v("download", progress[0] + "%");
                    statusList.get(position).setDownloading(true);
                    statusList.get(position).setProgress(Integer.parseInt(progress[0]));
                }
            } catch (Exception e) {

            }

        }

        public void AddDownloadLocal(Integer position) {
            final DownloadStorage downloadStorage = new DownloadStorage(activity.getApplicationContext());
            List<com.videos.phovio.model.Status> download_list = downloadStorage.loadImagesFavorites();
            Boolean exist = false;
            if (download_list == null) {
                download_list = new ArrayList<>();
            }
            for (int i = 0; i < download_list.size(); i++) {
                if (download_list.get(i).getId().equals(statusList.get(position).getId())) {
                    exist = true;
                }
            }
            if (exist == false) {
                ArrayList<com.videos.phovio.model.Status> audios = new ArrayList<com.videos.phovio.model.Status>();
                for (int i = 0; i < download_list.size(); i++) {
                    audios.add(download_list.get(i));
                }
                com.videos.phovio.model.Status videodownloaded = statusList.get(position);

                videodownloaded.setLocal(statusList.get(position).getPath());

                audios.add(videodownloaded);
                downloadStorage.storeImage(audios);
            }
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {

            statusList.get(position).setDownloading(false);
            if (statusList.get(position).getPath() == null) {
                if (downloads) {
                    switch (share_app) {
                        case WHATSAPP_ID:
                            shareWhatsapp(position);
                            break;
                        case FACEBOOK_ID:
                            shareFacebook(position);
                            break;
                        case MESSENGER_ID:
                            shareMessenger(position);
                            break;
                        case INSTAGRAM_ID:
                            shareInstagram(position);
                            break;
                        case SHARE_ID:
                            share(position);
                            break;
                    }
                } else {
                    Toasty.error(activity.getApplicationContext(), activity.getString(R.string.download_failed), Toast.LENGTH_SHORT, true).show();
                }
            } else {
                addShare(position);
                AddDownloadLocal(position);
                switch (share_app) {
                    case WHATSAPP_ID:
                        shareWhatsapp(position);
                        break;
                    case FACEBOOK_ID:
                        shareFacebook(position);
                        break;
                    case MESSENGER_ID:
                        shareMessenger(position);
                        break;
                    case INSTAGRAM_ID:
                        shareInstagram(position);
                        break;
                    case SHARE_ID:
                        share(position);
                        break;
                }
            }
            notifyDataSetChanged();
        }

        public void shareWhatsapp(Integer position) {
            String path = statusList.get(position).getPath();
            if (statusList.get(position).getLocal() != null) {
                if (new File(statusList.get(position).getLocal()).exists()) {
                    path = statusList.get(position).getLocal();
                }
            }
            Uri imageUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", new File(path));
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage(WHATSAPP_ID);


            final String final_text = activity.getResources().getString(R.string.download_more_from_link);

            shareIntent.putExtra(Intent.EXTRA_TEXT, final_text);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);


            shareIntent.setType(statusList.get(position).getType());
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                activity.startActivity(shareIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toasty.error(activity.getApplicationContext(), activity.getResources().getString(R.string.whatsapp_not_installed), Toast.LENGTH_SHORT, true).show();
            }
        }

        public void shareFacebook(Integer position) {
            String path = statusList.get(position).getPath();

            Uri imageUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", new File(path));
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage(FACEBOOK_ID);


            final String final_text = activity.getResources().getString(R.string.download_more_from_link);

            shareIntent.putExtra(Intent.EXTRA_TEXT, final_text);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

            shareIntent.setType(statusList.get(position).getType());
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                activity.startActivity(shareIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toasty.error(activity.getApplicationContext(), activity.getResources().getString(R.string.facebook_not_installed), Toast.LENGTH_SHORT, true).show();
            }
        }

        public void shareMessenger(Integer position) {
            String path = statusList.get(position).getPath();

            Uri imageUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", new File(path));
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage(MESSENGER_ID);

            final String final_text = activity.getResources().getString(R.string.download_more_from_link);

            shareIntent.putExtra(Intent.EXTRA_TEXT, final_text);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

            shareIntent.setType(statusList.get(position).getType());
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                activity.startActivity(shareIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toasty.error(activity.getApplicationContext(), activity.getResources().getString(R.string.messenger_not_installed), Toast.LENGTH_SHORT, true).show();
            }
        }

        public void shareInstagram(Integer position) {
            String path = statusList.get(position).getPath();

            Uri imageUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", new File(path));
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage(INSTAGRAM_ID);


            final String final_text = activity.getResources().getString(R.string.download_more_from_link);

            shareIntent.putExtra(Intent.EXTRA_TEXT, final_text);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

            shareIntent.setType(statusList.get(position).getType());
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                activity.startActivity(shareIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toasty.error(activity.getApplicationContext(), activity.getResources().getString(R.string.instagram_not_installed), Toast.LENGTH_SHORT, true).show();
            }
        }

        public void share(Integer position) {
            String path = statusList.get(position).getPath();

            Uri imageUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", new File(path));
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);


            final String final_text = activity.getResources().getString(R.string.download_more_from_link);
            shareIntent.putExtra(Intent.EXTRA_TEXT, final_text);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

            shareIntent.setType(statusList.get(position).getType());
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                activity.startActivity(Intent.createChooser(shareIntent, "Shared via " + activity.getResources().getString(R.string.app_name)));
            } catch (android.content.ActivityNotFoundException ex) {
                Toasty.error(activity.getApplicationContext(), activity.getResources().getString(R.string.app_not_installed), Toast.LENGTH_SHORT, true).show();
            }
        }

    }

    public class FacebookNativeHolder extends RecyclerView.ViewHolder {
        private final String TAG = "WALLPAPERADAPTER";
        private LinearLayout nativeAdContainer;
        private LinearLayout adView;
        private NativeAd nativeAd;

        public FacebookNativeHolder(View view) {
            super(view);
            loadNativeAd(view);
        }

        private void loadNativeAd(final View view) {
            // Instantiate a NativeAd object.
            // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
            // now, while you are testing and replace it later when you have signed up.
            // While you are using this temporary code you will only get test ads and if you release
            // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
            nativeAd = new NativeAd(activity, activity.getString(R.string.FACEBOOK_ADS_NATIVE_PLACEMENT_ID));
            nativeAd.setAdListener(new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {
                    // Native ad finished downloading all assets
                    Log.e(TAG, "Native ad finished downloading all assets.");
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    // Native ad failed to load
                    Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Native ad is loaded and ready to be displayed
                    Log.d(TAG, "Native ad is loaded and ready to be displayed!");
                    // Race condition, load() called again before last ad was displayed
                    if (nativeAd == null || nativeAd != ad) {
                        return;
                    }
                   /* NativeAdViewAttributes viewAttributes = new NativeAdViewAttributes()
                            .setBackgroundColor(activity.getResources().getColor(R.color.colorPrimaryDark))
                            .setTitleTextColor(Color.WHITE)
                            .setDescriptionTextColor(Color.WHITE)
                            .setButtonColor(Color.WHITE);

                    View adView = NativeAdView.render(activity, nativeAd, NativeAdView.Type.HEIGHT_300, viewAttributes);

                    LinearLayout nativeAdContainer = (LinearLayout) view.findViewById(R.id.native_ad_container);
                    nativeAdContainer.addView(adView);*/
                    // Inflate Native Ad into Container
                    inflateAd(nativeAd, view);
                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Native ad clicked
                    Log.d(TAG, "Native ad clicked!");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Native ad impression
                    Log.d(TAG, "Native ad impression logged!");
                }
            });

            // Request an ad
            nativeAd.loadAd();
        }

        private void inflateAd(NativeAd nativeAd, View view) {

            nativeAd.unregisterView();

            // Add the Ad view into the ad container.
            nativeAdContainer = view.findViewById(R.id.native_ad_container);
            LayoutInflater inflater = LayoutInflater.from(activity);
            // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
            adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout_1, nativeAdContainer, false);
            nativeAdContainer.addView(adView);

            // Add the AdChoices icon
            LinearLayout adChoicesContainer = view.findViewById(R.id.ad_choices_container);
            AdChoicesView adChoicesView = new AdChoicesView(activity, nativeAd, true);
            adChoicesContainer.addView(adChoicesView, 0);

            // Create native UI using the ad metadata.
            AdIconView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
            TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
            MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
            TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
            TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
            TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
            Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

            // Set the Text.
            nativeAdTitle.setText(nativeAd.getAdvertiserName());
            nativeAdBody.setText(nativeAd.getAdBodyText());
            nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
            nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
            nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
            sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

            // Create a list of clickable views
            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(nativeAdTitle);
            clickableViews.add(nativeAdCallToAction);

            // Register the Title and CTA button to listen for clicks.
            nativeAd.registerViewForInteraction(
                    adView,
                    nativeAdMedia,
                    nativeAdIcon,
                    clickableViews);
        }

    }

    private class SlideHolder extends RecyclerView.ViewHolder {
        private final ViewPagerIndicator view_pager_indicator;
        private final ClickableViewPager view_pager_slide;

        public SlideHolder(View itemView) {
            super(itemView);
            this.view_pager_indicator = (ViewPagerIndicator) itemView.findViewById(R.id.view_pager_indicator);
            this.view_pager_slide = (ClickableViewPager) itemView.findViewById(R.id.view_pager_slide);
        }

    }

    private class PortraitVideoListHolder extends RecyclerView.ViewHolder {
        private final LinearLayoutManager linearLayoutManager;
        private final PortraitVideoAdapter portraitVideoAdapter;
        private final RelativeLayout relative_layout_show_all_portrait_all;
        private final TextView text_view_item_full_screen_title;
        public RecyclerView recycler_view_item_portrait_list;

        public PortraitVideoListHolder(View itemView) {
            super(itemView);
            this.text_view_item_full_screen_title = (TextView) itemView.findViewById(R.id.text_view_item_full_screen_title);
            this.relative_layout_show_all_portrait_all = (RelativeLayout) itemView.findViewById(R.id.relative_layout_show_all_portrait_all);
            this.recycler_view_item_portrait_list = (RecyclerView) itemView.findViewById(R.id.recycler_view_item_portrait_list);
            this.linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
            this.portraitVideoAdapter = new PortraitVideoAdapter(fullScreenVdeos, activity);
            recycler_view_item_portrait_list.setHasFixedSize(true);
            recycler_view_item_portrait_list.setAdapter(portraitVideoAdapter);
            recycler_view_item_portrait_list.setLayoutManager(linearLayoutManager);
            portraitVideoAdapter.notifyDataSetChanged();
        }
    }

    public class AdmobNativeHolder extends RecyclerView.ViewHolder {
        private UnifiedNativeAd nativeAd;
        private FrameLayout frameLayout;

        public AdmobNativeHolder(@NonNull View itemView) {
            super(itemView);

            frameLayout = (FrameLayout) itemView.findViewById(R.id.fl_adplaceholder);
            AdLoader.Builder builder = new AdLoader.Builder(activity, activity.getResources().getString(R.string.ad_unit_id_native));

            builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                // OnUnifiedNativeAdLoadedListener implementation.
                @Override
                public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                    // You must call destroy on old ads when you are done with them,
                    // otherwise you will have a memory leak.
                    if (nativeAd != null) {
                        nativeAd.destroy();
                    }
                    nativeAd = unifiedNativeAd;

                    UnifiedNativeAdView adView = (UnifiedNativeAdView) activity.getLayoutInflater()
                            .inflate(R.layout.ad_unified, null);
                    populateUnifiedNativeAdView(unifiedNativeAd, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                }

            });

            VideoOptions videoOptions = new VideoOptions.Builder()
                    .setStartMuted(true)
                    .build();

            NativeAdOptions adOptions = new NativeAdOptions.Builder()
                    .setVideoOptions(videoOptions)
                    .build();

            builder.withNativeAdOptions(adOptions);

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {


                }
            }).build();

            adLoader.loadAds(new AdRequest.Builder().addTestDevice("FAF86C60429038E83D29176F3253C13F").build(), 1);
        }
    }

}
