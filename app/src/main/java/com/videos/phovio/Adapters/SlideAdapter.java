package com.videos.phovio.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.videos.phovio.R;
import com.videos.phovio.model.Slide;
import com.videos.phovio.ui.Activities.CategoryActivity;
import com.videos.phovio.ui.Activities.GifActivity;
import com.videos.phovio.ui.Activities.ImageActivity;
import com.videos.phovio.ui.Activities.QuoteActivity;
import com.videos.phovio.ui.Activities.VideoActivity;


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hsn on 28/11/2017.
 */

public class SlideAdapter extends PagerAdapter {
    private List<Slide> slideList =new ArrayList<Slide>();
    private Activity activity;

    public SlideAdapter(Activity activity, List<Slide> stringList) {
        this.slideList = stringList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return slideList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater)this.activity.getSystemService(this.activity.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.item_slide_one, container, false);

        TextView text_view_item_slide_one_title =  (TextView)  view.findViewById(R.id.text_view_item_slide_one_title);
        ImageView image_view_item_slide_one =  (ImageView)  view.findViewById(R.id.image_view_item_slide_one);

        Typeface face = Typeface.createFromAsset(activity.getAssets(), "Pattaya-Regular.ttf");
        text_view_item_slide_one_title.setTypeface(face);

        byte[] data = android.util.Base64.decode(slideList.get(position).getTitle(), android.util.Base64.DEFAULT);
        final String final_text = new String(data, Charset.forName("UTF-8"));

        text_view_item_slide_one_title.setText(final_text);

        CardView card_view_item_slide_one = (CardView) view.findViewById(R.id.card_view_item_slide_one);
        card_view_item_slide_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slideList.get(position).getType().equals("3") && slideList.get(position).getStatus()!=null){
                    if (slideList.get(position).getStatus().getKind().equals("video")) {
                        Intent intent = new Intent(activity, VideoActivity.class);
                        intent.putExtra("id", slideList.get(position).getStatus().getId());
                        intent.putExtra("title", slideList.get(position).getStatus().getTitle());
                        intent.putExtra("kind", slideList.get(position).getStatus().getKind());
                        intent.putExtra("description", slideList.get(position).getStatus().getDescription());
                        intent.putExtra("review", slideList.get(position).getStatus().getReview());
                        intent.putExtra("comment", slideList.get(position).getStatus().getComment());
                        intent.putExtra("comments", slideList.get(position).getStatus().getComments());
                        intent.putExtra("downloads", slideList.get(position).getStatus().getDownloads());
                        intent.putExtra("views", slideList.get(position).getStatus().getViews());
                        intent.putExtra("user", slideList.get(position).getStatus().getUser());
                        intent.putExtra("userid", slideList.get(position).getStatus().getUserid());
                        intent.putExtra("userimage", slideList.get(position).getStatus().getUserimage());
                        intent.putExtra("thumbnail", slideList.get(position).getStatus().getThumbnail());
                        intent.putExtra("original", slideList.get(position).getStatus().getOriginal());
                        intent.putExtra("type", slideList.get(position).getStatus().getType());
                        intent.putExtra("extension", slideList.get(position).getStatus().getExtension());
                        intent.putExtra("color", slideList.get(position).getStatus().getColor());
                        intent.putExtra("created", slideList.get(position).getStatus().getCreated());
                        intent.putExtra("tags", slideList.get(position).getStatus().getTags());
                        intent.putExtra("like", slideList.get(position).getStatus().getLike());
                        intent.putExtra("love", slideList.get(position).getStatus().getLove());
                        intent.putExtra("woow", slideList.get(position).getStatus().getWoow());
                        intent.putExtra("angry", slideList.get(position).getStatus().getAngry());
                        intent.putExtra("sad", slideList.get(position).getStatus().getSad());
                        intent.putExtra("haha", slideList.get(position).getStatus().getHaha());
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                    }else if (slideList.get(position).getStatus().getKind().equals("image")) {
                        Intent intent  = new Intent(activity,ImageActivity.class);
                        intent.putExtra("id",slideList.get(position).getStatus().getId());
                        intent.putExtra("title",slideList.get(position).getStatus().getTitle());
                        intent.putExtra("kind",slideList.get(position).getStatus().getKind());
                        intent.putExtra("description",slideList.get(position).getStatus().getDescription());
                        intent.putExtra("review",slideList.get(position).getStatus().getReview());
                        intent.putExtra("comment",slideList.get(position).getStatus().getComment());
                        intent.putExtra("comments",slideList.get(position).getStatus().getComments());
                        intent.putExtra("downloads",slideList.get(position).getStatus().getDownloads());
                        intent.putExtra("views",slideList.get(position).getStatus().getViews());
                        intent.putExtra("user",slideList.get(position).getStatus().getUser());
                        intent.putExtra("userid",slideList.get(position).getStatus().getUserid());
                        intent.putExtra("userimage",slideList.get(position).getStatus().getUserimage());
                        intent.putExtra("thumbnail",slideList.get(position).getStatus().getThumbnail());
                        intent.putExtra("original",slideList.get(position).getStatus().getOriginal());
                        intent.putExtra("type",slideList.get(position).getStatus().getType());
                        intent.putExtra("extension",slideList.get(position).getStatus().getExtension());
                        intent.putExtra("color",slideList.get(position).getStatus().getColor());
                        intent.putExtra("created",slideList.get(position).getStatus().getCreated());
                        intent.putExtra("tags",slideList.get(position).getStatus().getTags());
                        intent.putExtra("like",slideList.get(position).getStatus().getLike());
                        intent.putExtra("love",slideList.get(position).getStatus().getLove());
                        intent.putExtra("woow",slideList.get(position).getStatus().getWoow());
                        intent.putExtra("angry",slideList.get(position).getStatus().getAngry());
                        intent.putExtra("sad",slideList.get(position).getStatus().getSad());
                        intent.putExtra("haha",slideList.get(position).getStatus().getHaha());
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                    }else if (slideList.get(position).getStatus().getKind().equals("gif")) {
                        Intent intent  = new Intent(activity,GifActivity.class);
                        intent.putExtra("id",slideList.get(position).getStatus().getId());
                        intent.putExtra("title",slideList.get(position).getStatus().getTitle());
                        intent.putExtra("kind",slideList.get(position).getStatus().getKind());
                        intent.putExtra("description",slideList.get(position).getStatus().getDescription());
                        intent.putExtra("review",slideList.get(position).getStatus().getReview());
                        intent.putExtra("comment",slideList.get(position).getStatus().getComment());
                        intent.putExtra("comments",slideList.get(position).getStatus().getComments());
                        intent.putExtra("downloads",slideList.get(position).getStatus().getDownloads());
                        intent.putExtra("views",slideList.get(position).getStatus().getViews());
                        intent.putExtra("user",slideList.get(position).getStatus().getUser());
                        intent.putExtra("userid",slideList.get(position).getStatus().getUserid());
                        intent.putExtra("userimage",slideList.get(position).getStatus().getUserimage());
                        intent.putExtra("thumbnail",slideList.get(position).getStatus().getThumbnail());
                        intent.putExtra("original",slideList.get(position).getStatus().getOriginal());
                        intent.putExtra("type",slideList.get(position).getStatus().getType());
                        intent.putExtra("extension",slideList.get(position).getStatus().getExtension());
                        intent.putExtra("color",slideList.get(position).getStatus().getColor());
                        intent.putExtra("created",slideList.get(position).getStatus().getCreated());
                        intent.putExtra("tags",slideList.get(position).getStatus().getTags());
                        intent.putExtra("like",slideList.get(position).getStatus().getLike());
                        intent.putExtra("love",slideList.get(position).getStatus().getLove());
                        intent.putExtra("woow",slideList.get(position).getStatus().getWoow());
                        intent.putExtra("angry",slideList.get(position).getStatus().getAngry());
                        intent.putExtra("sad",slideList.get(position).getStatus().getSad());
                        intent.putExtra("haha",slideList.get(position).getStatus().getHaha());
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                    }else if (slideList.get(position).getStatus().getKind().equals("quote")) {
                        Intent intent  = new Intent(activity,QuoteActivity.class);
                        intent.putExtra("id",slideList.get(position).getStatus().getId());
                        intent.putExtra("title",slideList.get(position).getStatus().getTitle());
                        intent.putExtra("kind",slideList.get(position).getStatus().getKind());
                        intent.putExtra("description",slideList.get(position).getStatus().getDescription());
                        intent.putExtra("review",slideList.get(position).getStatus().getReview());
                        intent.putExtra("comment",slideList.get(position).getStatus().getComment());
                        intent.putExtra("comments",slideList.get(position).getStatus().getComments());
                        intent.putExtra("downloads",slideList.get(position).getStatus().getDownloads());
                        intent.putExtra("views",slideList.get(position).getStatus().getViews());
                        intent.putExtra("user",slideList.get(position).getStatus().getUser());
                        intent.putExtra("userid",slideList.get(position).getStatus().getUserid());
                        intent.putExtra("userimage",slideList.get(position).getStatus().getUserimage());
                        intent.putExtra("thumbnail",slideList.get(position).getStatus().getThumbnail());
                        intent.putExtra("original",slideList.get(position).getStatus().getOriginal());
                        intent.putExtra("type",slideList.get(position).getStatus().getType());
                        intent.putExtra("font",slideList.get(position).getStatus().getFont());
                        intent.putExtra("extension",slideList.get(position).getStatus().getExtension());
                        intent.putExtra("color",slideList.get(position).getStatus().getColor());
                        intent.putExtra("created",slideList.get(position).getStatus().getCreated());
                        intent.putExtra("tags",slideList.get(position).getStatus().getTags());
                        intent.putExtra("like",slideList.get(position).getStatus().getLike());
                        intent.putExtra("love",slideList.get(position).getStatus().getLove());
                        intent.putExtra("woow",slideList.get(position).getStatus().getWoow());
                        intent.putExtra("angry",slideList.get(position).getStatus().getAngry());
                        intent.putExtra("sad",slideList.get(position).getStatus().getSad());
                        intent.putExtra("haha",slideList.get(position).getStatus().getHaha());
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                    }

                }
                if (slideList.get(position).getType().equals("1") && slideList.get(position).getCategory()!=null){
                    Intent intent  =  new Intent(activity.getApplicationContext(), CategoryActivity.class);
                    intent.putExtra("id",slideList.get(position).getCategory().getId());
                    intent.putExtra("title",slideList.get(position).getCategory().getTitle());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.enter, R.anim.exit);
                }
                if (slideList.get(position).getType().equals("2") && slideList.get(position).getUrl()!=null){
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(slideList.get(position).getUrl()));
                    activity.startActivity(browserIntent);
                }
            }
        });

        Picasso.with(activity).load(slideList.get(position).getImage()).placeholder(R.drawable.placeholder).into(image_view_item_slide_one);

        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public float getPageWidth (int position) {
        return 1f;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);

    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);

    }
    @Override
    public Parcelable saveState() {
        return null;
    }
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
