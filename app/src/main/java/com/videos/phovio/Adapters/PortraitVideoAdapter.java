package com.videos.phovio.Adapters;

import android.app.Activity;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.squareup.picasso.Picasso;
import com.videos.phovio.R;
import com.videos.phovio.model.Status;
import com.videos.phovio.ui.Activities.PlayerActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PortraitVideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Status> statusList =new ArrayList<>();
    private Activity activity;

    public PortraitVideoAdapter(List<Status> statusList, Activity activity) {
        this.statusList = statusList;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case 1: {
                View v1 = inflater.inflate(R.layout.item_portrait_video, null);
                viewHolder = new PortraitVideoHolder(v1);
                break;
            }
            case 2: {
                View v2 = inflater.inflate(R.layout.item_portrait_video_full, null);
                viewHolder = new PortraitVideoHolderFull(v2);
                break;
            }
            case 6:{
                View v6 = inflater.inflate(R.layout.item_facebook_ads, parent, false);
                viewHolder = new FacebookNativeHolder(v6);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
            switch (statusList.get(position).getViewType()){
                case 1:
                    PortraitVideoHolder portraitVideoHolder = (PortraitVideoHolder) holder;
                    if (statusList.get(position).getReview()){
                        portraitVideoHolder.relative_layout_item_video_review.setVisibility(View.VISIBLE);
                    }else {
                        portraitVideoHolder.relative_layout_item_video_review.setVisibility(View.GONE);
                    }
                    Picasso.with(activity).load(statusList.get(position).getThumbnail()).into(portraitVideoHolder.image_view_item_category_status);
                    Picasso.with(activity).load(statusList.get(position).getUserimage()).into(portraitVideoHolder.circle_image_view_item_gif_user);
                    portraitVideoHolder.text_view_item_gif_name_user.setText(statusList.get(position).getUser());
                    portraitVideoHolder.image_view_item_category_status.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent  = new Intent(activity,PlayerActivity.class);
                            intent.putExtra("id",statusList.get(position).getId());
                            intent.putExtra("title",statusList.get(position).getTitle());
                            intent.putExtra("kind",statusList.get(position).getKind());
                            intent.putExtra("description",statusList.get(position).getDescription());
                            intent.putExtra("review",statusList.get(position).getReview());
                            intent.putExtra("comment",statusList.get(position).getComment());
                            intent.putExtra("comments",statusList.get(position).getComments());
                            intent.putExtra("downloads",statusList.get(position).getDownloads());
                            intent.putExtra("views",statusList.get(position).getViews());
                            intent.putExtra("font",statusList.get(position).getFont());

                            intent.putExtra("user",statusList.get(position).getUser());
                            intent.putExtra("userid",statusList.get(position).getUserid());
                            intent.putExtra("userimage",statusList.get(position).getUserimage());
                            intent.putExtra("thumbnail",statusList.get(position).getThumbnail());
                            intent.putExtra("original",statusList.get(position).getOriginal());
                            intent.putExtra("type",statusList.get(position).getType());
                            intent.putExtra("extension",statusList.get(position).getExtension());
                            intent.putExtra("color",statusList.get(position).getColor());
                            intent.putExtra("created",statusList.get(position).getCreated());
                            intent.putExtra("tags",statusList.get(position).getTags());
                            intent.putExtra("like",statusList.get(position).getLike());
                            intent.putExtra("love",statusList.get(position).getLove());
                            intent.putExtra("woow",statusList.get(position).getWoow());
                            intent.putExtra("angry",statusList.get(position).getAngry());
                            intent.putExtra("sad",statusList.get(position).getSad());
                            intent.putExtra("haha",statusList.get(position).getHaha());
                            intent.putExtra("local",statusList.get(position).getLocal());

                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                        }
                    });

                    break;
                case 2:
                    PortraitVideoHolderFull portraitVideoHolderfull = (PortraitVideoHolderFull) holder;
                    if (statusList.get(position).getReview()){
                        portraitVideoHolderfull.relative_layout_item_video_review.setVisibility(View.VISIBLE);
                    }else {
                        portraitVideoHolderfull.relative_layout_item_video_review.setVisibility(View.GONE);
                    }
                    Picasso.with(activity).load(statusList.get(position).getThumbnail()).into(portraitVideoHolderfull.image_view_item_category_status);
                    Picasso.with(activity).load(statusList.get(position).getUserimage()).into(portraitVideoHolderfull.circle_image_view_item_gif_user);
                    portraitVideoHolderfull.text_view_item_gif_name_user.setText(statusList.get(position).getUser());
                    portraitVideoHolderfull.image_view_item_category_status.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent  = new Intent(activity,PlayerActivity.class);
                            intent.putExtra("id",statusList.get(position).getId());
                            intent.putExtra("title",statusList.get(position).getTitle());
                            intent.putExtra("kind",statusList.get(position).getKind());
                            intent.putExtra("description",statusList.get(position).getDescription());
                            intent.putExtra("review",statusList.get(position).getReview());
                            intent.putExtra("comment",statusList.get(position).getComment());
                            intent.putExtra("comments",statusList.get(position).getComments());
                            intent.putExtra("downloads",statusList.get(position).getDownloads());
                            intent.putExtra("views",statusList.get(position).getViews());
                            intent.putExtra("font",statusList.get(position).getFont());

                            intent.putExtra("user",statusList.get(position).getUser());
                            intent.putExtra("userid",statusList.get(position).getUserid());
                            intent.putExtra("userimage",statusList.get(position).getUserimage());
                            intent.putExtra("thumbnail",statusList.get(position).getThumbnail());
                            intent.putExtra("original",statusList.get(position).getOriginal());
                            intent.putExtra("type",statusList.get(position).getType());
                            intent.putExtra("extension",statusList.get(position).getExtension());
                            intent.putExtra("color",statusList.get(position).getColor());
                            intent.putExtra("created",statusList.get(position).getCreated());
                            intent.putExtra("tags",statusList.get(position).getTags());
                            intent.putExtra("like",statusList.get(position).getLike());
                            intent.putExtra("love",statusList.get(position).getLove());
                            intent.putExtra("woow",statusList.get(position).getWoow());
                            intent.putExtra("angry",statusList.get(position).getAngry());
                            intent.putExtra("sad",statusList.get(position).getSad());
                            intent.putExtra("haha",statusList.get(position).getHaha());
                            intent.putExtra("local",statusList.get(position).getLocal());

                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                        }
                    });

                    break;
            }




    }

    public static class PortraitVideoHolder extends RecyclerView.ViewHolder {
        private final TextView text_view_item_gif_name_user;
        private ImageView image_view_item_category_status;
        private CircleImageView circle_image_view_item_gif_user;
        private final RelativeLayout relative_layout_item_video_review;

        public PortraitVideoHolder(View view) {
            super(view);
            this.relative_layout_item_video_review = (RelativeLayout) view.findViewById(R.id.relative_layout_item_video_review);

            this.image_view_item_category_status = (ImageView) view.findViewById(R.id.image_view_item_category_status);
            this.circle_image_view_item_gif_user = (CircleImageView) view.findViewById(R.id.circle_image_view_item_gif_user);
            this.text_view_item_gif_name_user = (TextView) view.findViewById(R.id.text_view_item_gif_name_user);
        }
    }
    public static class PortraitVideoHolderFull extends RecyclerView.ViewHolder {
        private final TextView text_view_item_gif_name_user;
        private final RelativeLayout relative_layout_item_video_review;
        private ImageView image_view_item_category_status;
        private CircleImageView circle_image_view_item_gif_user;

        public PortraitVideoHolderFull(View view) {
            super(view);
            this.relative_layout_item_video_review = (RelativeLayout) view.findViewById(R.id.relative_layout_item_video_review);
            this.image_view_item_category_status = (ImageView) view.findViewById(R.id.image_view_item_category_status);
            this.circle_image_view_item_gif_user = (CircleImageView) view.findViewById(R.id.circle_image_view_item_gif_user);
            this.text_view_item_gif_name_user = (TextView) view.findViewById(R.id.text_view_item_gif_name_user);
        }
    }
    @Override
    public int getItemCount() {
        return statusList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return  statusList.get(position).getViewType();
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

    /**
     * Populates a {@link UnifiedNativeAdView} object with data from a given
     * {@link UnifiedNativeAd}.
     *
     * @param nativeAd the object containing the ad's assets
     * @param adView          the view to be populated
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
    public  class FacebookNativeHolder extends  RecyclerView.ViewHolder {
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
            nativeAd = new NativeAd(activity,activity.getString(R.string.FACEBOOK_ADS_NATIVE_PLACEMENT_ID));
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
                    inflateAd(nativeAd,view);
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

        private void inflateAd(NativeAd nativeAd,View view) {

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
}
