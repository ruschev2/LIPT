package com.example.lipt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.lipt.Database.Player;
import com.example.lipt.databinding.ActivityPlayerInfoBinding;

import java.util.Locale;

public class PlayerInfoActivity extends AppCompatActivity {
    private static final String PLAYER_INFO_ACTIVITY_SELECTED_USER_ID = "com.example.lipt.PLAYER_INFO_ACTIVITY_SELECTED_USER_ID";
    private static final String PLAYER_INFO_ACTIVITY_LOGGED_IN_USER_ID = "com.example.lipt.PLAYER_INFO_ACTIVITY_LOGGED_IN_USER_ID";
    private int loggedInAdminId;
    private int selectedUserId;

    private ActivityPlayerInfoBinding binding;
    private PlayerViewModel playerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlayerInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        selectedUserId = getIntent().getIntExtra(PLAYER_INFO_ACTIVITY_SELECTED_USER_ID, 0);
        loggedInAdminId = getIntent().getIntExtra(PLAYER_INFO_ACTIVITY_LOGGED_IN_USER_ID,0);

        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);

        playerViewModel.getPlayerLiveDataById(selectedUserId).observe(this, user -> {
            if (user != null) {
                updateUserUI(user);
            }
        });

        binding.backAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AdminActivity.adminActivityIntentFactory(getApplicationContext(), loggedInAdminId);
                startActivity(intent);
            }
        });

        binding.adminPromoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerViewModel.promotePlayerToAdmin(selectedUserId);
            }
        });

        binding.levelUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerViewModel.levelUpPlayer(selectedUserId);
            }
        });

    }

    private void updateUserUI(Player user) {
        if(user.isAdmin()) {
            binding.adminStatusTextview.setText("Admin");
            binding.adminPromoteButton.setVisibility(View.GONE);
        } else {
            binding.adminStatusTextview.setText("User");
            binding.adminPromoteButton.setVisibility(View.VISIBLE);
        }

        if(user.getUserID() == loggedInAdminId) {
            binding.levelUpButton.setVisibility(View.INVISIBLE);
        }

        binding.usernameTextview.setText(user.getUsername());
        binding.levelValueTextview.setText(String.valueOf(user.getTrainer_level()));
        binding.roundsValueTextview.setText(String.valueOf(user.getRounds_played()));
        binding.accuracyValueTextview.setText(String.valueOf(String.format("%.2f", user.getAccuracy()) + "%"));
    }

    static Intent adminActivityIntentFactory(Context context, int loggedInAdminId, int selectedUserID) {
        Intent intent = new Intent(context, PlayerInfoActivity.class);
        intent.putExtra(PLAYER_INFO_ACTIVITY_LOGGED_IN_USER_ID, loggedInAdminId);
        intent.putExtra(PLAYER_INFO_ACTIVITY_SELECTED_USER_ID, selectedUserID);
        return intent;
    }
}