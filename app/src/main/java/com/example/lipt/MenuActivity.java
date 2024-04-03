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
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.lipt.Database.Player;
import com.example.lipt.Database.PokeRepository;
import com.example.lipt.databinding.ActivityMenuBinding;

import java.util.List;


public class MenuActivity extends AppCompatActivity {

    private static final String CURRENT_USERNAME = "Active User";
    private static final int CURRENT_USER_ID = 0;

    ActivityMenuBinding binding;
    private PokeRepository menu_repo;
    private LiveData<List<Player>> allCurrentPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //retrieving and saving currently logged in player ID
        int current_id = getIntent().getIntExtra(CURRENT_USERNAME, 0);

        Toast.makeText(MenuActivity.this, "MENU ID: " + current_id, Toast.LENGTH_SHORT).show();

        //establishing repo, grabbing list of players
        menu_repo = new PokeRepository((Application) getApplicationContext());
        allCurrentPlayers = menu_repo.getAllPlayers();

        //grabbing current player specs, then displaying in the view
        allCurrentPlayers.observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                Player current_player = findPlayerbyID(players, current_id);
                //populating textviews with player's info
                if (current_player != null) {
                    binding.usernameMenuDisplayText.setText(current_player.getUsername());
                    binding.trainerLevelMenuDisplayText.setText(String.valueOf(current_player.getTrainer_level()));

                }
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

        //instantiating an interface of onClickListener for logging out
        binding.exitMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.mainToRegistrationFactory(getApplicationContext());
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


    private Player findPlayerbyID(List<Player> players, int id_num) {
        for(Player player : players) {
            if(player.getUserID() == id_num) {
                return player;
            }
        }
        return null;
    }



}