/**
 * Luis Hernandez, Guillermo Zendejas
 * April 3, 2024
 * PokedexActivity.java, this describes our application's Pokedex activity
 */

package com.example.lipt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Button;

import com.example.lipt.Utils.PokemonInfo;
import com.example.lipt.databinding.ActivityPokedexBinding;

import java.util.ArrayList;
import java.util.List;

public class PokedexActivity extends AppCompatActivity {
    private ActivityPokedexBinding binding;
    private static final String CURRENT_USERNAME = "Active User";
    private static final int CURRENT_USER_ID = 0;
    private MediaPlayer mediaPlayer;

    private RecyclerView recyclerView;
    private PokemonDisplayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPokedexBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        recyclerView = findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<PokemonDisplay> displays = new ArrayList<>();
        displays.add(new PokemonDisplay(PokemonInfo.getPokemonName(1), R.drawable.pokemon1, R.raw.sound1));
        displays.add(new PokemonDisplay(PokemonInfo.getPokemonName(2), R.drawable.pokemon2, R.raw.sound2));
        displays.add(new PokemonDisplay(PokemonInfo.getPokemonName(3), R.drawable.pokemon3, R.raw.sound3));


        adapter = new PokemonDisplayAdapter(displays);
        recyclerView.setAdapter(adapter);
        //initializing media player

        String[] names = {"pokemon1", "pokemon2", "pokemon3"};


        //retrieving and saving currently logged in player ID
        int current_id = getIntent().getIntExtra(CURRENT_USERNAME, 0);
        Toast.makeText(PokedexActivity.this, "RECORD ID: " + current_id, Toast.LENGTH_SHORT).show();

        //TODO: establish pokedex repo

        //TODO: implement scrollable view for pokedex


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