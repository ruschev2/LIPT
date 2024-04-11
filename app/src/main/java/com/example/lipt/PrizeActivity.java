/**
 * Luis Hernandez, Guillermo Zendejas
 * April 4, 2024
 * PrizeActivity.java, this class describes the prize collection activity in our application
 */

package com.example.lipt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.lipt.Database.Player;
import com.example.lipt.Database.PlayerPrizeCrossRef;
import com.example.lipt.Database.PlayerPrizeCrossRefDao;
import com.example.lipt.Database.PlayerPrizeCrossRefRepository;
import com.example.lipt.Database.PlayerRepository;
import com.example.lipt.Database.Prize;
import com.example.lipt.Database.PrizeRepository;
import com.example.lipt.databinding.ActivityPrizeBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PrizeActivity extends AppCompatActivity {

    private static final String CURRENT_USERNAME = "Active User";
    private static final int CURRENT_USER_ID = 0;
    private int current_id;
    private PlayerRepository player_repo;
    private PrizeRepository prize_repo;
    private PlayerPrizeCrossRefRepository playerprize_repo;
    private LiveData<List<Player>> allCurrentPlayers;
    private LiveData<List<Prize>> allPrizes;
    private LiveData<List<PlayerPrizeCrossRef>> playerPrizeRefs;
    private List<Integer> earned_prize_ids = new ArrayList<>();
    private Player current_player;
    private RecyclerView recyclerView;
    private PrizeAdapter adapter;
    ActivityPrizeBinding binding;
    Executor executor = Executors.newSingleThreadExecutor();
    private CountDownLatch latch = new CountDownLatch(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrizeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //retrieving and saving currently logged in Player ID
        current_id = getIntent().getIntExtra(CURRENT_USERNAME, 0);
        Toast.makeText(PrizeActivity.this, "PRIZE ID: " + current_id, Toast.LENGTH_SHORT).show();

        //grabbing player prize refs
        grabData(current_id);
        Log.d(MainActivity.TAG, "prize id: " + current_id);

        //generating recyclerView
        recyclerView = findViewById(R.id.prizeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            latch.await();
            allPrizes.observe(this, new Observer<List<Prize>>() {
                @Override
                public void onChanged(List<Prize> prizes) {
                    //generating new list for actually earned prizes
                    List<Prize> earnedPrizes = new ArrayList<>();
                    for(Prize prize : prizes) {
                        if(earned_prize_ids.contains(prize.getPrizeID())) {
                            earnedPrizes.add(prize);
                        }
                    }
                    //displaying player name in the view
                    binding.usernameDisplayTextView.setText(current_player.getUsername());

                    //displaying earned prizes in recyclerview
                    binding.prizesEarnedTextView.setText(String.valueOf(earnedPrizes.size()) + "/20");
                    adapter = new PrizeAdapter(PrizeActivity.this, earnedPrizes);
                    recyclerView.setAdapter(adapter);
                }
            });
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        //instantiating an interface of onClickListener for return home button
        binding.prizeExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MenuActivity.mainMenuFactory(getApplicationContext(), current_id);
                startActivity(intent);
            }
        });

    }

    //intent factory
    public static Intent prizeFactory(Context context, int user_id) {
        Intent intent = new Intent(context, PrizeActivity.class);
        intent.putExtra(CURRENT_USERNAME, user_id);
        return intent;
    }

    //for fetching database information
    private void grabData(final int playerId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //establishing repos, grabbing full lists of players, prizes, and playerprizerefs
                playerprize_repo = new PlayerPrizeCrossRefRepository((Application) getApplicationContext());
                earned_prize_ids = playerprize_repo.getPlayerPrizeIdsForPlayer(playerId);
                player_repo = new PlayerRepository((Application) getApplicationContext());
                allCurrentPlayers = player_repo.getAllPlayers();
                prize_repo = new PrizeRepository((Application) getApplicationContext());
                allPrizes = prize_repo.getAllPrizes();

                //grabbing current player
                current_player = player_repo.getPlayerById(current_id);
                Log.d(MainActivity.TAG, "executor prize id: " + current_player.getUserID());
                latch.countDown();
            }
        });
    }

}