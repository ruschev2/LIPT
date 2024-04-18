/**
 * Luis Hernandez, Guillermo Zendejas
 * April 1, 2024
 * registrationActivity.java, this describes the registration activity for our application
 */

package com.example.lipt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.lipt.Database.Player;
import com.example.lipt.Database.PlayerRepository;
import com.example.lipt.Utils.InputValidator;
import com.example.lipt.databinding.ActivityRegistrationBinding;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class registrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;
    private PlayerRepository registration_repo;
    private LiveData<List<Player>> allCurrentPlayers;
    boolean registrationProcessed;
    Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initializePlayerRepo();

        //instantiating an interface of onClickListener for register button
        binding.registrationButton2.setOnClickListener(v -> {

            //user inputted username and password
            String temp_username = binding.registrationUsernameTextEdit.getText().toString();
            String temp_password = binding.registrationPasswordTextEdit.getText().toString();

            //if inputted username and password are viable inputs
            if (!InputValidator.viableUsername(temp_username) || !InputValidator.viablePassword(temp_password)) {
                Toast.makeText(registrationActivity.this, "Invalid username and/or password", Toast.LENGTH_SHORT).show();
            } else {
                //resetting boolean
                registrationProcessed = false;
                //observer established to authenticate credentials
                allCurrentPlayers.observe(registrationActivity.this, players -> processRegistraton(players, temp_username, temp_password));
            }
        });

        //instantiating an interface of onclick listener for returning to login view
        binding.registrationReturnButton.setOnClickListener(v -> {
            Intent intent = MainActivity.mainFactory(getApplicationContext());
            startActivity(intent);
        });

    }

    //intent factory
    public static Intent registrationFactory(Context context) {
        return new Intent(context, registrationActivity.class);
    }

    /**
     * this method grabs the list of current players from the database
     */
    private void initializePlayerRepo() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                //establishing repo, grabbing list of players
                registration_repo = new PlayerRepository((Application) getApplicationContext());
                allCurrentPlayers = registration_repo.getAllPlayers();
            }

        });
    }

    /**
     * this method checks whether inputted username and password are not taken
     *
     * @param players the list of players from the database
     * @param user    the inputted username
     * @param pass    the inputted password
     */
    private void processRegistraton(List<Player> players, String user, String pass) {
        boolean taken = false;
        for (Player player : players) {
            if (!registrationProcessed) {
                //checking whether username is in the database
                if (player.getUsername().equalsIgnoreCase(user)) {
                    Toast.makeText(registrationActivity.this, "Username taken", Toast.LENGTH_SHORT).show();
                    registrationProcessed = true;
                    return;
                }
                //inputted user and password are valid and available, creating new account and returning to home screen
                else {
                    Player new_player = new Player(user, pass, false);
                    registration_repo.insert(new_player);
                    Toast.makeText(registrationActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
                    Intent intent = MainActivity.mainFactory(getApplicationContext());
                    startActivity(intent);
                    registrationProcessed = true;
                    return;
                }
            }
        }
    }
}