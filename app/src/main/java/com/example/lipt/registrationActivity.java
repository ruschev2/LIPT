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
public class registrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;
    private PlayerRepository registration_repo;
    private LiveData<List<Player>> allCurrentPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //establishing repo, grabbing list of players
        registration_repo = new PlayerRepository((Application) getApplicationContext());
        allCurrentPlayers = registration_repo.getAllPlayers();

        //instantiating an interface of onClickListener for register button
        binding.registrationButton2.setOnClickListener(v -> {

            //user inputted username and password
            String temp_username = binding.registrationUsernameTextEdit.getText().toString();
            String temp_password = binding.registrationPasswordTextEdit.getText().toString();

            //if inputted username and password are viable inputs
            if (!InputValidator.viableUsername(temp_username) || !InputValidator.viablePassword(temp_password)) {
                Toast.makeText(registrationActivity.this, "Invalid username and/or password", Toast.LENGTH_SHORT).show();
            }
            else {
                //observer established to authenticate credentials
                allCurrentPlayers.observe(registrationActivity.this, players -> processRegistraton(players, temp_username, temp_password));
            }
        });

        //instantiating an interface of onclick listener for returning to login view
        binding.registrationReturnButton.setOnClickListener(v -> {
            Intent intent = MainActivity.mainToRegistrationFactory(getApplicationContext());
            startActivity(intent);
        });

    }

    //intent factory
    public static Intent registrationToMainFactory(Context context) {
        return new Intent(context, registrationActivity.class);
    }

    //method to check for taken username and proceed with registration if not true
    private void processRegistraton(List<Player> players, String user, String pass) {
            boolean taken = false;
            for(Player player : players) {
                //checking whether username is in the database
                if(player.getUsername().equalsIgnoreCase(user)) {
                    taken = true;
                    Toast.makeText(registrationActivity.this, "Username taken", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
            //inputted user and password are valid and available, creating new account and returning to home screen
            if(!taken) {
                Player player = new Player(user, pass, false);
                registration_repo.insert(player);
                Toast.makeText(registrationActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
                Intent intent = MainActivity.mainToRegistrationFactory(getApplicationContext());
                startActivity(intent);
            }
        }
}