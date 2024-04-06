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
import android.view.View;
import android.widget.Toast;
import com.example.lipt.Database.Player;
import com.example.lipt.Database.PlayerRepository;
import com.example.lipt.Database.Prize;
import com.example.lipt.Database.PrizeRepository;
import com.example.lipt.databinding.ActivityPrizeBinding;
import java.util.List;

public class PrizeActivity extends AppCompatActivity {

    private static final String CURRENT_USERNAME = "Active User";
    private static final int CURRENT_USER_ID = 0;
    private PlayerRepository player_repo;

    private PrizeRepository prize_repo;
    private LiveData<List<Player>> allCurrentPlayers;

    private LiveData<List<Prize>> allPrizes;

    private RecyclerView recyclerView;

    private PrizeAdapter adapter;

    ActivityPrizeBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrizeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //retrieving and saving currently logged in Player ID
        int current_id = getIntent().getIntExtra(CURRENT_USERNAME, 0);
        Toast.makeText(PrizeActivity.this, "PRIZE ID: " + current_id, Toast.LENGTH_SHORT).show();

        player_repo = new PlayerRepository((Application) getApplicationContext());
        allCurrentPlayers = player_repo.getAllPlayers();

        //establishing repo, grabbing list of players
        player_repo = new PlayerRepository((Application) getApplicationContext());
        allCurrentPlayers = player_repo.getAllPlayers();

        //grabbing current player specs, then displaying in the view
        allCurrentPlayers.observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                for(Player player : players) {
                    if (player.getUserID() == current_id) {
                        binding.usernameDisplayTextView.setText(player.getUsername());
                        break;
                    } else {
                        binding.usernameDisplayTextView.setText(CURRENT_USERNAME);
                    }
                }
            }
        });

        prize_repo = new PrizeRepository((Application) getApplicationContext());
        allPrizes = prize_repo.getAllPrizes();

        recyclerView = findViewById(R.id.prizeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        allPrizes.observe(this, new Observer<List<Prize>>() {
            @Override
            public void onChanged(List<Prize> prizes) {
                binding.prizesEarnedTextView.setText(String.valueOf(prizes.size()) + "/20");
                adapter = new PrizeAdapter(PrizeActivity.this, prizes);
                recyclerView.setAdapter(adapter);
            }
        });

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
}