/**
 * Luis Hernandez, Guillermo Zendejas
 * April 1, 2024
 * trainerRecordActivity.java, this describes the trainer record activity for our application
 */

package com.example.lipt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.lipt.Database.Player;
import com.example.lipt.Database.PlayerPrizeCrossRefRepository;
import com.example.lipt.Database.PlayerRepository;
import com.example.lipt.databinding.ActivityTrainerRecordBinding;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class trainerRecordActivity extends AppCompatActivity {
    private PlayerRepository player_repo;
    private PlayerPrizeCrossRefRepository playerprize_repo;
    private LiveData<List<Player>> allCurrentPlayers;
    private List<Integer> earnedPrizes;
    private ActivityTrainerRecordBinding binding;
    private static final String CURRENT_USERNAME = "Active User";
    private static final int CURRENT_USER_ID = 0;
    private int current_id;
    Executor executor = Executors.newSingleThreadExecutor();
    CountDownLatch latch = new CountDownLatch(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrainerRecordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //retrieving and saving currently logged in player ID
        current_id = getIntent().getIntExtra(CURRENT_USERNAME, 0);
        Toast.makeText(trainerRecordActivity.this, "RECORD ID: " + current_id, Toast.LENGTH_SHORT).show();

        //grabbing earned prizes
        grabEarnedPrizes(current_id);


        try {
            latch.await();

            //grabbing current player specs, then displaying in the view
            allCurrentPlayers.observe(this, new Observer<List<Player>>() {
                @Override
                public void onChanged(List<Player> players) {
                    Player current_player = findPlayerbyID(players, current_id);

                    //populating field with player specs if found
                    if (current_player != null) {
                        binding.usernameTextView.setText(current_player.getUsername());
                        binding.trainerLevelTextView.setText(String.valueOf(current_player.getTrainer_level()));
                        binding.roundsPlayedTextView.setText(String.valueOf(current_player.getRounds_played()));
                        binding.accuracyTextView.setText(String.format("%.2f", current_player.getAccuracy()) + "%");
                        binding.trainerRecordPrizeTextView.setText(String.valueOf(earnedPrizes.size()) + "/20");
                    } else {
                        binding.usernameTextView.setText(getString(R.string.null_string));
                        binding.trainerLevelTextView.setText(R.string.null_string);
                        binding.roundsPlayedTextView.setText(getString(R.string.null_string));
                        binding.accuracyTextView.setText(getString(R.string.null_string));
                        binding.trainerRecordPrizeTextView.setText(R.string.null_string);
                    }
                }
            });

        } catch(InterruptedException e) {
            e.printStackTrace();
        }


        //instantiating an interface of onClickListener for resetting trainer level
        binding.trainerResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(trainerRecordActivity.this);
                builder.setTitle("Confirm reset");
                builder.setMessage("Are you certain you wish to reset your trainer level to zero?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetLevel(current_id);
                        Toast.makeText(trainerRecordActivity.this, "Level reset!", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //instantiating an interface of onClickListener for self delete button
        binding.selfDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(trainerRecordActivity.this);
                builder.setTitle("Confirm deletion");
                builder.setMessage("Are you absolutely certain you wish to delete your account?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetLevel(current_id);
                        Toast.makeText(trainerRecordActivity.this, "Goodbye, Trainer!", Toast.LENGTH_SHORT).show();
                        player_repo.deletePlayer(current_id);

                        //creating intent to return to login menu
                        Intent intent = MainActivity.mainFactory(getApplicationContext());
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

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

    /**
     * this method returns a specific player from a given list using their id number
     *
     * @param players the list of players parsed
     * @param id_num  the id number of desired player
     * @return the player which has the parameterized ID number
     */
    private Player findPlayerbyID(List<Player> players, int id_num) {
        for (Player player : players) {
            if (player.getUserID() == id_num) {
                return player;
            }
        }
        return null;
    }

    /**
     * this method updates the class fields player_repo and earned_prizes
     *
     * @param playerId the ID of the player whose earned prize IDs list will be updated
     */
    private void grabEarnedPrizes(final int playerId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //establishing repo, grabbing list of players and prize refs
                player_repo = new PlayerRepository((Application) getApplicationContext());
                allCurrentPlayers = player_repo.getAllPlayers();
                playerprize_repo = new PlayerPrizeCrossRefRepository((Application) getApplicationContext());
                earnedPrizes = playerprize_repo.getPlayerPrizeIdsForPlayer(playerId);

                latch.countDown();
            }
        });
    }

    /**
     * this method resets the trainer's level to zero
     *
     * @param playerId the player ID of the trainer who will be reset to zero
     */
    private void resetLevel(final int playerId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                player_repo.resetPlayerLevel(playerId);
            }

        });
    }
}