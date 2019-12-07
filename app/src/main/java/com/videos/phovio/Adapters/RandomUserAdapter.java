package com.videos.phovio.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.videos.phovio.Provider.PrefManager;
import com.videos.phovio.R;
import com.videos.phovio.api.apiClient;
import com.videos.phovio.api.apiRest;
import com.videos.phovio.model.ApiResponse;
import com.videos.phovio.model.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Nirav Mandani on 07-12-2019.
 * Followal Solutions
 */
public class RandomUserAdapter extends RecyclerView.Adapter<RandomUserAdapter.RandomUserViewHolder> {

    Context context;
    ArrayList<User> users = new ArrayList<>();
    ArrayList<User> followedUsers = new ArrayList<>();

    public RandomUserAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    public ArrayList<User> getFollowedUsers() {
        return followedUsers;
    }

    @NonNull
    @Override
    public RandomUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_random_user, parent, false);
        return new RandomUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RandomUserViewHolder holder, final int position) {
        final User user = users.get(position);
        holder.txtUserName.setText(user.getName());
        holder.txtFollowUser.setText(user.isFollow() ? "UNFOLLOW" : "FOLLOW");
        if (!user.getImage().isEmpty()) {
            Picasso.with(context).load(user.getImage()).error(R.mipmap.ic_launcher_round).placeholder(R.drawable.profile).into(holder.imgProfile);
        } else {
            Picasso.with(context).load(R.mipmap.ic_launcher_round).error(R.mipmap.ic_launcher_round).placeholder(R.drawable.profile).into(holder.imgProfile);
        }

        holder.txtFollowUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                follow(user, position);
            }
        });
    }

    public void follow(final User user, final int position) {

        PrefManager prf = new PrefManager(context);
        if (prf.getString("LOGGED").toString().equals("TRUE")) {
            String follower = prf.getString("ID_USER");
            String key = prf.getString("TOKEN_USER");
            Retrofit retrofit = apiClient.getClient();
            apiRest service = retrofit.create(apiRest.class);
            Call<ApiResponse> call = service.follow(user.getId(), Integer.parseInt(follower), key);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals(200)) {
                            user.setFollow(true);
                            followedUsers.add(user);
                        } else if (response.body().getCode().equals(202)) {
                            user.setFollow(false);
                            followedUsers.remove(user);
                        }
                        users.set(position, user);
                        notifyItemChanged(position);
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class RandomUserViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imgProfile;
        TextView txtUserName;
        TextView txtFollowUser;

        public RandomUserViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProfile = (CircleImageView) itemView.findViewById(R.id.imgProfile);
            txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
            txtFollowUser = (TextView) itemView.findViewById(R.id.txtFollowUser);
        }
    }
}
