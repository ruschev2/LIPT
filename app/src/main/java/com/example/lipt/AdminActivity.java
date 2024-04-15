package com.example.lipt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lipt.databinding.ActivityAdminBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminActivity extends AppCompatActivity {
    private static final String ADMIN_ACTIVITY_USER_ID = "com.example.lipt.ADMIN_ACTIVITY_USER_ID";
    private ActivityAdminBinding binding;

    private int loggedInId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loggedInId = getIntent().getIntExtra(ADMIN_ACTIVITY_USER_ID, 0);

        BottomNavigationView bottomNavigationView = binding.adminBottomNavigation;

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.back_arrow_item) {
                    Intent intent = MenuActivity.mainMenuFactory(getApplicationContext(), loggedInId);
                    startActivity(intent);
                }


                return true;
            }
        });

    }

    /**
     * An intent factory used by other activities for creating intents to AdminActivity
     * @param context The context of the activity sending the intent
     * @param adminUserId The id of the admin user accessing the AdminActivity
     * @return an intent with extra containing an admin user's activity
     */
    static Intent adminActivityIntentFactory(Context context, int adminUserId) {
        Intent intent = new Intent(context, AdminActivity.class);
        intent.putExtra(ADMIN_ACTIVITY_USER_ID, adminUserId);
        return intent;
    }
}