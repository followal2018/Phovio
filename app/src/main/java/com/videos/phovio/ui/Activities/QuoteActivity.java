package com.videos.phovio.ui.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.like.LikeButton;
import com.like.OnAnimationEndListener;
import com.like.OnLikeListener;
import com.peekandpop.shalskar.peekandpop.PeekAndPop;
import com.squareup.picasso.Picasso;
import com.videos.phovio.Adapters.CommentAdapter;
import com.videos.phovio.Adapters.StatusAdapter;
import com.videos.phovio.Provider.DownloadStorage;
import com.videos.phovio.Provider.FavoritesStorage;
import com.videos.phovio.Provider.PrefManager;
import com.videos.phovio.R;
import com.videos.phovio.api.apiClient;
import com.videos.phovio.api.apiRest;
import com.videos.phovio.model.ApiResponse;
import com.videos.phovio.model.Category;
import com.videos.phovio.model.Comment;
import com.videos.phovio.model.Language;
import com.videos.phovio.model.Status;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
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

public class QuoteActivity extends AppCompatActivity {
    private static final String COPY_ID = "com.android.copy";
    private static final String WHATSAPP_ID = "com.whatsapp";
    private static final String FACEBOOK_ID = "com.facebook.katana";
    private static final String MESSENGER_ID = "com.facebook.orca";
    private static final String INSTAGRAM_ID = "com.instagram.android";
    private static final String SHARE_ID = "com.android.all";
    private static final String DOWNLOAD_ID = "com.android.download";
    private static final String TWITTER_ID = "com.twitter.android";
    private static final String SNAPSHAT_ID = "com.snapchat.android";
    private static final String HIKE_ID = "com.bsb.hike";
    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final String TAG = QuoteActivity.class.getSimpleName();
    RewardedAd rewardedAd;
    boolean Isrewardcompleted = false;
    private int id;
    private int userid;
    private String user;
    private String userimage;
    private String created;
    private boolean comment;
    private int comments = 0;
    private String from;
    private String title;
    private String kind;
    private String original;
    private String thumbnail;
    private String type;
    private String extension;
    private int downloads;
    private int views;
    private String tags;
    private boolean review;
    private int like;
    private int love;
    private int angry;
    private int haha;
    private int woow;
    private int sad;
    private String color;
    private int position;
    private int superLikeCount;
    private Boolean downloading = false;
    private String path;
    private String urlToDownload;
    private ViewPager main_view_pager;
    private PrefManager prefManager;
    private String language = "0";
    private String local;
    private String description;
    private CircleImageView circle_image_view_activity_quote_user;
    private TextView text_view_activity_quote_name_user;
    private LikeButton like_button_whatsapp_activity_quote;
    private LikeButton like_button_messenger_activity_quote;
    private LikeButton like_button_twitter_activity_quote;
    private LikeButton like_button_copy_activity_quote;
    private LikeButton like_button_hike_activity_quote;
    private LikeButton like_button_fav_activity_quote;
    private LikeButton like_botton_share_activity_quote;
    private InterstitialAd mInterstitialAdDownload;
    private int open_action;
    private TextView text_view_wallpaper_comments_count;
    private RelativeLayout relative_layout_comment_section;
    private EditText edit_text_comment_add;
    private ProgressBar progress_bar_comment_add;
    private ProgressBar progress_bar_comment_list;
    private ImageView image_button_comment_add;
    private RecyclerView recycle_view_comment;
    private ImageView imageView_empty_comment;
    private LikeButton like_button_comments_wallpaper;
    private RelativeLayout relative_layout_wallpaper_comments;
    private ArrayList<Comment> commentList = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private LinearLayoutManager linearLayoutManagerCOmment;
    private RelativeLayout relative_layout_dialog_top;
    private Button button_follow_user_activity;
    private RelativeLayout relative_layout_main;
    private ScrollView scroll_view_main;
    private View view;
    private TextView text_view_comment_box_count;
    private ImageView image_view_comment_box_close;
    private TextView text_view_sad_activity_quote;
    private TextView text_view_angry_activity_quote;
    private TextView text_view_haha_activity_quote;
    private TextView text_view_love_activity_quote;
    private TextView text_view_like_activity_quote;
    private TextView text_view_woow_activity_quote;
    private LikeButton like_button_sad_activity_quote;
    private LikeButton like_button_angry_activity_quote;
    private LikeButton like_button_woow_activity_quote;
    private LikeButton like_button_like_activity_quote;
    private LikeButton like_button_haha_activity_quote;
    private LikeButton like_button_love_activity_quote;
    private RelativeLayout relative_layout_progress_activity_quote;
    private TextView text_view_progress_activity_quote;
    private ProgressBar progress_bar_activity_quote;
    private LikeButton like_button_instagram_activity_quote;
    private LikeButton like_button_download_activity_quote;
    private LikeButton like_button_facebook_activity_quote;
    private LinearLayout linear_layout_reactions_loading;
    private int strtext;
    private List<Status> statusList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();
    private StatusAdapter statusAdapter;
    private PeekAndPop peekAndPop;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recycler_view_status_load_more;
    private CardView card_view_reactions;
    private LikeButton like_button_reaction_activity_quote;
    private LikeButton like_button_super_like_activity_quote;
    private TextView text_view_reaction_count;
    private TextView text_view_super_like_count;

    private int reaction_count;
    private RelativeLayout relative_layout_quote_activity_quote;
    private TextView text_view_quote_status_activity_quote;
    private ImageView image_view_status_verified;
    private int font = 1;
    private RelativeLayout ripple_view_wallpaper_super_like;
    private RelativeLayout nativeBannerAdContainer;
    private LinearLayout adView;
    private NativeBannerAd nativeBannerAd;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        checkPermission();
        loadLang();
        Bundle bundle = getIntent().getExtras();
        this.from = bundle.getString("from");


        this.id = bundle.getInt("id");
        this.title = bundle.getString("title");
        this.description = bundle.getString("description");
        this.thumbnail = bundle.getString("thumbnail");
        this.userid = bundle.getInt("userid");
        this.user = bundle.getString("user");
        this.userimage = bundle.getString("userimage");
        this.type = bundle.getString("type");
        this.original = bundle.getString("original");
        this.extension = bundle.getString("extension");
        this.comment = bundle.getBoolean("comment");
        this.downloads = bundle.getInt("downloads");
        this.font = bundle.getInt("font");
        this.views = bundle.getInt("views");
        this.tags = bundle.getString("tags");
        this.review = bundle.getBoolean("review");
        this.comments = bundle.getInt("comments");
        this.created = bundle.getString("created");
        this.local = bundle.getString("local");

        this.woow = bundle.getInt("woow");
        this.like = bundle.getInt("like");
        this.love = bundle.getInt("love");
        this.angry = bundle.getInt("angry");
        this.sad = bundle.getInt("sad");
        this.haha = bundle.getInt("haha");
        this.kind = bundle.getString("kind");
        this.color = bundle.getString("color");
        this.position = bundle.getInt("position");
        this.superLikeCount = bundle.getInt("superLikeCount");

        urlToDownload = original;

        this.prefManager = new PrefManager(getApplicationContext());
        this.language = prefManager.getString("LANGUAGE_DEFAULT");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.reaction_count = haha + like + love + woow + sad + angry;

        loadRewardedAd();


        initView();
        initAction();
        initInterstitialAdPrepare();
        initStatus();
        getUser();
        setReaction(prefManager.getString("reaction_" + id));
        loadMore();
        initAds();
        showAdsBanner();
        addView();

    }

    private void onSuperLikeClicked() {
        PrefManager prefManager = new PrefManager(this);


        if (prefManager.getInt("SuperLikeCount") == 5) {

            showDialogTimer();

        } else {
            String SuperLikeIds = prefManager.getString("SuperLikeUserIds");
            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            if (prefManager.getString("SuperLikeDate").equalsIgnoreCase(sdf.format(today))) {
                if (SuperLikeIds.contains(String.valueOf(userid))) {
                    Toasty.error(this, "Already Superlike this user post", Toast.LENGTH_SHORT, true).show();
                } else {
                    showDialog(id, userid, position);
                }
            } else {
                prefManager.setInt("SuperLikeCount", 0);
                prefManager.setString("SuperLikeUserIds", "");
                prefManager.setString("SuperLikeDate", sdf.format(today));
//                            setSuperLikedata(position);
//                            AddSuperLikePoints(statusList.get(position).getId(), statusList.get(position).getUserid());
                showDialog(id, userid, position);
            }
        }
    }


    public void showDialogTimer() {
        final Dialog dialog = new Dialog(this,
                R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_rewardadtimer);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        final TextView textviewtimer = (TextView) dialog.findViewById(R.id.textviewtimer);
        TextView text_view_reward_ok = (TextView) dialog.findViewById(R.id.text_view_reward_ok);

        Calendar now = Calendar.getInstance();
        now = Calendar.getInstance();
        PrefManager prefManager = new PrefManager(this);
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
        final Dialog dialog = new Dialog(this,
                R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_rewardads);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
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

    private void loadRewardedAd() {
        if (rewardedAd == null || !rewardedAd.isLoaded()) {
//            isLoading = true;
            rewardedAd = new RewardedAd(this, this.getString(R.string.ad_unit_id_reward));

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
                                Toasty.error(getApplicationContext(), "You have to watch full video to earn points", Toast.LENGTH_SHORT, true).show();
                            }
                            // Ad closed.
//                            Toast.makeText(getActivity(), "onRewardedAdClosed", Toast.LENGTH_SHORT).show();
                            // Preload the next rewarded ad.
                            loadRewardedAd();
                        }

                        @Override
                        public void onUserEarnedReward(RewardItem rewardItem) {


                            Isrewardcompleted = true;
                            Toasty.success(getApplicationContext(), "Success", Toast.LENGTH_SHORT, true).show();
                            AddSuperLikePoints(superlikePostId, userid, position);
                        }

                        @Override
                        public void onRewardedAdFailedToShow(int errorCode) {

                            // Ad failed to display
//                            Toast.makeText(getActivity(), "onRewardedAdFailedToShow", Toast.LENGTH_SHORT)
//                                    .show();
                        }
                    };
            rewardedAd.show(this, adCallback);
        } else {
            Toasty.error(getApplicationContext(), "Ads will Available in Next 15 Min.", Toast.LENGTH_SHORT, true).show();
        }
    }

    public void AddSuperLikePoints(Integer postid, Integer userid, final Integer position) {
        final PrefManager prefManager = new PrefManager(this);
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
                apiClient.FormatData(QuoteActivity.this, response);

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

    public void setSuperLikedata(int position) {

        PrefManager prefManager = new PrefManager(this);

        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR, 6);
        System.out.println(now.getTime());

        long milisecond = now.getTimeInMillis();
        Log.e("milisecond", "" + milisecond);
        prefManager.setString("LastSuperLikeTime", "" + milisecond);


        int SuperLikeCount = prefManager.getInt("SuperLikeCount") + 1;
        prefManager.setInt("SuperLikeCount", SuperLikeCount);
        Log.e("SuperLikeCount", "" + prefManager.getInt("SuperLikeCount"));

        prefManager.setString("SuperLikeUserIds", String.valueOf(userid));
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        prefManager.setString("SuperLikeDate", sdf.format(today));

        superLikeCount++;
        setReaction("none");

    }

    private void showAdsBanner() {
        if (prefManager.getString("SUBSCRIBED").equals("FALSE")) {
            final AdView mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder()
                    .build();

            // Start loading the ad in the background.
            mAdView.loadAd(adRequest);

            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mAdView.setVisibility(View.VISIBLE);
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        if (from != null) {
            Intent intent = new Intent(QuoteActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
            finish();
        } else {
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
                if (from != null) {
                    Intent intent = new Intent(QuoteActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                    finish();
                } else {
                    super.onBackPressed();
                    overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        checkPermission();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private void initStatus() {
        Picasso.with(QuoteActivity.this.getApplicationContext()).load(userimage).error(R.drawable.profile).placeholder(R.drawable.profile).into(this.circle_image_view_activity_quote_user);

        this.text_view_activity_quote_name_user.setText(user);
        this.text_view_wallpaper_comments_count.setText(comments + "");
        this.text_view_reaction_count.setText(format(reaction_count));
        this.text_view_super_like_count.setText(format(superLikeCount));
        if (this.comment) {
            relative_layout_comment_section.setVisibility(View.VISIBLE);
        } else {
            relative_layout_comment_section.setVisibility(View.GONE);
        }


        this.text_view_wallpaper_comments_count.setText(comments + "");
        this.text_view_comment_box_count.setText(comments + " " + QuoteActivity.this.getResources().getString(R.string.comments));
        if (this.comment) {
            relative_layout_comment_section.setVisibility(View.VISIBLE);
        } else {
            relative_layout_comment_section.setVisibility(View.GONE);
        }

        this.text_view_like_activity_quote.setText(format(like));
        this.text_view_love_activity_quote.setText(format(love));
        this.text_view_angry_activity_quote.setText(format(angry));
        this.text_view_haha_activity_quote.setText(format(haha));
        this.text_view_woow_activity_quote.setText(format(woow));
        this.text_view_sad_activity_quote.setText(format(sad));

        String text = null;
        try {
            byte[] data = Base64.decode(title, Base64.DEFAULT);
            text = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        text_view_quote_status_activity_quote.setText(text);
        relative_layout_quote_activity_quote.setBackgroundResource(R.drawable.bg_quote_background);
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "font_" + font + ".ttf");
        text_view_quote_status_activity_quote.setTypeface(tf);
        GradientDrawable drawable = (GradientDrawable) relative_layout_quote_activity_quote.getBackground();
        drawable.setColor(Color.parseColor("#" + color));
    }

    private void initView() {
        this.card_view_reactions = (CardView) findViewById(R.id.card_view_reactions);
        this.recycler_view_status_load_more = (RecyclerView) findViewById(R.id.recycler_view_status_load_more);
        this.peekAndPop = new PeekAndPop.Builder(QuoteActivity.this)
                .parentViewGroupToDisallowTouchEvents(recycler_view_status_load_more)
                .peekLayout(R.layout.dialog_view)
                .build();
        this.linearLayoutManager = new LinearLayoutManager(QuoteActivity.this, LinearLayoutManager.VERTICAL, false);
        statusAdapter = new StatusAdapter(statusList, categoryList, QuoteActivity.this, peekAndPop);
        recycler_view_status_load_more.setHasFixedSize(true);
        recycler_view_status_load_more.setAdapter(statusAdapter);
        recycler_view_status_load_more.setLayoutManager(linearLayoutManager);

        this.image_view_status_verified = (ImageView) findViewById(R.id.image_view_status_verified);

        this.relative_layout_quote_activity_quote = (RelativeLayout) findViewById(R.id.relative_layout_quote_activity_quote);
        this.text_view_quote_status_activity_quote = (TextView) findViewById(R.id.text_view_quote_status_activity_quote);
        this.text_view_reaction_count = (TextView) findViewById(R.id.text_view_reaction_count);
        this.text_view_super_like_count = (TextView) findViewById(R.id.text_view_super_like_count);
        this.linear_layout_reactions_loading = (LinearLayout) findViewById(R.id.linear_layout_reactions_loading);
        this.like_button_reaction_activity_quote = (LikeButton) findViewById(R.id.like_button_reaction_activity_quote);
        this.like_button_super_like_activity_quote = (LikeButton) findViewById(R.id.like_button_super_like_activity_quote);
        this.like_button_download_activity_quote = (LikeButton) findViewById(R.id.like_button_download_activity_quote);
        this.like_button_facebook_activity_quote = (LikeButton) findViewById(R.id.like_button_facebook_activity_quote);
        this.like_button_instagram_activity_quote = (LikeButton) findViewById(R.id.like_button_instagram_activity_quote);
        this.progress_bar_activity_quote = (ProgressBar) findViewById(R.id.progress_bar_activity_quote);
        this.text_view_progress_activity_quote = (TextView) findViewById(R.id.text_view_progress_activity_quote);
        this.relative_layout_progress_activity_quote = (RelativeLayout) findViewById(R.id.relative_layout_progress_activity_quote);
        this.like_button_angry_activity_quote = (LikeButton) findViewById(R.id.like_button_angry_activity_quote);
        this.like_button_like_activity_quote = (LikeButton) findViewById(R.id.like_button_like_activity_quote);
        this.like_button_love_activity_quote = (LikeButton) findViewById(R.id.like_button_love_activity_quote);
        this.like_button_sad_activity_quote = (LikeButton) findViewById(R.id.like_button_sad_activity_quote);
        this.like_button_woow_activity_quote = (LikeButton) findViewById(R.id.like_button_woow_activity_quote);
        this.like_button_haha_activity_quote = (LikeButton) findViewById(R.id.like_button_haha_activity_quote);

        this.text_view_sad_activity_quote = (TextView) findViewById(R.id.text_view_sad_activity_quote);
        this.text_view_angry_activity_quote = (TextView) findViewById(R.id.text_view_angry_activity_quote);
        this.text_view_haha_activity_quote = (TextView) findViewById(R.id.text_view_haha_activity_quote);
        this.text_view_love_activity_quote = (TextView) findViewById(R.id.text_view_love_activity_quote);
        this.text_view_like_activity_quote = (TextView) findViewById(R.id.text_view_like_activity_quote);
        this.text_view_woow_activity_quote = (TextView) findViewById(R.id.text_view_woow_activity_quote);
        this.image_view_comment_box_close = (ImageView) findViewById(R.id.image_view_comment_box_close);
        this.text_view_comment_box_count = (TextView) findViewById(R.id.text_view_comment_box_count);
        this.scroll_view_main = (ScrollView) findViewById(R.id.scroll_view_main);
        this.relative_layout_main = (RelativeLayout) findViewById(R.id.relative_layout_main);
        this.like_button_whatsapp_activity_quote = (LikeButton) findViewById(R.id.like_button_whatsapp_activity_quote);
        this.like_button_messenger_activity_quote = (LikeButton) findViewById(R.id.like_button_messenger_activity_quote);
        this.like_button_twitter_activity_quote = (LikeButton) findViewById(R.id.like_button_twitter_activity_quote);
        this.like_button_copy_activity_quote = (LikeButton) findViewById(R.id.like_button_copy_activity_quote);
        this.like_button_hike_activity_quote = (LikeButton) findViewById(R.id.like_button_hike_activity_quote);

        this.like_button_fav_activity_quote = (LikeButton) findViewById(R.id.like_button_fav_activity_quote);
        this.like_botton_share_activity_quote = (LikeButton) findViewById(R.id.like_botton_share_activity_quote);

        this.text_view_activity_quote_name_user = (TextView) findViewById(R.id.text_view_activity_quote_name_user);
        this.circle_image_view_activity_quote_user = (CircleImageView) findViewById(R.id.circle_image_view_activity_quote_user);


        this.linearLayoutManager = new LinearLayoutManager(QuoteActivity.this, LinearLayoutManager.VERTICAL, false);


        this.linearLayoutManagerCOmment = new LinearLayoutManager(QuoteActivity.this, LinearLayoutManager.VERTICAL, false);

        this.text_view_wallpaper_comments_count = (TextView) findViewById(R.id.text_view_wallpaper_comments_count);
        this.relative_layout_comment_section = (RelativeLayout) findViewById(R.id.relative_layout_comment_section);
        this.edit_text_comment_add = (EditText) findViewById(R.id.edit_text_comment_add);
        this.progress_bar_comment_add = (ProgressBar) findViewById(R.id.progress_bar_comment_add);
        this.progress_bar_comment_list = (ProgressBar) findViewById(R.id.progress_bar_comment_list);
        this.image_button_comment_add = (ImageView) findViewById(R.id.image_button_comment_add);
        this.recycle_view_comment = (RecyclerView) findViewById(R.id.recycle_view_comment);
        this.commentAdapter = new CommentAdapter(commentList, QuoteActivity.this.getApplication());
        this.recycle_view_comment.setHasFixedSize(true);
        this.recycle_view_comment.setAdapter(commentAdapter);
        this.recycle_view_comment.setLayoutManager(linearLayoutManagerCOmment);
        this.imageView_empty_comment = (ImageView) findViewById(R.id.imageView_empty_comment);
        this.like_button_comments_wallpaper = (LikeButton) findViewById(R.id.like_botton_comment_activity_quote);
        this.relative_layout_wallpaper_comments = (RelativeLayout) findViewById(R.id.relative_layout_wallpaper_comments);
        image_button_comment_add.setEnabled(false);


        this.button_follow_user_activity = (Button) findViewById(R.id.button_follow_user_activity);
        this.relative_layout_dialog_top = (RelativeLayout) findViewById(R.id.relative_layout_dialog_top);

        try {
            ripple_view_wallpaper_super_like = findViewById(R.id.ripple_view_wallpaper_super_like);
            ripple_view_wallpaper_super_like.setVisibility(Integer.parseInt(prefManager.getString("ID_USER")) == userid ? View.GONE : View.VISIBLE);
        } catch (Exception e) {
            Log.e("Error", "" + e.toString());
        }
        final FavoritesStorage storageFavorites = new FavoritesStorage(QuoteActivity.this.getApplicationContext());

        List<Status> favorites_list = storageFavorites.loadImagesFavorites();
        Boolean exist = false;
        if (favorites_list == null) {
            favorites_list = new ArrayList<>();
        }
        for (int i = 0; i < favorites_list.size(); i++) {
            if (favorites_list.get(i).getId().equals(id)) {
                exist = true;
            }
        }
        if (exist == false) {
            like_button_fav_activity_quote.setLiked(false);
        } else {
            like_button_fav_activity_quote.setLiked(true);
        }
    }

    private void initAction() {

        this.like_button_like_activity_quote.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                addLike(id);
                card_view_reactions.setVisibility(View.GONE);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                deleteLike(id);
                setReaction("none");
                prefManager.setString("reaction_" + id, "none");
                card_view_reactions.setVisibility(View.GONE);

            }
        });

        this.like_button_love_activity_quote.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                addLove(id);
                card_view_reactions.setVisibility(View.GONE);

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                deleteLove(id);
                setReaction("none");
                prefManager.setString("reaction_" + id, "none");
                card_view_reactions.setVisibility(View.GONE);

            }
        });
        this.like_button_woow_activity_quote.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                addWoow(id);
                card_view_reactions.setVisibility(View.GONE);


            }

            @Override
            public void unLiked(LikeButton likeButton) {
                deleteWoow(id);
                setReaction("none");
                prefManager.setString("reaction_" + id, "none");
                card_view_reactions.setVisibility(View.GONE);

            }
        });

        this.like_button_angry_activity_quote.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                addAngry(id);
                card_view_reactions.setVisibility(View.GONE);

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                deleteAngry(id);
                setReaction("none");
                prefManager.setString("reaction_" + id, "none");
                card_view_reactions.setVisibility(View.GONE);

            }
        });
        this.like_button_sad_activity_quote.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                addSad(id);
                card_view_reactions.setVisibility(View.GONE);

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                deleteSad(id);
                setReaction("none");
                prefManager.setString("reaction_" + id, "none");
                card_view_reactions.setVisibility(View.GONE);

            }
        });
        this.like_button_haha_activity_quote.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                addHaha(id);
                card_view_reactions.setVisibility(View.GONE);

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                deleteHaha(id);
                setReaction("none");
                prefManager.setString("reaction_" + id, "none");
                card_view_reactions.setVisibility(View.GONE);

            }
        });
        this.image_view_comment_box_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCommentBox();
            }
        });
        this.button_follow_user_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                follow();
            }
        });
        this.relative_layout_dialog_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuoteActivity.this, UserActivity.class);
                intent.putExtra("id", userid);
                intent.putExtra("name", user);
                intent.putExtra("image", userimage);
                startActivity(intent);
            }
        });
        this.like_button_whatsapp_activity_quote.setOnAnimationEndListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(LikeButton likeButton) {
                like_button_whatsapp_activity_quote.setLiked(false);
                if (mInterstitialAdDownload.isLoaded()) {
                    if (check()) {
                        mInterstitialAdDownload.show();
                        open_action = 5001;
                    } else {
                        shareTextWith(WHATSAPP_ID);
                    }
                } else {
                    shareTextWith(WHATSAPP_ID);
                }
            }
        });

        this.like_button_messenger_activity_quote.setOnAnimationEndListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(LikeButton likeButton) {
                like_button_messenger_activity_quote.setLiked(false);


                if (mInterstitialAdDownload.isLoaded()) {
                    if (check()) {
                        mInterstitialAdDownload.show();
                        open_action = 5002;
                    } else {
                        shareTextWith(MESSENGER_ID);

                    }
                } else {
                    shareTextWith(MESSENGER_ID);

                }
            }
        });
        this.like_button_facebook_activity_quote.setOnAnimationEndListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(LikeButton likeButton) {
                like_button_facebook_activity_quote.setLiked(false);


                if (mInterstitialAdDownload.isLoaded()) {
                    if (check()) {
                        mInterstitialAdDownload.show();
                        open_action = 5003;
                    } else {
                        shareTextWith(FACEBOOK_ID);


                    }
                } else {
                    shareTextWith(FACEBOOK_ID);

                }
            }
        });
        this.like_button_instagram_activity_quote.setOnAnimationEndListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(LikeButton likeButton) {
                like_button_instagram_activity_quote.setLiked(false);


                if (mInterstitialAdDownload.isLoaded()) {
                    if (check()) {
                        mInterstitialAdDownload.show();
                        open_action = 5004;
                    } else {
                        shareTextWith(INSTAGRAM_ID);

                    }
                } else {
                    shareTextWith(INSTAGRAM_ID);

                }
            }
        });
        this.like_button_twitter_activity_quote.setOnAnimationEndListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(LikeButton likeButton) {
                like_button_twitter_activity_quote.setLiked(false);


                if (mInterstitialAdDownload.isLoaded()) {
                    if (check()) {
                        mInterstitialAdDownload.show();
                        open_action = 5005;
                    } else {
                        shareTextWith(TWITTER_ID);
                    }
                } else {
                    shareTextWith(TWITTER_ID);
                }
            }
        });
        this.like_botton_share_activity_quote.setOnAnimationEndListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(LikeButton likeButton) {
                like_botton_share_activity_quote.setLiked(false);


                if (mInterstitialAdDownload.isLoaded()) {
                    if (check()) {
                        mInterstitialAdDownload.show();
                        open_action = 5006;
                    } else {
                        shareTextWith(SHARE_ID);
                    }
                } else {
                    shareTextWith(SHARE_ID);
                }
            }
        });
        this.like_button_download_activity_quote.setOnAnimationEndListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(LikeButton likeButton) {
                like_button_download_activity_quote.setLiked(false);

                if (mInterstitialAdDownload.isLoaded()) {
                    if (check()) {
                        mInterstitialAdDownload.show();
                        open_action = 5007;
                    } else {
                        ShareAsImage();
                    }
                } else {
                    ShareAsImage();
                }
            }
        });

        this.like_button_hike_activity_quote.setOnAnimationEndListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(LikeButton likeButton) {
                like_button_hike_activity_quote.setLiked(false);

                if (mInterstitialAdDownload.isLoaded()) {
                    if (check()) {
                        mInterstitialAdDownload.show();
                        open_action = 5008;
                    } else {
                        shareTextWith(HIKE_ID);
                    }
                } else {
                    shareTextWith(HIKE_ID);
                }
            }
        });
        this.like_button_copy_activity_quote.setOnAnimationEndListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(LikeButton likeButton) {
                like_button_copy_activity_quote.setLiked(false);
                if (mInterstitialAdDownload.isLoaded()) {
                    if (check()) {
                        mInterstitialAdDownload.show();
                        open_action = 5009;
                    } else {
                        shareTextWith(COPY_ID);

                    }
                } else {
                    shareTextWith(COPY_ID);

                }
            }
        });
        this.like_button_fav_activity_quote.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if (mInterstitialAdDownload.isLoaded()) {
                    if (check()) {
                        mInterstitialAdDownload.show();
                        open_action = 5010;
                    } else {
                        favorite();
                    }
                } else {
                    favorite();
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                if (mInterstitialAdDownload.isLoaded()) {
                    if (check()) {
                        mInterstitialAdDownload.show();
                        open_action = 5010;
                    } else {
                        favorite();
                    }
                } else {
                    favorite();
                }
            }
        });


        this.image_button_comment_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment();
            }
        });

        this.edit_text_comment_add.addTextChangedListener(new QuoteActivity.CommentTextWatcher(this.edit_text_comment_add));


        this.like_button_comments_wallpaper.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                like_button_comments_wallpaper.setLiked(false);
                showCommentBox();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                like_button_comments_wallpaper.setLiked(false);
                showCommentBox();
            }
        });
        this.like_button_reaction_activity_quote.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                like_button_comments_wallpaper.setLiked(false);
                card_view_reactions.setVisibility(View.VISIBLE);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                like_button_comments_wallpaper.setLiked(false);
                card_view_reactions.setVisibility(View.VISIBLE);
            }
        });

        this.like_button_super_like_activity_quote.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                onSuperLikeClicked();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
            }
        });


    }

    private boolean ValidateComment() {
        String email = edit_text_comment_add.getText().toString().trim();
        if (email.isEmpty()) {
            image_button_comment_add.setEnabled(false);
            return false;
        } else {
            image_button_comment_add.setEnabled(true);
        }
        return true;
    }

    public void getComments() {
        progress_bar_comment_list.setVisibility(View.VISIBLE);
        recycle_view_comment.setVisibility(View.GONE);
        imageView_empty_comment.setVisibility(View.GONE);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Comment>> call = service.getComments(id);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful()) {
                    commentList.clear();
                    comments = response.body().size();
                    text_view_wallpaper_comments_count.setText(comments + "");
                    text_view_comment_box_count.setText(comments + " " + QuoteActivity.this.getResources().getString(R.string.comments));
                    if (response.body().size() != 0) {
                        for (int i = 0; i < response.body().size(); i++) {
                            commentList.add(response.body().get(i));
                        }
                        commentAdapter.notifyDataSetChanged();

                        progress_bar_comment_list.setVisibility(View.GONE);
                        recycle_view_comment.setVisibility(View.VISIBLE);
                        imageView_empty_comment.setVisibility(View.GONE);


                    } else {
                        progress_bar_comment_list.setVisibility(View.GONE);
                        recycle_view_comment.setVisibility(View.GONE);
                        imageView_empty_comment.setVisibility(View.VISIBLE);

                    }
                } else {

                }
                recycle_view_comment.setNestedScrollingEnabled(false);

            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {


            }
        });
    }

    public void showCommentBox() {
        getComments();
        if (relative_layout_wallpaper_comments.getVisibility() == View.VISIBLE) {
            Animation c = AnimationUtils.loadAnimation(QuoteActivity.this.getApplicationContext(),
                    R.anim.slide_down);
            c.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    relative_layout_wallpaper_comments.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            relative_layout_wallpaper_comments.startAnimation(c);


        } else {
            Animation c = AnimationUtils.loadAnimation(QuoteActivity.this.getApplicationContext(),
                    R.anim.slide_up);
            c.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    relative_layout_wallpaper_comments.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            relative_layout_wallpaper_comments.startAnimation(c);

        }

    }

    public void favorite() {
        final FavoritesStorage storageFavorites = new FavoritesStorage(QuoteActivity.this.getApplicationContext());

        List<Status> favorites_list = storageFavorites.loadImagesFavorites();
        Boolean exist = false;
        if (favorites_list == null) {
            favorites_list = new ArrayList<>();
        }
        for (int i = 0; i < favorites_list.size(); i++) {
            if (favorites_list.get(i).getId().equals(id)) {
                exist = true;
            }
        }
        if (exist == false) {
            ArrayList<Status> audios = new ArrayList<Status>();
            for (int i = 0; i < favorites_list.size(); i++) {
                audios.add(favorites_list.get(i));
            }


            Status fragement_Status_joke = new Status();
            fragement_Status_joke.setId(id);
            fragement_Status_joke.setTitle(title);
            fragement_Status_joke.setDescription(description);
            fragement_Status_joke.setDownloads(downloads);
            fragement_Status_joke.setViews(views);
            fragement_Status_joke.setFont(font);
            fragement_Status_joke.setThumbnail(thumbnail);
            fragement_Status_joke.setColor(color);
            fragement_Status_joke.setOriginal(original);
            fragement_Status_joke.setUserid(userid);
            fragement_Status_joke.setUser(user);
            fragement_Status_joke.setTags(tags);
            fragement_Status_joke.setReview(review);
            fragement_Status_joke.setUserimage(userimage);
            fragement_Status_joke.setComments(comments);
            fragement_Status_joke.setComment(comment);
            fragement_Status_joke.setCreated(created);
            fragement_Status_joke.setExtension(extension);
            fragement_Status_joke.setType(type);

            fragement_Status_joke.setLike(like);
            fragement_Status_joke.setKind(kind);
            fragement_Status_joke.setLove(love);
            fragement_Status_joke.setAngry(angry);
            fragement_Status_joke.setSad(sad);
            fragement_Status_joke.setHaha(haha);
            fragement_Status_joke.setWoow(woow);


            audios.add(fragement_Status_joke);
            storageFavorites.storeImage(audios);
        } else {
            ArrayList<Status> new_favorites = new ArrayList<Status>();
            for (int i = 0; i < favorites_list.size(); i++) {
                if (!favorites_list.get(i).getId().equals(id)) {
                    new_favorites.add(favorites_list.get(i));

                }
            }
            storageFavorites.storeImage(new_favorites);
        }
    }

    public void AddDownloadLocal(String localpath) {
        final DownloadStorage downloadStorage = new DownloadStorage(QuoteActivity.this.getApplicationContext());
        List<Status> download_list = downloadStorage.loadImagesFavorites();
        Boolean exist = false;
        if (download_list == null) {
            download_list = new ArrayList<>();
        }
        for (int i = 0; i < download_list.size(); i++) {
            if (download_list.get(i).getId().equals(id)) {
                exist = true;
            }
        }
        if (exist == false) {
            ArrayList<Status> audios = new ArrayList<Status>();
            for (int i = 0; i < download_list.size(); i++) {
                audios.add(download_list.get(i));
            }
            Status videodownloaded = new Status();
            videodownloaded.setId(id);
            videodownloaded.setTitle(title);
            videodownloaded.setDescription(description);
            videodownloaded.setDownloads(downloads);
            videodownloaded.setViews(views);
            videodownloaded.setFont(font);
            videodownloaded.setThumbnail(thumbnail);
            videodownloaded.setOriginal(original);
            videodownloaded.setUser(user);
            videodownloaded.setColor(color);

            videodownloaded.setUserid(userid);
            videodownloaded.setTags(tags);
            videodownloaded.setReview(review);
            videodownloaded.setUserimage(userimage);
            videodownloaded.setComments(comments);
            videodownloaded.setComment(comment);
            videodownloaded.setCreated(created);
            videodownloaded.setExtension(extension);
            videodownloaded.setType(type);
            videodownloaded.setKind(kind);

            videodownloaded.setLike(like);
            videodownloaded.setLove(love);
            videodownloaded.setAngry(angry);
            videodownloaded.setSad(sad);
            videodownloaded.setHaha(haha);
            videodownloaded.setWoow(woow);
            videodownloaded.setLocal(localpath);

            audios.add(videodownloaded);
            downloadStorage.storeImage(audios);
        }
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(QuoteActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


                if (ActivityCompat.shouldShowRequestPermissionRationale(QuoteActivity.this, Manifest.permission.READ_CONTACTS)) {
                    Intent intent_image = new Intent(QuoteActivity.this.getApplicationContext(), PermissionActivity.class);
                    startActivity(intent_image);
                    QuoteActivity.this.overridePendingTransition(R.anim.enter, R.anim.exit);
                    QuoteActivity.this.finish();
                } else {
                    Intent intent_image = new Intent(QuoteActivity.this.getApplicationContext(), PermissionActivity.class);
                    startActivity(intent_image);
                    QuoteActivity.this.overridePendingTransition(R.anim.enter, R.anim.exit);
                    QuoteActivity.this.finish();
                }
            }

        }
    }

    public void shareTextWith(String APP_ID) {
        if (COPY_ID == APP_ID) {
            String text = null;
            try {
                byte[] data = Base64.decode(title, Base64.DEFAULT);
                text = new String(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("quote" + id, text);
            clipboard.setPrimaryClip(clip);
            Toasty.success(this, getResources().getString(R.string.status_copied_success)).show();
            addShare(id);
            return;
        }
        String text = null;
        try {
            byte[] data = Base64.decode(title, Base64.DEFAULT);
            text = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String shareBody = text;
        // shareBody += " \n\n  "+getApplicationContext().getResources().getString(R.string.download_more_from_link);

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        if (!SHARE_ID.equals(APP_ID)) {
            sharingIntent.setPackage(APP_ID);
        }
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_via)));
        addShare(id);
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAdDownload.loadAd(adRequest);
    }

    private void initInterstitialAdPrepare() {
        mInterstitialAdDownload = new InterstitialAd(QuoteActivity.this);
        mInterstitialAdDownload.setAdUnitId(getResources().getString(R.string.ad_unit_id_interstitial));

        mInterstitialAdDownload.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                switch (open_action) {

                    case 5001: {
                        shareTextWith(WHATSAPP_ID);
                        break;
                    }
                    case 5002: {
                        shareTextWith(MESSENGER_ID);
                        break;
                    }
                    case 5003: {

                        shareTextWith(FACEBOOK_ID);

                        break;
                    }
                    case 5004: {
                        shareTextWith(INSTAGRAM_ID);

                    }
                    case 5005: {
                        shareTextWith(TWITTER_ID);

                    }
                    case 5006: {

                        shareTextWith(SHARE_ID);

                        break;
                    }
                    case 5007: {
                        ShareAsImage();
                        break;
                    }
                    case 5008: {
                        shareTextWith(HIKE_ID);

                        break;
                    }
                    case 5009: {


                        shareTextWith(COPY_ID);

                        break;
                    }
                    case 5010: {
                        favorite();
                        break;
                    }

                }

                requestNewInterstitial();


            }
        });

        requestNewInterstitial();
    }

    public void ShareAsImage() {

        Bitmap bitmap;
        relative_layout_quote_activity_quote.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(relative_layout_quote_activity_quote.getDrawingCache());
        relative_layout_quote_activity_quote.setDrawingCacheEnabled(false);


        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/ProjectName";
        File dir = new File(file_path);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, "screen.png");
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Uri imageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

        final String final_text = getResources().getString(R.string.download_more_from_link);

        shareIntent.putExtra(Intent.EXTRA_TEXT, final_text);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(Intent.createChooser(shareIntent, "Shared via " + getResources().getString(R.string.app_name)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toasty.error(getApplicationContext(), "No App installed", Toast.LENGTH_SHORT, true).show();
        }

    }

    public boolean check() {
        PrefManager prf = new PrefManager(QuoteActivity.this.getApplicationContext());
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

                if (seconds > Integer.parseInt(getResources().getString(R.string.AD_MOB_TIME))) {
                    prf.setString("LAST_DATE_ADS", strDate);
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void loadLang() {
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Language>> call = service.languageAll();
        call.enqueue(new Callback<List<Language>>() {
            @Override
            public void onResponse(Call<List<Language>> call, final Response<List<Language>> response) {

            }

            @Override
            public void onFailure(Call<List<Language>> call, Throwable t) {
            }
        });
    }

    public void addComment() {


        PrefManager prf = new PrefManager(QuoteActivity.this.getApplicationContext());
        if (prf.getString("LOGGED").toString().equals("TRUE")) {

            byte[] data = new byte[0];
            String comment_final = "";
            try {
                data = edit_text_comment_add.getText().toString().getBytes("UTF-8");
                comment_final = Base64.encodeToString(data, Base64.DEFAULT);

            } catch (UnsupportedEncodingException e) {
                comment_final = edit_text_comment_add.getText().toString();
                e.printStackTrace();
            }
            progress_bar_comment_add.setVisibility(View.VISIBLE);
            image_button_comment_add.setVisibility(View.GONE);
            Retrofit retrofit = apiClient.getClient();
            apiRest service = retrofit.create(apiRest.class);
            Call<ApiResponse> call = service.addCommentImage(prf.getString("ID_USER").toString(), id, comment_final);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode() == 200) {
                            comments++;
                            text_view_wallpaper_comments_count.setText(comments + "");
                            text_view_comment_box_count.setText(text_view_wallpaper_comments_count.getText() + " " + QuoteActivity.this.getResources().getString(R.string.comments));
                            recycle_view_comment.setVisibility(View.VISIBLE);
                            imageView_empty_comment.setVisibility(View.GONE);
                            Toasty.success(QuoteActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            edit_text_comment_add.setText("");
                            String id = "";
                            String content = "";
                            String user = "";
                            String image = "";

                            String trusted = "false";

                            for (int i = 0; i < response.body().getValues().size(); i++) {
                                if (response.body().getValues().get(i).getName().equals("id")) {
                                    id = response.body().getValues().get(i).getValue();
                                }
                                if (response.body().getValues().get(i).getName().equals("content")) {
                                    content = response.body().getValues().get(i).getValue();
                                }
                                if (response.body().getValues().get(i).getName().equals("user")) {
                                    user = response.body().getValues().get(i).getValue();
                                }
                                if (response.body().getValues().get(i).getName().equals("trusted")) {
                                    trusted = response.body().getValues().get(i).getValue();
                                }
                                if (response.body().getValues().get(i).getName().equals("image")) {
                                    image = response.body().getValues().get(i).getValue();
                                }
                            }
                            Comment comment = new Comment();
                            comment.setId(Integer.parseInt(id));
                            comment.setUser(user);
                            comment.setContent(content);
                            comment.setImage(image);
                            comment.setEnabled(true);
                            comment.setTrusted(trusted);
                            comment.setCreated(getResources().getString(R.string.now_time));
                            commentList.add(comment);
                            commentAdapter.notifyDataSetChanged();

                        } else {
                            Toasty.error(QuoteActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    recycle_view_comment.scrollToPosition(recycle_view_comment.getAdapter().getItemCount() - 1);
                    recycle_view_comment.scrollToPosition(recycle_view_comment.getAdapter().getItemCount() - 1);
                    commentAdapter.notifyDataSetChanged();
                    progress_bar_comment_add.setVisibility(View.GONE);
                    image_button_comment_add.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    progress_bar_comment_add.setVisibility(View.VISIBLE);
                    image_button_comment_add.setVisibility(View.GONE);
                }
            });
        } else {
            Intent intent = new Intent(QuoteActivity.this, LoginActivity.class);
            startActivity(intent);
        }

    }

    public void follow() {

        PrefManager prf = new PrefManager(QuoteActivity.this.getApplicationContext());
        if (prf.getString("LOGGED").toString().equals("TRUE")) {
            button_follow_user_activity.setText(getResources().getString(R.string.loading));
            button_follow_user_activity.setEnabled(false);
            String follower = prf.getString("ID_USER");
            String key = prf.getString("TOKEN_USER");
            Retrofit retrofit = apiClient.getClient();
            apiRest service = retrofit.create(apiRest.class);
            Call<ApiResponse> call = service.follow(userid, Integer.parseInt(follower), key);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals(200)) {
                            button_follow_user_activity.setText("UnFollow");
                        } else if (response.body().getCode().equals(202)) {
                            button_follow_user_activity.setText("Follow");

                        }
                    }
                    button_follow_user_activity.setEnabled(true);

                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    button_follow_user_activity.setEnabled(true);
                }
            });
        } else {
            Intent intent = new Intent(QuoteActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void getUser() {
        PrefManager prf = new PrefManager(QuoteActivity.this.getApplicationContext());
        Integer follower = -1;
        if (prf.getString("LOGGED").toString().equals("TRUE")) {
            button_follow_user_activity.setEnabled(false);
            follower = Integer.parseInt(prf.getString("ID_USER"));
        }
        if (follower != userid) {
            button_follow_user_activity.setVisibility(View.VISIBLE);
        }
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<ApiResponse> call = service.getUser(userid, follower);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {

                    for (int i = 0; i < response.body().getValues().size(); i++) {
                        if (response.body().getValues().get(i).getName().equals("trusted")) {
                            if (response.body().getValues().get(i).getValue().equals("true"))
                                image_view_status_verified.setVisibility(View.VISIBLE);
                            else
                                image_view_status_verified.setVisibility(View.GONE);
                        }
                        if (response.body().getValues().get(i).getName().equals("follow")) {
                            if (response.body().getValues().get(i).getValue().equals("true"))
                                button_follow_user_activity.setText("UnFollow");
                            else
                                button_follow_user_activity.setText("Follow");
                        }
                    }

                } else {


                }
                button_follow_user_activity.setEnabled(true);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                button_follow_user_activity.setEnabled(true);
            }
        });
    }

    public void addLike(Integer id) {
        removeReaction(prefManager.getString("reaction_" + id));
        reaction_count++;

        linear_layout_reactions_loading.setVisibility(View.VISIBLE);
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<Integer> call = service.imageAddLike(id);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                if (response.isSuccessful()) {
                    like = response.body();
                    text_view_like_activity_quote.setText(format(like));
                    setReaction("like");
                }
                linear_layout_reactions_loading.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                linear_layout_reactions_loading.setVisibility(View.GONE);

            }
        });
    }

    public void addLove(Integer id) {
        removeReaction(prefManager.getString("reaction_" + id));
        linear_layout_reactions_loading.setVisibility(View.VISIBLE);
        reaction_count++;

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<Integer> call = service.imageAddLove(id);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                if (response.isSuccessful()) {
                    love = response.body();
                    text_view_love_activity_quote.setText(format(love));
                    setReaction("love");
                }
                linear_layout_reactions_loading.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                linear_layout_reactions_loading.setVisibility(View.GONE);

            }
        });
    }

    public void addSad(Integer id) {
        removeReaction(prefManager.getString("reaction_" + id));
        linear_layout_reactions_loading.setVisibility(View.VISIBLE);
        reaction_count++;

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<Integer> call = service.imageAddSad(id);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                if (response.isSuccessful()) {
                    sad = response.body();
                    text_view_sad_activity_quote.setText(format(sad));
                    setReaction("sad");
                }
                linear_layout_reactions_loading.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                linear_layout_reactions_loading.setVisibility(View.GONE);

            }
        });
    }

    public void addAngry(Integer id) {
        removeReaction(prefManager.getString("reaction_" + id));
        linear_layout_reactions_loading.setVisibility(View.VISIBLE);
        reaction_count++;
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<Integer> call = service.imageAddAngry(id);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                if (response.isSuccessful()) {
                    angry = response.body();
                    text_view_angry_activity_quote.setText(format(angry));
                    setReaction("angry");
                }
                linear_layout_reactions_loading.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                linear_layout_reactions_loading.setVisibility(View.GONE);

            }
        });
    }

    public void addHaha(Integer id) {
        linear_layout_reactions_loading.setVisibility(View.VISIBLE);

        removeReaction(prefManager.getString("reaction_" + id));
        reaction_count++;

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<Integer> call = service.imageAddHaha(id);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                if (response.isSuccessful()) {
                    haha = response.body();
                    text_view_haha_activity_quote.setText(format(haha));
                    setReaction("haha");
                }
                linear_layout_reactions_loading.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                linear_layout_reactions_loading.setVisibility(View.GONE);

            }
        });
    }

    public void addWoow(Integer id) {
        linear_layout_reactions_loading.setVisibility(View.VISIBLE);

        removeReaction(prefManager.getString("reaction_" + id));
        reaction_count++;

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<Integer> call = service.imageAddWoow(id);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                if (response.isSuccessful()) {
                    woow = response.body();
                    text_view_woow_activity_quote.setText(format(woow));
                    setReaction("woow");
                }
                linear_layout_reactions_loading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                linear_layout_reactions_loading.setVisibility(View.GONE);
            }
        });
    }

    public void deleteWoow(Integer id) {
        reaction_count--;

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<Integer> call = service.imageDeleteWoow(id);
        linear_layout_reactions_loading.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                if (response.isSuccessful()) {
                    woow = response.body();
                    text_view_woow_activity_quote.setText(format(woow));
                }
                linear_layout_reactions_loading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                linear_layout_reactions_loading.setVisibility(View.GONE);
            }
        });
    }

    public void deleteLike(Integer id) {
        reaction_count--;
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        linear_layout_reactions_loading.setVisibility(View.VISIBLE);

        Call<Integer> call = service.imageDeleteLike(id);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                if (response.isSuccessful()) {
                    like = response.body();
                    text_view_like_activity_quote.setText(format(like));
                }
                linear_layout_reactions_loading.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                linear_layout_reactions_loading.setVisibility(View.GONE);

            }
        });
    }

    public void deleteAngry(Integer id) {
        reaction_count--;
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        linear_layout_reactions_loading.setVisibility(View.VISIBLE);

        Call<Integer> call = service.imageDeleteAngry(id);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                if (response.isSuccessful()) {
                    angry = response.body();
                    text_view_angry_activity_quote.setText(format(angry));
                }
                linear_layout_reactions_loading.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                linear_layout_reactions_loading.setVisibility(View.GONE);

            }
        });
    }

    public void deleteHaha(Integer id) {
        reaction_count--;
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        linear_layout_reactions_loading.setVisibility(View.VISIBLE);

        Call<Integer> call = service.imageDeleteHaha(id);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                if (response.isSuccessful()) {
                    haha = response.body();
                    text_view_haha_activity_quote.setText(format(haha));
                }
                linear_layout_reactions_loading.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                linear_layout_reactions_loading.setVisibility(View.GONE);

            }
        });
    }

    public void deleteSad(Integer id) {
        reaction_count--;

        linear_layout_reactions_loading.setVisibility(View.VISIBLE);

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<Integer> call = service.imageDeleteSad(id);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                if (response.isSuccessful()) {
                    sad = response.body();
                    text_view_sad_activity_quote.setText(format(sad));
                }
                linear_layout_reactions_loading.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                linear_layout_reactions_loading.setVisibility(View.GONE);

            }
        });
    }

    public void deleteLove(Integer id) {
        reaction_count--;

        linear_layout_reactions_loading.setVisibility(View.VISIBLE);

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<Integer> call = service.imageDeleteLove(id);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                if (response.isSuccessful()) {
                    love = response.body();
                    text_view_love_activity_quote.setText(format(love));
                }
                linear_layout_reactions_loading.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                linear_layout_reactions_loading.setVisibility(View.GONE);

            }
        });
    }

    public void setReaction(String reaction) {
        text_view_like_activity_quote.setBackground(getResources().getDrawable(R.drawable.bg_card_count));
        text_view_love_activity_quote.setBackground(getResources().getDrawable(R.drawable.bg_card_count));
        text_view_angry_activity_quote.setBackground(getResources().getDrawable(R.drawable.bg_card_count));
        text_view_sad_activity_quote.setBackground(getResources().getDrawable(R.drawable.bg_card_count));
        text_view_haha_activity_quote.setBackground(getResources().getDrawable(R.drawable.bg_card_count));
        text_view_woow_activity_quote.setBackground(getResources().getDrawable(R.drawable.bg_card_count));

        text_view_like_activity_quote.setTextColor(getResources().getColor(R.color.primary_text));
        text_view_woow_activity_quote.setTextColor(getResources().getColor(R.color.primary_text));
        text_view_love_activity_quote.setTextColor(getResources().getColor(R.color.primary_text));
        text_view_sad_activity_quote.setTextColor(getResources().getColor(R.color.primary_text));
        text_view_angry_activity_quote.setTextColor(getResources().getColor(R.color.primary_text));
        text_view_haha_activity_quote.setTextColor(getResources().getColor(R.color.primary_text));

        like_button_like_activity_quote.setLiked(false);
        like_button_love_activity_quote.setLiked(false);
        like_button_angry_activity_quote.setLiked(false);
        like_button_haha_activity_quote.setLiked(false);
        like_button_sad_activity_quote.setLiked(false);
        like_button_woow_activity_quote.setLiked(false);


        if (reaction.equals("like")) {
            prefManager.setString("reaction_" + id, "like");
            text_view_like_activity_quote.setBackground(getResources().getDrawable(R.drawable.bg_card_count_select));
            text_view_like_activity_quote.setTextColor(getResources().getColor(R.color.white));
            like_button_like_activity_quote.setLiked(true);

        } else if (reaction.equals("woow")) {
            prefManager.setString("reaction_" + id, "woow");
            text_view_woow_activity_quote.setBackground(getResources().getDrawable(R.drawable.bg_card_count_select));
            text_view_woow_activity_quote.setTextColor(getResources().getColor(R.color.white));
            like_button_woow_activity_quote.setLiked(true);

        } else if (reaction.equals("love")) {
            prefManager.setString("reaction_" + id, "love");
            text_view_love_activity_quote.setBackground(getResources().getDrawable(R.drawable.bg_card_count_select));
            text_view_love_activity_quote.setTextColor(getResources().getColor(R.color.white));
            like_button_love_activity_quote.setLiked(true);

        } else if (reaction.equals("angry")) {
            prefManager.setString("reaction_" + id, "angry");
            text_view_angry_activity_quote.setBackground(getResources().getDrawable(R.drawable.bg_card_count_select));
            text_view_angry_activity_quote.setTextColor(getResources().getColor(R.color.white));
            like_button_angry_activity_quote.setLiked(true);

        } else if (reaction.equals("sad")) {
            prefManager.setString("reaction_" + id, "sad");
            text_view_sad_activity_quote.setBackground(getResources().getDrawable(R.drawable.bg_card_count_select));
            text_view_sad_activity_quote.setTextColor(getResources().getColor(R.color.white));
            like_button_sad_activity_quote.setLiked(true);

        } else if (reaction.equals("haha")) {
            prefManager.setString("reaction_" + id, "haha");
            text_view_haha_activity_quote.setBackground(getResources().getDrawable(R.drawable.bg_card_count_select));
            text_view_haha_activity_quote.setTextColor(getResources().getColor(R.color.white));
            like_button_haha_activity_quote.setLiked(true);
        }
        this.text_view_reaction_count.setText(format(reaction_count));
        this.text_view_super_like_count.setText(format(superLikeCount));

    }

    public void removeReaction(String reaction) {
        if (reaction.equals("like")) {
            deleteLike(id);
        } else if (reaction.equals("woow")) {
            deleteWoow(id);
        } else if (reaction.equals("love")) {
            deleteLove(id);
        } else if (reaction.equals("angry")) {
            deleteAngry(id);
        } else if (reaction.equals("sad")) {
            deleteSad(id);
        } else if (reaction.equals("haha")) {
            deleteHaha(id);
        }
    }

    public void addShare(Integer id) {
        final PrefManager prefManager = new PrefManager(this);
        Integer id_user = 0;
        String key_user = "";
        if (prefManager.getString("LOGGED").toString().equals("TRUE")) {
            id_user = Integer.parseInt(prefManager.getString("ID_USER"));
            key_user = prefManager.getString("TOKEN_USER");
        }
        if (!prefManager.getString(id + "_share").equals("true")) {
            prefManager.setString(id + "_share", "true");
            Retrofit retrofit = apiClient.getClient();
            apiRest service = retrofit.create(apiRest.class);
            Call<Integer> call = service.addShare(id, id_user, key_user);
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {

                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {

                }
            });
        }
    }

    public void addView() {
        final PrefManager prefManager = new PrefManager(this);
        Integer id_user = 0;
        String key_user = "";
        if (prefManager.getString("LOGGED").toString().equals("TRUE")) {
            id_user = Integer.parseInt(prefManager.getString("ID_USER"));
            key_user = prefManager.getString("TOKEN_USER");
        }
        if (!prefManager.getString(id + "_view").equals("true")) {
            prefManager.setString(id + "_view", "true");
            Retrofit retrofit = apiClient.getClient();
            apiRest service = retrofit.create(apiRest.class);
            String ishash = "";
            try {
                ishash = VideoActivity.sha1((id + 55463938) + "");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            ;
            Call<Integer> call = service.addView(ishash, id_user, key_user);
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, retrofit2.Response<Integer> response) {
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                }
            });
        }
    }

    public void initAds() {
        if (prefManager.getString("SUBSCRIBED").equals("TRUE"))
            return;
        if (!getResources().getString(R.string.FACEBOOK_ADS_ENABLED_BANNER).equals("true"))
            return;
        // Instantiate a NativeBannerAd object.
        // NOTE: the placement ID will eventually identify this as your App, you can ignore it for
        // now, while you are testing and replace it later when you have signed up.
        // While you are using this temporary code you will only get test ads and if you release
        // your code like this to the Google Play your users will not receive ads (you will get a no fill error).
        nativeBannerAd = new NativeBannerAd(QuoteActivity.this, getResources().getString(R.string.FACEBOOK_ADS_NATIVE_BANNER_PLACEMENT_ID));
        nativeBannerAd.setAdListener(new NativeAdListener() {
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
                // Race condition, load() called again before last ad was displayed
                if (nativeBannerAd == null || nativeBannerAd != ad) {
                    return;
                }
                // Inflate Native Banner Ad into Container
                inflateAd(nativeBannerAd);
                Log.d(TAG, "Native ad is loaded and ready to be displayed!");
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
        // load the ad
        nativeBannerAd.loadAd();
    }

    private void inflateAd(NativeBannerAd nativeBannerAd) {
        // Unregister last ad
        nativeBannerAd.unregisterView();

        // Add the Ad view into the ad container.
        nativeBannerAdContainer = findViewById(R.id.native_banner_ad_container);
        LayoutInflater inflater = LayoutInflater.from(QuoteActivity.this);
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        adView = (LinearLayout) inflater.inflate(R.layout.native_banner_ad_layout, nativeBannerAdContainer, false);
        nativeBannerAdContainer.addView(adView);

        // Add the AdChoices icon (NativeBannerAdActivity.this, nativeBannerAd, true);
        RelativeLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdChoicesView adChoicesView = new AdChoicesView(QuoteActivity.this.getApplicationContext(), nativeBannerAd, true);
        adChoicesContainer.addView(adChoicesView, 0);

        // Create native UI using the ad metadata.
        TextView nativeAdTitle = findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        AdIconView nativeAdIconView = adView.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews);
    }

    private void loadMore() {
        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<List<Status>> call = service.ImageByRandom(language);
        call.enqueue(new Callback<List<Status>>() {
            @Override
            public void onResponse(Call<List<Status>> call, Response<List<Status>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() != 0) {
                        statusList.clear();
                        for (int i = 0; i < response.body().size(); i++) {
                            statusList.add(response.body().get(i));
                        }
                        statusAdapter.notifyDataSetChanged();
                        recycler_view_status_load_more.setNestedScrollingEnabled(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Status>> call, Throwable t) {

            }
        });
    }

    private class CommentTextWatcher implements TextWatcher {
        private View view;

        private CommentTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edit_text_comment_add:
                    ValidateComment();
                    break;
            }
        }
    }
}
