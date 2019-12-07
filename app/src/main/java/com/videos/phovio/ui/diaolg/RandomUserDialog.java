package com.videos.phovio.ui.diaolg;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.videos.phovio.Adapters.RandomUserAdapter;
import com.videos.phovio.R;
import com.videos.phovio.model.User;

import java.util.ArrayList;

/**
 * Created by Nirav Mandani on 07-12-2019.
 * Followal Solutions
 */
public class RandomUserDialog extends DialogFragment {

    private static final String EXTRA_CATEGORIES = "extra_categories";

    Context context;
    RandomUserAdapter adapter;
    ArrayList<User> userList = new ArrayList<>();
    FollowedListener listener;

    public static RandomUserDialog getInstance(ArrayList<User> userList) {
        RandomUserDialog dialog = new RandomUserDialog();
        Bundle extras = new Bundle();
        extras.putSerializable(EXTRA_CATEGORIES, userList);
        dialog.setArguments(extras);
        return dialog;
    }

    public void setListener(FollowedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userList = (ArrayList<User>) getArguments().getSerializable(EXTRA_CATEGORIES);
        }
        adapter = new RandomUserAdapter(context, userList);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_random_user, null, false);

        RecyclerView rvRandomUsers = (RecyclerView) view.findViewById(R.id.rvRandomUsers);
        TextView txtPositive = (TextView) view.findViewById(R.id.txtPositive);

        rvRandomUsers.setLayoutManager(new LinearLayoutManager(context));
        rvRandomUsers.setAdapter(adapter);

        txtPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPositiveClicked();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false);
        return builder.create();
    }


    public void onPositiveClicked() {
        if (adapter != null && adapter.getFollowedUsers().size() >= 5) {
            if (listener != null) {
                listener.onUserFollowed();
            }
            dismiss();
        } else {
            Toast.makeText(context, "Please Follow Minimum 5 Users", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    public interface FollowedListener {
        void onUserFollowed();
    }

}
