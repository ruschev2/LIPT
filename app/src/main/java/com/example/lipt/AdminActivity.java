package com.example.lipt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
                int selectedItemId = menuItem.getItemId();
                Fragment adminUsersListFragment = new AdminUsersListFragment();
                Fragment allUsersListFragment = new AllUsersListFragment();
                Fragment nextFragment = adminUsersListFragment;


                if (selectedItemId == R.id.back_arrow_item) {
                    Intent intent = MenuActivity.mainMenuFactory(getApplicationContext(), loggedInId);
                    startActivity(intent);
                } else if (selectedItemId == R.id.admin_users_item) {
                    nextFragment = adminUsersListFragment;
                } else if (selectedItemId == R.id.all_users) {
                    nextFragment = allUsersListFragment;
                }
                switchFragment(nextFragment);
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.all_users);

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

    //todo uncomment after setting up fragments
    private void switchFragment(Fragment newFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.admin_frame_layout, newFragment);
        fragmentTransaction.commit();
    }

}