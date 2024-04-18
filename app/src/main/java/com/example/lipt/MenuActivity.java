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
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
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
    private int current_id;
    private LiveData<Player> loggedInPlayer;
    MediaPlayer mediaPlayer;
    private final int NO_USER_ID = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //retrieving and saving currently logged in player ID
        current_id = getIntent().getIntExtra(CURRENT_USERNAME, 0);

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

        // making player button invisible for non-admin players.
        loggedInPlayer = menu_repo.getPlayerLiveDataById(current_id);
        loggedInPlayer.observe(this, player -> {
            if (!player.isAdmin()) {
                binding.adminButton.setVisibility(View.GONE);
            }
        });


        //setting onClickListener for accessing admin dashboard activity
        binding.adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current_id = getIntent().getIntExtra(CURRENT_USERNAME, 0);
                Intent intent = AdminActivity.adminActivityIntentFactory(getApplicationContext(), current_id);
                startActivity(intent);
            }
        });

        //instantiating an interface of onClickListener for credits activity
        binding.creditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CreditsActivity.creditsFactory(getApplicationContext(), current_id);
                startActivity(intent);
            }
        });


        //instantiating an interface of onClickListener for logging out
        binding.exitMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSharedPreference(NO_USER_ID);
                Intent intent = MainActivity.mainFactory(getApplicationContext());
                startActivity(intent);
            }
        });


    }

    //Main menu factory
    public static Intent menuFactory(Context context, int user_id) {
        Intent intent = new Intent(context, MenuActivity.class);
        intent.putExtra(CURRENT_USERNAME, user_id);
        return intent;
    }

    /**
     * Updates sharedPreferences value used to store the ID of the currently logged in User.
     * Persists when app is closed.
     * @param loggedInUserId the id to be saved to sharedPreferences
     */
    private void updateSharedPreference(int loggedInUserId) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_userId_key), loggedInUserId);
        sharedPrefEditor.apply();
    }

}