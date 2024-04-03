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

import com.example.lipt.Database.Player;
import com.example.lipt.Database.PokeRepository;
import com.example.lipt.databinding.ActivityTrainerRecordBinding;

import java.util.List;

public class trainerRecordActivity extends AppCompatActivity {

    private PokeRepository record_repo;
    private LiveData<List<Player>> allCurrentPlayers;

    private ActivityTrainerRecordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrainerRecordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        //establishing repo, grabbing list of players
        record_repo = new PokeRepository((Application) getApplicationContext());
        allCurrentPlayers = record_repo.getAllPlayers();

        //grabbing current player specs, then displaying in the view
        allCurrentPlayers.observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                int currentID = 0;
                Player current_player = findPlayerbyID(players, 0);

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
                Intent intent = MainActivity.mainToRegistrationFactory(getApplicationContext());
                startActivity(intent);
            }
        });

    }


    //intent factory
    public static Intent trainerRecordFactory(Context context) {
        return new Intent(context, trainerRecordActivity.class);
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