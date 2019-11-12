package com.videos.phovio.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.videos.phovio.api.apiClient;
import com.videos.phovio.api.apiRest;
import com.videos.phovio.model.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Tamim on 26/10/2017.
 */


public class FirebaseIDService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseIDService";


    @Override
    public void onNewToken(@NonNull String s) {
        // Get updated InstanceID token.
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(s);
        super.onNewToken(s);


    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {

        Retrofit retrofit = apiClient.getClient();
        apiRest service = retrofit.create(apiRest.class);
        Call<ApiResponse> call = service.addDevice(token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful())
                    Log.v(TAG,"Hassan : "+response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.v(TAG,"Hassan : "+ t.getMessage().toString());
            }
        });
    }
}