/**
 * Luis Hernandez, Guillermo Zendejas
 * April 3, 2024
 * PokedexActivity.java, this describes our application's Pokedex activity
 */

package com.example.lipt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Button;

import com.example.lipt.databinding.ActivityPokedexBinding;

public class PokedexActivity extends AppCompatActivity {
    private ActivityPokedexBinding binding;
    private static final String CURRENT_USERNAME = "Active User";
    private static final int CURRENT_USER_ID = 0;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPokedexBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        //initializing media player

        String[] names = {"pokemon1", "pokemon2", "pokemon3"};


        //retrieving and saving currently logged in player ID
        int current_id = getIntent().getIntExtra(CURRENT_USERNAME, 0);
        Toast.makeText(PokedexActivity.this, "RECORD ID: " + current_id, Toast.LENGTH_SHORT).show();

        //TODO: establish pokedex repo

        //TODO: implement scrollable view for pokedex

        //instantiating an interface of onClickListener for playing sound button
        binding.soundTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
            }
        });

        //instantiating an interface of onClickListener for return to menu button
        binding.exitPokedexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MenuActivity.mainMenuFactory(getApplicationContext(), current_id);
                startActivity(intent);
            }
        });
    }

    //overriding on destroy to ensure media player is released and memory leaks are prevented
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer when the activity is destroyed to avoid resource leaks
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    //intent factory
    public static Intent pokedexFactory(Context context, int user_id) {
        Intent intent = new Intent(context, PokedexActivity.class);
        intent.putExtra(CURRENT_USERNAME, user_id);
        return intent;
    }
}