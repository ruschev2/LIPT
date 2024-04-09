package com.example.lipt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lipt.Database.Player;
import com.example.lipt.Database.PlayerPrizeCrossRef;
import com.example.lipt.Database.PlayerPrizeCrossRefRepository;
import com.example.lipt.Database.PlayerRepository;
import com.example.lipt.Database.Pokemon;
import com.example.lipt.Database.Prize;
import com.example.lipt.Database.PrizeRepository;
import com.example.lipt.Utils.PokemonInfo;
import com.example.lipt.databinding.ActivityGameResultBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class GameResultActivity extends AppCompatActivity {

    private ActivityGameResultBinding binding;
    private static final String CURRENT_USERNAME = "Active User";
    private static final String FINAL_SCORE = "hopefully ten";
    private static final int CURRENT_USER_ID = 0;
    private PlayerRepository player_repo;
    private Player current_player;
    private LiveData<List<Player>> allCurrentPlayers;
    private PlayerPrizeCrossRefRepository playerPrizeRepo;
    private List<Integer> earnedPrizeIDs = new ArrayList<>();
    Executor executor = Executors.newSingleThreadExecutor();
    private int final_score = 0, current_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //retrieving and saving currently logged in Player ID
        current_id = getIntent().getIntExtra(CURRENT_USERNAME, 0);
        Toast.makeText(GameResultActivity.this, "RESULT ID: " + current_id, Toast.LENGTH_SHORT).show();
        Log.d(MainActivity.TAG, "initial result ID: " + current_id);

        final_score = getIntent().getIntExtra(FINAL_SCORE, 0);
        Toast.makeText(GameResultActivity.this, "SCORE: " + final_score, Toast.LENGTH_SHORT).show();


        updatePlayerAsync(current_id);

        //if player has lost
        if(final_score < 7) {
            //displaying the loser prize image
            ImageView wonkaView = findViewById(R.id.youLoseGoodDaySir);
            int loserResourceID = getResources().getIdentifier("loser_result", "drawable", getPackageName());
            Glide.with(this)
                    .load(loserResourceID)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(wonkaView);
        }

        //instantiating an interface of onClickListener for playing a new game round
        binding.restartGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = GameActivity.gameFactory(getApplicationContext(), current_id);
                startActivity(intent);
            }
        });

        //instantiating an interface of onClickListener for return to menu button
        binding.exitGameResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MenuActivity.mainMenuFactory(getApplicationContext(), current_id);
                startActivity(intent);
            }
        });
    }

    //game result factory
    public static Intent gameResultFactory(Context context, int user_id, int final_score) {
        Intent intent = new Intent(context, GameResultActivity.class);
        intent.putExtra(CURRENT_USERNAME, user_id);
        intent.putExtra(FINAL_SCORE, final_score);
        return intent;
    }

    //for updating the UI
    private void updatePlayerAsync(final int playerId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //displaying final score in view
                binding.finalScoreTextView.setText(String.valueOf(final_score) + "/10");

                //displaying player level in view
                player_repo = new PlayerRepository((Application) getApplicationContext());
                current_player = player_repo.getPlayerById(current_id);
                Log.d(MainActivity.TAG, "player id: " + current_player.getUserID());
                binding.currentLevelDisplay.setText(String.valueOf(current_player.getTrainer_level()));


                //if player has won (score of 7 or greater)
                if(final_score > 6) {
                    //player has leveled up
                    binding.levelUpText.setText("You leveled up!");

                    //calculating which prizes have not been earned yet, generating list of their IDs
                    List<Prize> full_prize_list = PokemonInfo.full_prize_list;
                    playerPrizeRepo = new PlayerPrizeCrossRefRepository((Application) getApplicationContext());
                    earnedPrizeIDs.addAll(playerPrizeRepo.getPlayerPrizeIdsForPlayer(current_id));
                    List<Integer> unearnedPrizeIDs = PokemonInfo.getPrizeIDList();
                    unearnedPrizeIDs.removeAll(earnedPrizeIDs);

                    //if player has already earned all prizes
                    if(unearnedPrizeIDs.isEmpty()) {
                        binding.earnedPrizeText.setText("All earned!");
                    }

                    //player is assigned a new prize otherwise
                    else {
                        //generating randomness, drawing a random prize ID
                        Random random = new Random();
                        int randomIndex = random.nextInt(unearnedPrizeIDs.size());
                        int drawnPrizeID = unearnedPrizeIDs.get(randomIndex);

                        //displaying awarded prize in View
                        binding.earnedPrizeText.setText(full_prize_list.get(drawnPrizeID-1).getName());
                        binding.resultPrizeImageView.setImageResource(full_prize_list.get(drawnPrizeID-1).getImageResourceId());

                        //awarding prize into player's account
                        PlayerPrizeCrossRef reward = new PlayerPrizeCrossRef(current_id, drawnPrizeID);
                        playerPrizeRepo.insert(reward);

                    }

                }
            }
        });


    }


}