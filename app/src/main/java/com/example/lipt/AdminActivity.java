package com.example.lipt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminActivity extends AppCompatActivity {

    private static final String ADMIN_ACTIVITY_USER_ID = "com.example.lipt.ADMIN_ACTIVITY_USER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

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