package com.example.lipt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.lipt.Database.Player;
import com.example.lipt.Database.PlayerRepository;
import com.example.lipt.Database.Prize;
import com.example.lipt.Database.PrizeRepository;
import com.example.lipt.databinding.ActivityGameResultBinding;

import java.util.List;

public class GameResultActivity extends AppCompatActivity {

    private ActivityGameResultBinding binding;
    private static final String CURRENT_USERNAME = "Active User";
    private static final String FINAL_SCORE = "hopefully ten";
    private static final int CURRENT_USER_ID = 0;
    private PlayerRepository login_repo;
    private LiveData<List<Player>> allCurrentPlayers;
    private PrizeRepository prize_repo;
    private LiveData<List<Prize>> allPrizes;

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