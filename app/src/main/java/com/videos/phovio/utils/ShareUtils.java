package com.videos.phovio.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.videos.phovio.Provider.PrefManager;
import com.videos.phovio.R;
import com.videos.phovio.api.apiClient;
import com.videos.phovio.api.apiRest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Created by Nirav Mandani on 18-12-2019.
 * Followal Solutions
 */
public class ShareUtils {

    private static final String WHATSAPP_ID = "com.whatsapp";
    private static final String FACEBOOK_ID = "com.facebook.katana";
    private static final String MESSENGER_ID = "com.facebook.orca";
    private static final String INSTAGRAM_ID = "com.instagram.android";
    private static final String SHARE_ID = "com.android.all";
    private static final String DOWNLOAD_ID = "com.android.download";
    private static final String TWITTER_ID = "com.twitter.android";
    private static final String SNAPSHAT_ID = "com.snapchat.android";
    private static final String HIKE_ID = "com.bsb.hike";

    Context context;
    String type;
    int id;
    String path;
    private ProgressDialog dialog;

    public ShareUtils(Context context) {
        Timber.e("ShareUtils :-> " + "ShareUtils");
        this.context = context;
        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait...");
        dialog.setCancelable(false);
    }

    public void shareStatus(int id, String kind, String shareWith, String thumb, String title) {
        Timber.e("ShareUtils :-> " + "shareStatus");
        this.id = id;
        if (kind.equals("quote")) {
            this.type = "text";
            createSharableLink(id, kind, shareWith, null);
        } else {
            this.type = "image";
            new DownloadFileFromURL().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, thumb, title, ".jpg", 0, shareWith, kind);
        }
    }

    public void setDownloading(Boolean downloading) {
        if (dialog != null) {
            if (downloading) {
                dialog.show();
            } else {
                dialog.dismiss();
            }
        }
    }

    public void createSharableLink(int id, String kind, final String shareWith, final String path) {
        Timber.e("ShareUtils :-> " + "createSharableLink");
        String link = "https://phovio.page.link/?statusid=" + id + "&kind=" + kind;
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
                        shareWith(shareWith, path, invitationLink);
                    }
                });
        setDownloading(false);
    }

    public void shareWith(String share_app, String local, String text) {
        Timber.e("ShareUtils :-> " + "shareWith " + share_app);
        switch (share_app) {
            case WHATSAPP_ID:
                shareWhatsapp(text, local);
                break;
            case FACEBOOK_ID:
                shareFacebook(text, local);
                break;
            case MESSENGER_ID:
                shareMessenger(text, local);
                break;
            case INSTAGRAM_ID:
                shareInstagram(text, local);
                break;
            case SHARE_ID:
                share(text, local);
                break;
            case TWITTER_ID:
                shareTwitter(text, local);
                break;
            case SNAPSHAT_ID:
                shareSnapshat(text, local);
                break;
            case HIKE_ID:
                shareHike(text, local);
                break;
           /* case DOWNLOAD_ID:
                download();
                break;*/
        }
    }

    public void addShare(Context context, Integer id) {
        final PrefManager prefManager = new PrefManager(context);
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

    public void shareWhatsapp(String text, String path) {
        Timber.e("ShareUtils :-> " + "shareWhatsapp " + text + "   path  " + path);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage(WHATSAPP_ID);

        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        if (path != null && !path.isEmpty()) {
            Uri imageUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(path));
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        }

        shareIntent.setType(type);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toasty.error(context.getApplicationContext(), context.getResources().getString(R.string.whatsapp_not_installed), Toast.LENGTH_SHORT, true).show();
        }
    }

    public void shareFacebook(String text, String path) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage(FACEBOOK_ID);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);

        if (path != null && !path.isEmpty()) {
            Uri imageUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(path));
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        }

        shareIntent.setType(type);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toasty.error(context.getApplicationContext(), context.getResources().getString(R.string.facebook_not_installed), Toast.LENGTH_SHORT, true).show();
        }
    }

    public void shareMessenger(String text, String path) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage(MESSENGER_ID);

        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        if (path != null && !path.isEmpty()) {
            Uri imageUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(path));
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        }

        shareIntent.setType(type);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toasty.error(context.getApplicationContext(), context.getResources().getString(R.string.messenger_not_installed), Toast.LENGTH_SHORT, true).show();
        }
    }

    public void shareSnapshat(String text, String path) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage(SNAPSHAT_ID);

        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        if (path != null && !path.isEmpty()) {
            Uri imageUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(path));
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        }

        shareIntent.setType(type);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toasty.error(context.getApplicationContext(), context.getResources().getString(R.string.snapchat_not_installed), Toast.LENGTH_SHORT, true).show();
        }
    }

    public void shareHike(String text, String path) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage(HIKE_ID);

        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        if (path != null && !path.isEmpty()) {
            Uri imageUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(path));
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        }

        shareIntent.setType(type);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toasty.error(context.getApplicationContext(), context.getResources().getString(R.string.hike_not_installed), Toast.LENGTH_SHORT, true).show();
        }
    }

    public void shareInstagram(String text, String path) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage(INSTAGRAM_ID);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);

        if (path != null && !path.isEmpty()) {
            Uri imageUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(path));
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        }

        shareIntent.setType(type);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toasty.error(context.getApplicationContext(), context.getResources().getString(R.string.instagram_not_installed), Toast.LENGTH_SHORT, true).show();
        }
    }

    public void shareTwitter(String text, String path) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage(TWITTER_ID);

        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        if (path != null && !path.isEmpty()) {
            Uri imageUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(path));
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        }

        shareIntent.setType(type);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toasty.error(context.getApplicationContext(), context.getResources().getString(R.string.twitter_not_installed), Toast.LENGTH_SHORT, true).show();
        }
    }

    public void share(String text, String path) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);

        if (path != null && !path.isEmpty()) {
            Uri imageUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(path));
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        }

        shareIntent.setType(type);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(Intent.createChooser(shareIntent, context.getResources().getString(R.string.share_via) + " " + context.getResources().getString(R.string.app_name)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toasty.error(context.getApplicationContext(), context.getResources().getString(R.string.app_not_installed), Toast.LENGTH_SHORT, true).show();
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
        private String kind = "";

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setDownloading(true);
            Timber.e("ShareUtils :-> " + "onPreExecute");
            Log.v("prepost", "ok");
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
            Timber.e("ShareUtils :-> " + "doInBackground");
            int count;
            try {
                URL url = new URL((String) f_url[0]);
                String title = (String) f_url[1];
                String extension = (String) f_url[2];
                this.position = (int) f_url[3];
                this.share_app = (String) f_url[4];
                this.kind = (String) f_url[5];
                Log.v("v", (String) f_url[0]);

                URLConnection conection = url.openConnection();
                conection.setRequestProperty("Accept-Encoding", "identity");
                conection.connect();

                int lenghtOfFile = conection.getContentLength();
                Log.v("lenghtOfFile", lenghtOfFile + "");

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);


                String dir_path = Environment.getExternalStorageDirectory().toString() + "/StatusThumbnails/";

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
                    Log.v("dir", "file is exist");
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

                }
                Timber.e("ShareUtils :-> " + "File Writing complete");
                MediaScannerConnection.scanFile(context.getApplicationContext(), new String[]{dir_path + title.toString().replace("/", "_") + "_" + id + "." + extension},
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
                    context.sendBroadcast(scanIntent);
                } else {
                    final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
                    context.sendBroadcast(intent);
                }
                path = dir_path + title.toString().replace("/", "_") + "_" + id + "." + extension;
                Timber.e("ShareUtils :-> " + "Path -> " + path);
            } catch (Exception e) {
                Timber.e("ShareUtils :-> " + e.getLocalizedMessage());
                e.printStackTrace();
                //Log.v("ex",e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            Timber.e("ShareUtils :-> " + "onProgressUpdate");
            // setting progress percentage
            try {
                if (!progress[0].equals(old)) {
                    old = progress[0];
                    Log.v("download", progress[0] + "%");
//                    setDownloading(true);
                }
            } catch (Exception e) {

            }

        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            Timber.e("ShareUtils :-> " + "onPostExecute");
            createSharableLink(id, kind, share_app, path);
        }
    }


}
