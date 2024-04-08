package com.example.lipt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class GameResultActivity extends AppCompatActivity {

    private ActivityGameResultBinding binding;
    private static final String CURRENT_USERNAME = "Active User";
    private static final String FINAL_SCORE = "hopefully ten";
    private static final int CURRENT_USER_ID = 0;
    private PlayerRepository player_repo;
    private LiveData<List<Player>> allCurrentPlayers;
    private PlayerPrizeCrossRefRepository playerPrizeRepo;
    private List<Integer> earnedPrizeIDs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //retrieving and saving currently logged in Player ID
        int current_id = getIntent().getIntExtra(CURRENT_USERNAME, 0);
        Toast.makeText(GameResultActivity.this, "RESULT ID: " + current_id, Toast.LENGTH_SHORT).show();

        int final_score = getIntent().getIntExtra(FINAL_SCORE, 0);
        Toast.makeText(GameResultActivity.this, "SCORE: " + final_score, Toast.LENGTH_SHORT).show();

        binding.finalScoreTextView.setText(String.valueOf(final_score) + "/10");

        //incrementing player rounds and points
        player_repo = new PlayerRepository((Application) getApplicationContext());
        player_repo.increasePlayerRoundsPlayed(current_id);
        player_repo.increasePlayerPoints(current_id, final_score);

        //activity view if player has succeeded in a round (7 or higher score)
        if(final_score > 6) {

            //leveling up the player
            binding.levelUpText.setText("You leveled up!");
            player_repo.levelUpPlayer(current_id);

            //displaying player level
            player_repo = new PlayerRepository((Application) getApplicationContext());
            Player current_player = player_repo.getPlayerById(current_id);
            binding.currentLevelDisplay.setText(current_player.getTrainer_level());

            //calculating which prizes have not been earned yet, generating list of IDs
            List<Prize> full_prize_list = PokemonInfo.full_prize_list;
            playerPrizeRepo = new PlayerPrizeCrossRefRepository((Application) getApplicationContext());
            earnedPrizeIDs.addAll(playerPrizeRepo.getPlayerPrizeIdsForPlayer(current_id));
            List<Integer> unearnedPrizeIDs = PokemonInfo.getPrizeIDList();
            unearnedPrizeIDs.removeAll(earnedPrizeIDs);

            Random random = new Random();
            int randomIndex = random.nextInt(unearnedPrizeIDs.size());
            int drawnPrizeID = unearnedPrizeIDs.get(randomIndex);

            //displaying awarded prize
            binding.earnedPrizeText.setText(full_prize_list.get(drawnPrizeID).getName());
            binding.resultPrizeImageView.setImageResource(full_prize_list.get(drawnPrizeID).getImageResourceId());

            //awarding prize into player's account
            PlayerPrizeCrossRef reward = new PlayerPrizeCrossRef(current_id, drawnPrizeID);
            playerPrizeRepo.insert(reward);
        }


        //activity view if player has lost a round (6 or less score)
        else {
            //displaying player level
            Player current_player = player_repo.getPlayerById(current_id);
            binding.currentLevelDisplay.setText(String.valueOf(current_player.getTrainer_level()));

            //displaying the loser prize image
            ImageView wonkaView = findViewById(R.id.youLoseGoodDaySir);
            String gifPath = "R.drawable.loser_result";
            Glide.with(this)
                    .load(gifPath)
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


}