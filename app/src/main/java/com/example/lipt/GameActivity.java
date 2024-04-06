/**
 * Luis Hernandez, Guillermo Zendejas
 * April 6, 2024
 * GameActivity.java, this class describes the game round activity, the focus of our application
 */

package com.example.lipt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.lipt.Database.Player;
import com.example.lipt.Database.PlayerRepository;
import com.example.lipt.Database.Pokemon;
import com.example.lipt.Database.PokemonRepository;
import com.example.lipt.Database.Prize;
import com.example.lipt.Database.PrizeRepository;
import com.example.lipt.databinding.ActivityGameBinding;

public class GameActivity extends AppCompatActivity {

    private ActivityGameBinding binding;
    private static final String CURRENT_USERNAME = "Active User";
    private static final int CURRENT_USER_ID = 0;
    private MediaPlayer mediaPlayer;
    private PokemonAdapter pokemon_adapter;
    private PrizeAdapter prize_adapter;
    private PlayerRepository player_repo;
    private PrizeRepository prize_repo;
    private PokemonRepository pokemon_repo;
    private LiveData<Player> allPlayers;
    private LiveData<Prize> allPrizes;
    private LiveData<Pokemon> allPokemon;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        int current_id = getIntent().getIntExtra(CURRENT_USERNAME, 0);
        Toast.makeText(GameActivity.this, "GAME ID: " + current_id, Toast.LENGTH_SHORT).show();



        //instantiating an interface of onClickListener for returning to menu button
        binding.exitGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MenuActivity.mainMenuFactory(getApplicationContext(), current_id);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    //game factory
    public static Intent gameFactory(Context context, int user_id) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(CURRENT_USERNAME, user_id);
        return intent;
    }
}