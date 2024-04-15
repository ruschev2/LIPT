/**
 * Luis Hernandez, Guillermo Zendejas
 * April 3, 2024
 * PokedexActivity.java, this describes our application's Pokedex activity
 */

package com.example.lipt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.lipt.Database.Pokemon;
import com.example.lipt.Database.PokemonRepository;
import com.example.lipt.databinding.ActivityPokedexBinding;

import java.util.List;

public class PokedexActivity extends AppCompatActivity {
    private ActivityPokedexBinding binding;
    private static final String CURRENT_USERNAME = "Active User";
    private static final int CURRENT_USER_ID = 0;
    private MediaPlayer mediaPlayer;
    private RecyclerView recyclerView;
    private PokemonAdapter adapter;
    private PokemonRepository pokedex_repo;
    private LiveData<List<Pokemon>> allPokemon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPokedexBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //retrieving and saving currently logged in player ID
        int current_id = getIntent().getIntExtra(CURRENT_USERNAME, 0);
        Toast.makeText(PokedexActivity.this, "RECORD ID: " + current_id, Toast.LENGTH_SHORT).show();

        //establishing repo, grabbing list of pokemon
        pokedex_repo = new PokemonRepository((Application) getApplicationContext());
        allPokemon = pokedex_repo.getAllPokemon();

        recyclerView = findViewById(R.id.pokedexRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        allPokemon.observe(this, new Observer<List<Pokemon>>() {
            @Override
            public void onChanged(List<Pokemon> pokemons) {
                Log.d(MainActivity.TAG, "Pokemon no.1 sound ID: " + getResources().getIdentifier("sound1", "raw", getPackageName()));
                Log.d(MainActivity.TAG, "Pokemon no. 4 sound ID:" + getResources().getIdentifier("sound4", "raw", getPackageName()));
                adapter = new PokemonAdapter(PokedexActivity.this, pokemons);
                recyclerView.setAdapter(adapter);
            }
        });

        //instantiating an interface of onClickListener for return to menu button
        binding.exitPokedexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MenuActivity.menuFactory(getApplicationContext(), current_id);
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