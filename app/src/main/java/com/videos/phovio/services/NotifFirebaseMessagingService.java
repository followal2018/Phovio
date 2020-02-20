package com.videos.phovio.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.videos.phovio.Provider.PrefManager;
import com.videos.phovio.R;
import com.videos.phovio.ui.Activities.CategoryActivity;
import com.videos.phovio.ui.Activities.GifActivity;
import com.videos.phovio.ui.Activities.ImageActivity;
import com.videos.phovio.ui.Activities.MainActivity;
import com.videos.phovio.ui.Activities.PayoutsActivity;
import com.videos.phovio.ui.Activities.PlayerActivity;
import com.videos.phovio.ui.Activities.QuoteActivity;
import com.videos.phovio.ui.Activities.UserActivity;
import com.videos.phovio.ui.Activities.VideoActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Tamim on 26/10/2017.
 */

public class NotifFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    Bitmap bitmap;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String type = remoteMessage.getData().get("type");
        String id = remoteMessage.getData().get("id");
        String title = remoteMessage.getData().get("title");
        String image = remoteMessage.getData().get("image");
        String icon = remoteMessage.getData().get("icon");
        String message = remoteMessage.getData().get("message");
        PrefManager prf = new PrefManager(getApplicationContext());
        Log.v(TAG, type);
        Log.e(TAG, remoteMessage.getData().toString());

        if (!prf.getString("notifications").equals("false")) {
            if (type.equals("status")) {
                String kind = remoteMessage.getData().get("kind");
                String status_id = remoteMessage.getData().get("id");
                String status_title = remoteMessage.getData().get("status_title");
                String status_description = remoteMessage.getData().get("status_description");
                String status_review = remoteMessage.getData().get("status_review");
                String status_comment = remoteMessage.getData().get("status_comment");
                String status_comments = remoteMessage.getData().get("status_comments");
                String status_downloads = remoteMessage.getData().get("status_downloads");
                String status_views = remoteMessage.getData().get("status_views");
                String status_user = remoteMessage.getData().get("status_user");
                String status_userid = remoteMessage.getData().get("status_userid");
                String status_font = remoteMessage.getData().get("status_font");
                String status_userimage = remoteMessage.getData().get("status_userimage");
                String status_type = remoteMessage.getData().get("status_type");
                String status_extension = remoteMessage.getData().get("status_extension");
                String status_thumbnail = remoteMessage.getData().get("status_thumbnail");
                String status_original = remoteMessage.getData().get("status_original");
                String status_color = remoteMessage.getData().get("status_color");
                String status_created = remoteMessage.getData().get("status_created");
                String status_tags = remoteMessage.getData().get("status_tags");
                String status_like = remoteMessage.getData().get("status_like");
                String status_love = remoteMessage.getData().get("status_love");
                String status_woow = remoteMessage.getData().get("status_woow");
                String status_angry = remoteMessage.getData().get("status_angry");
                String status_sad = remoteMessage.getData().get("status_sad");
                String status_haha = remoteMessage.getData().get("status_haha");

                sendStatusNotification(id, title, image, icon, message, kind, status_id, status_title, status_description, status_review, status_comment, status_comments, status_downloads, status_views, status_user, status_userid, status_font, status_type, status_extension, status_thumbnail, status_original, status_color, status_userimage, status_created, status_tags, status_like, status_love, status_woow, status_angry, status_sad, status_haha);
            } else if (type.equals("link")) {
                String link = remoteMessage.getData().get("link");
                sendNotificationUrl(
                        id,
                        title,
                        image,
                        icon,
                        message,
                        link
                );
            } else if (type.equals("category")) {
                String category_title = remoteMessage.getData().get("title_category");
                String category_image = remoteMessage.getData().get("status_category");

                sendNotificationCategory(
                        id,
                        title,
                        image,
                        icon,
                        message,
                        category_title,
                        category_image);
            } else if (type.equals("payment")) {
                Log.v(TAG, type);

                sendNotificationPayment(
                        id,
                        title,
                        image,
                        icon,
                        message);

            } else if (type.equals("user")) {
                String name_user = remoteMessage.getData().get("name_user");
                String image_user = remoteMessage.getData().get("image_user");

                sendNotificationUser(
                        id,
                        title,
                        image,
                        icon,
                        message,
                        name_user,
                        image_user);
            } else if (type.equals("Creator User")) {
                String name_user = remoteMessage.getData().get("name_user");
                String image_user = remoteMessage.getData().get("image_user");

                sendNotificationcreator(
                        id,
                        title,
                        image,
                        icon,
                        message,
                        name_user,
                        image_user);
            }
        }
    }


    private void sendNotificationcreator(
            String id,
            String title,
            String imageUri,
            String iconUrl,
            String message,
            String name_user,
            String image_user
    )
    {


        Bitmap image = getBitmapfromUrl(imageUri);
        Bitmap icon = getBitmapfromUrl(iconUrl);
        Intent intent = new Intent(this, UserActivity.class);
        intent.setAction(Long.toString(System.currentTimeMillis()));


        intent.putExtra("id", Integer.parseInt(id));
        intent.putExtra("name", "");
        intent.putExtra("image", image_user);
        intent.putExtra("from", "notification");

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        int NOTIFICATION_ID = Integer.parseInt(id);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message);

        if (icon != null) {
            builder.setLargeIcon(icon);

        } else {
            builder.setLargeIcon(largeIcon);
        }
        if (image != null) {
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image));
        }


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }


    private void sendNotificationUser(
            String id,
            String title,
            String imageUri,
            String iconUrl,
            String message,
            String name_user,
            String image_user
    )
    {


        Bitmap image = getBitmapfromUrl(imageUri);
        Bitmap icon = getBitmapfromUrl(iconUrl);
        Intent intent = new Intent(this, UserActivity.class);
        intent.setAction(Long.toString(System.currentTimeMillis()));


        intent.putExtra("id", Integer.parseInt(id));
        intent.putExtra("name", name_user);
        intent.putExtra("image", image_user);
        intent.putExtra("from", "notification");

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        int NOTIFICATION_ID = Integer.parseInt(id);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message);

        if (icon != null) {
            builder.setLargeIcon(icon);

        } else {
            builder.setLargeIcon(largeIcon);
        }
        if (image != null) {
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image));
        }


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

    private void sendNotificationCategory(
            String id,
            String title,
            String imageUri,
            String iconUrl,
            String message,
            String category_title,
            String category_image
    ) {


        Bitmap image = getBitmapfromUrl(imageUri);
        Bitmap icon = getBitmapfromUrl(iconUrl);
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.setAction(Long.toString(System.currentTimeMillis()));


        intent.putExtra("id", Integer.parseInt(id));
        intent.putExtra("title", category_title);
        intent.putExtra("image", category_image);
        intent.putExtra("from", "notification");


        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        int NOTIFICATION_ID = Integer.parseInt(id);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_w)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message);

        if (icon != null) {
            builder.setLargeIcon(icon);

        } else {
            builder.setLargeIcon(largeIcon);
        }
        if (image != null) {
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image));
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());


    }

    private void sendNotificationPayment(
            String id,
            String title,
            String imageUri,
            String iconUrl,
            String message
    ) {


        Log.v(TAG, message);

        Bitmap image = getBitmapfromUrl(imageUri);
        Bitmap icon = getBitmapfromUrl(iconUrl);
        Intent intent = new Intent(this, PayoutsActivity.class);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        intent.putExtra("from", "notification");


        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        int NOTIFICATION_ID = Integer.parseInt(id);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_w)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message);

        if (icon != null) {
            builder.setLargeIcon(icon);

        } else {
            builder.setLargeIcon(largeIcon);
        }
        if (image != null) {
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image));
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());


    }

    private void sendNotificationUrl(
            String id,
            String title,
            String imageUri,
            String iconUrl,
            String message,
            String url

    ) {


        Bitmap image = getBitmapfromUrl(imageUri);
        Bitmap icon = getBitmapfromUrl(iconUrl);


        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
        notificationIntent.setData(Uri.parse(url));
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        int NOTIFICATION_ID = Integer.parseInt(id);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_w)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message);

        if (icon != null) {
            builder.setLargeIcon(icon);

        } else {
            builder.setLargeIcon(largeIcon);
        }
        if (image != null) {
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image));
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());


    }


    private void sendStatusNotification(
            String id,
            String title,
            String imageUri,
            String iconUrl,
            String message,
            String kind,
            String status_id,
            String status_title,
            String status_description,
            String status_review,
            String status_comment,
            String status_comments,
            String status_downloads,
            String status_views,
            String status_user,
            String status_userid,
            String status_font,
            String status_type,
            String status_extension,
            String status_thumbnail,
            String status_original,
            String status_color,
            String status_userimage,
            String status_created,
            String status_tags,
            String status_like,
            String status_love,
            String status_woow,
            String status_angry,
            String status_sad,
            String status_haha

    ) {
        Bitmap image = getBitmapfromUrl(imageUri);
        Bitmap icon = getBitmapfromUrl(iconUrl);
        Intent intent = new Intent(this, VideoActivity.class);

        if (kind.equals("video")) {
            intent = new Intent(this, VideoActivity.class);
        } else if (kind.equals("quote")) {
            intent = new Intent(this, QuoteActivity.class);

        } else if (kind.equals("gif")) {
            intent = new Intent(this, GifActivity.class);

        } else if (kind.equals("image")) {
            intent = new Intent(this, ImageActivity.class);

        } else if (kind.equals("fullscreen")) {
            intent = new Intent(this, PlayerActivity.class);
        }
        intent.setAction(Long.toString(System.currentTimeMillis()));


        Log.v(TAG, kind);
        Log.v(TAG, status_userid);
        Log.v(TAG, status_userid);
        intent.putExtra("from", "notification");
        intent.putExtra("id", Integer.parseInt(status_id));
        intent.putExtra("title", status_title);
        intent.putExtra("kind", kind);
        intent.putExtra("description", status_description);

        if (status_review.equals("true"))
            intent.putExtra("review", true);
        else
            intent.putExtra("review", false);

        if (status_comment.equals("true"))
            intent.putExtra("comment", true);
        else
            intent.putExtra("comment", false);

        intent.putExtra("comments", Integer.parseInt(status_comments));
        intent.putExtra("downloads", Integer.parseInt(status_downloads));
        intent.putExtra("views", Integer.parseInt(status_views));
        intent.putExtra("user", status_user);
        intent.putExtra("userid", Integer.parseInt(status_userid));
        intent.putExtra("font", Integer.parseInt(status_font));
        intent.putExtra("userimage", status_userimage);
        intent.putExtra("thumbnail", status_thumbnail);
        intent.putExtra("original", status_original);
        intent.putExtra("type", status_type);
        intent.putExtra("extension", status_extension);
        intent.putExtra("color", status_color);
        intent.putExtra("created", status_created);
        intent.putExtra("tags", status_tags);
        intent.putExtra("like", Integer.parseInt(status_like));
        intent.putExtra("love", Integer.parseInt(status_love));
        intent.putExtra("woow", Integer.parseInt(status_woow));
        intent.putExtra("angry", Integer.parseInt(status_angry));
        intent.putExtra("sad", Integer.parseInt(status_sad));
        intent.putExtra("haha", Integer.parseInt(status_haha));


        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        int NOTIFICATION_ID = Integer.parseInt(id);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_w)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message);

        if (icon != null) {
            builder.setLargeIcon(icon);

        } else {
            builder.setLargeIcon(largeIcon);
        }
        if (image != null) {
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image));
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }


    /*
     *To get a Bitmap image from the URL received
     * */
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}