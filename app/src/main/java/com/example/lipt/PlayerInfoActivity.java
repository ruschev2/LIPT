package com.example.lipt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lipt.databinding.ActivityPlayerInfoBinding;

public class PlayerInfoActivity extends AppCompatActivity {
    private static final String PLAYER_INFO_ACTIVITY_USER_ID = "com.example.lipt.PLAYER_INFO_ACTIVITY_USER_ID";
    private int adminUserId;

    private ActivityPlayerInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlayerInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }


    static Intent adminActivityIntentFactory(Context context, int adminUserId) {
        Intent intent = new Intent(context, PlayerInfoActivity.class);
        intent.putExtra(PLAYER_INFO_ACTIVITY_USER_ID, adminUserId);
        return intent;
    }
}