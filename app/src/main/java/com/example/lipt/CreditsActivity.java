/**
 * Luis Hernandez, Guillermo Zendejas
 * April 17, 2024
 * CreditsActivity.java, this class describes our credits activity
 */

package com.example.lipt;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lipt.databinding.ActivityCreditsBinding;

public class CreditsActivity extends AppCompatActivity {

    private static final String CURRENT_USERNAME = "Active User";
    private static final int CURRENT_USER_ID = 0;
    private int current_id;
    private ActivityCreditsBinding binding;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreditsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //retrieving and saving currently logged in player ID
        int current_id = getIntent().getIntExtra(CURRENT_USERNAME, 0);

        //setting background image
        ImageView creditView = findViewById(R.id.creditsBackgroundImage);
        int loserResourceID = getResources().getIdentifier("credits_wallpaper", "drawable", getPackageName());
        Glide.with(this)
                .load(loserResourceID)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(creditView);


        //instantiating an interface of onClickListener for return to menu button
        binding.exitCreditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MenuActivity.menuFactory(getApplicationContext(), current_id);
                startActivity(intent);
            }
        });

    }

    //intent factory
    public static Intent creditsFactory(Context context, int user_id) {
        Intent intent = new Intent(context, CreditsActivity.class);
        intent.putExtra(CURRENT_USERNAME, user_id);
        return intent;
    }
}