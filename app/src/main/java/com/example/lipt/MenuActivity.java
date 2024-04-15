/**
 * Luis Hernandez, Guillermo Zendejas
 * April 3, 2024
 * MenuActivity.java, this describes the main menu activity view of our application
 */

package com.example.lipt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.lipt.Database.Player;
import com.example.lipt.Database.PlayerRepository;
import com.example.lipt.Database.PokemonRepository;
import com.example.lipt.databinding.ActivityMenuBinding;

import java.util.List;


public class MenuActivity extends AppCompatActivity {

    private static final String CURRENT_USERNAME = "Active User";
    private static final int CURRENT_USER_ID = 0;

    ActivityMenuBinding binding;
    private PlayerRepository menu_repo;
    private LiveData<List<Player>> allCurrentPlayers;

    private PokemonRepository pokerepo;

    MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //retrieving and saving currently logged in player ID
        int current_id = getIntent().getIntExtra(CURRENT_USERNAME, 0);

        Toast.makeText(MenuActivity.this, "MENU ID: " + current_id, Toast.LENGTH_SHORT).show();


        /*

        //establishing pokedex
        //establishing repo, grabbing list of pokemon
        pokerepo = new PokemonRepository((Application) getApplicationContext());

        //populating pokedex table
        for(int i = 1; i <= 493; i++ ) {
            Pokemon pokemon = new Pokemon(i,
                    PokemonInfo.getPokemonName(i),
                    getResources().getIdentifier("pokemon" + i, "drawable", getPackageName()),
                    getResources().getIdentifier("sound" + i, "raw", getPackageName())
            );
            pokerepo.insert(pokemon);
        }
         */

        //establishing repo, grabbing list of players
        menu_repo = new PlayerRepository((Application) getApplicationContext());
        allCurrentPlayers = menu_repo.getAllPlayers();

        //grabbing current player specs, then displaying in the view
        allCurrentPlayers.observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                for(Player player : players) {
                    if (player.getUserID() == current_id) {
                        binding.usernameMenuDisplayText.setText(player.getUsername());
                        binding.trainerLevelMenuDisplayText.setText(String.valueOf(player.getTrainer_level()));
                        break;
                    } else {
                        binding.usernameMenuDisplayText.setText(CURRENT_USERNAME);
                    }
                }
            }
        });

        //instantiating an interface of onClickListener for pokedex view
        binding.pokedexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PokedexActivity.pokedexFactory(getApplicationContext(), current_id);
                startActivity(intent);
            }
        });

        //instantiating an interface of onClickListener for trainer record view
        binding.trainerRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = trainerRecordActivity.trainerRecordFactory(getApplicationContext(), current_id);
                startActivity(intent);
            }
        });

        //instantiating an interface of onClickListener for prize collection view
        binding.prizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PrizeActivity.prizeFactory(getApplicationContext(), current_id);
                startActivity(intent);
            }
        });

        //instantiating an interface of onClickListener for prize collection view
        binding.newRoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = GameActivity.gameFactory(getApplicationContext(), current_id);
                startActivity(intent);
            }
        });

        //instantiating an instance of onClickListener for accessing admin dashboard activity
        binding.adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current_id = getIntent().getIntExtra(CURRENT_USERNAME, 0);
                Intent intent = AdminActivity.adminActivityIntentFactory(getApplicationContext(), current_id);
                startActivity(intent);
            }
        });


        //instantiating an interface of onClickListener for logging out
        binding.exitMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.mainFactory(getApplicationContext());
                startActivity(intent);
            }
        });


    }

    //Main menu factory
    public static Intent mainMenuFactory(Context context, int user_id) {
        Intent intent = new Intent(context, MenuActivity.class);
        intent.putExtra(CURRENT_USERNAME, user_id);
        return intent;
    }

}