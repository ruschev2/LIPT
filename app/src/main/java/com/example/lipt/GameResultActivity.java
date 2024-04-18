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
import android.widget.ImageView;
import com.example.lipt.Database.Player;
import com.example.lipt.Database.PlayerPrizeCrossRef;
import com.example.lipt.Database.PlayerPrizeCrossRefRepository;
import com.example.lipt.Database.PlayerRepository;
import com.example.lipt.Database.Prize;
import com.example.lipt.Database.PrizeRepository;
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
    private PrizeRepository prize_repo;
    private PlayerPrizeCrossRefRepository playerPrizeRepo;
    private Player current_player;
    private List<Prize> allPrizes = new ArrayList<>();
    private final List<Integer> earnedPrizeIDs = new ArrayList<>();
    private final List<Integer> unearnedPrizeIDs = new ArrayList<>();
    Executor executor = Executors.newSingleThreadExecutor();
    private int final_score = 0, current_id = 0, drawnPrizeId;
    private boolean prize_awarded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //retrieving and saving currently logged in Player ID
        current_id = getIntent().getIntExtra(CURRENT_USERNAME, 0);
        final_score = getIntent().getIntExtra(FINAL_SCORE, 0);

        //fetching player and prize data
        setData(current_id);

        //grabbing playerprizecrossrefs
        playerPrizeRepo = new PlayerPrizeCrossRefRepository((Application) getApplicationContext());
        LiveData<List<PlayerPrizeCrossRef>> playerPrizeCrossRefs = playerPrizeRepo.getAllPlayerPrizeCrossRefs();

        //beginning observer call for rest of activity
        playerPrizeCrossRefs.observe(this, new Observer<List<PlayerPrizeCrossRef>>() {
            @Override
            public void onChanged(List<PlayerPrizeCrossRef> playerPrizeCrossRefs) {
                //setting final score and name text views
                binding.finalScoreTextView.setText(String.valueOf(final_score) + "/10");
                binding.currentLevelDisplay.setText(String.valueOf(current_player.getTrainer_level()));

                //if player has 'won' (score of 7 or greater)
                if(final_score > 6) {
                    //player has leveled up
                    binding.levelUpText.setText("You leveled up!");

                    //displaying earned prize
                    if(prize_awarded) {
                        binding.earnedPrizeText.setText(allPrizes.get(drawnPrizeId-1).getName());
                        binding.resultPrizeImageView.setImageResource(allPrizes.get(drawnPrizeId-1).getImageResourceId());
                    }
                    else {
                        binding.earnedPrizeText.setText("All earned!");
                        AlertDialog.Builder builder = new AlertDialog.Builder(GameResultActivity.this);
                        builder.setTitle("Congratulations");
                        builder.setMessage("Your prize list is full!" +
                                "\nIf you are feeling bold, you can delete them in prize collection mode.");

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            }
        });

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
        binding.restartGameButton.setOnClickListener(v -> {
            Intent intent = GameActivity.gameFactory(getApplicationContext(), current_id);
            startActivity(intent);
        });

        //instantiating an interface of onClickListener for return to menu button
        binding.exitGameResultButton.setOnClickListener(v -> {
            Intent intent = MenuActivity.menuFactory(getApplicationContext(), current_id);
            startActivity(intent);
        });
    }

    //game result factory
    public static Intent gameResultFactory(Context context, int user_id, int final_score) {
        Intent intent = new Intent(context, GameResultActivity.class);
        intent.putExtra(CURRENT_USERNAME, user_id);
        intent.putExtra(FINAL_SCORE, final_score);
        return intent;
    }

    /**
     * this method grabs relevant data from the room database, and awards a random prize if warranted.
     * @param playerId the ID of the player who is logged into the app.
     */
    private void setData(final int playerId) {
        executor.execute(() -> {
            //assigning the current player account to current_player object
            player_repo = new PlayerRepository((Application) getApplicationContext());
            current_player = player_repo.getPlayerById(playerId);

            //calculating which prizes have not been earned yet, generating list of their IDs
            for(int i = 1; i < 21; i++) {
                unearnedPrizeIDs.add(i);
            }
            prize_repo = new PrizeRepository((Application) getApplicationContext());
            allPrizes = prize_repo.getPrizeList();
            playerPrizeRepo = new PlayerPrizeCrossRefRepository((Application) getApplicationContext());
            earnedPrizeIDs.addAll(playerPrizeRepo.getPlayerPrizeIdsForPlayer(playerId));
            unearnedPrizeIDs.removeAll(earnedPrizeIDs);

            //if player can earn a prize and should, process this
            if(final_score > 6 ) {
                if(!unearnedPrizeIDs.isEmpty()) {
                    //generating randomness, drawing a random prize ID
                    Random random = new Random();
                    int randomIndex = random.nextInt(unearnedPrizeIDs.size());
                    drawnPrizeId = unearnedPrizeIDs.get(randomIndex);

                    //rewarding prize into player's account (through playerprizetable)
                    PlayerPrizeCrossRef reward = new PlayerPrizeCrossRef(playerId, drawnPrizeId);
                    playerPrizeRepo.insert(reward);
                    prize_awarded = true;
                }
            }
        });
    }

}