/**
 * Luis Hernandez, Guillermo Zendejas
 * April 1, 2024
 * trainerRecordActivity.java, this describes the trainer record activity for our application
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
import com.example.lipt.Database.PlayerRepository;
import com.example.lipt.databinding.ActivityTrainerRecordBinding;

import java.util.List;

public class trainerRecordActivity extends AppCompatActivity {
    private PlayerRepository record_repo;
    private LiveData<List<Player>> allCurrentPlayers;

    private ActivityTrainerRecordBinding binding;

    private static final String CURRENT_USERNAME = "Active User";
    private static final int CURRENT_USER_ID = 0;

    //TODO: create player adapater and implement in trainer record activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrainerRecordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //retrieving and saving currently logged in player ID
        int current_id = getIntent().getIntExtra(CURRENT_USERNAME, 0);

        Toast.makeText(trainerRecordActivity.this, "RECORD ID: " + current_id, Toast.LENGTH_SHORT).show();

        //establishing repo, grabbing list of players
        record_repo = new PlayerRepository((Application) getApplicationContext());
        allCurrentPlayers = record_repo.getAllPlayers();

        //grabbing current player specs, then displaying in the view
        allCurrentPlayers.observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                Player current_player = findPlayerbyID(players, current_id);

                //populating field with player spec if found
                if (current_player != null) {
                    binding.usernameDisplayTextView.setText(current_player.getUsername());
                    binding.accuracyDisplayTextView.setText(String.valueOf(current_player.getAccuracy()));
                    binding.roundsPlayedTextView.setText(String.valueOf(current_player.getRounds_played()));
                }
                else {
                    binding.usernameDisplayTextView.setText(getString(R.string.null_string));
                    binding.accuracyDisplayTextView.setText(getString(R.string.null_string));
                    binding.roundsPlayedTextView.setText(getString(R.string.null_string));
                }
            }
        });



        //instantiating an interface of onClickListener for return home button
        binding.trainerRecordExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MenuActivity.mainMenuFactory(getApplicationContext(), current_id);
                startActivity(intent);
            }
        });

    }


    //intent factory
    public static Intent trainerRecordFactory(Context context, int user_id) {
        Intent intent = new Intent(context, trainerRecordActivity.class);
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