/**
 * Luis Hernandez, Guillermo Zendejas
 * April 1, 2024
 * registrationActivity.java, this describes the registration activity for our application
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
import android.widget.Toast;

import com.example.lipt.Database.Player;
import com.example.lipt.Database.PokeRepository;
import com.example.lipt.Utils.InputValidator;
import com.example.lipt.databinding.ActivityRegistrationBinding;

import java.util.List;

public class registrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;
    private PokeRepository registration_repo;
    private LiveData<List<Player>> allCurrentPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //establishing repo, grabbing list of players
        registration_repo = new PokeRepository((Application) getApplicationContext());
        allCurrentPlayers = registration_repo.getAllPlayers();

        //instantiating an interface of onClickListener for register button
        binding.registrationButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //user inputted username and password
                String temp_username = binding.registrationUsernameTextEdit.getText().toString();
                String temp_password = binding.registrationPasswordTextEdit.getText().toString();

                //if inputted username and password are viable inputs
                if (!InputValidator.viableUsername(temp_username) || !InputValidator.viablePassword(temp_password)) {
                    Toast.makeText(registrationActivity.this, "Invalid username and/or password", Toast.LENGTH_SHORT).show();
                }
                else {
                    //observer established to authenticate credentials
                    //TODO: MainActivity.this parameter may not work here since it is inside onClick()
                    allCurrentPlayers.observe(registrationActivity.this, new Observer<List<Player>>() {
                        @Override
                        public void onChanged(List<Player> players) {
                            boolean taken = false;
                            for(Player player : players) {
                                //checking whether username is in the database
                                if(player.getUsername().equalsIgnoreCase(temp_username)) {
                                    taken = true;
                                    Toast.makeText(registrationActivity.this, "Username taken", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            //inputted user and password are valid and available, creating new account and returning to home screen
                            if(!taken) {
                                Player player = new Player(temp_username, temp_password, false);
                                registration_repo.insert(player);
                                Toast.makeText(registrationActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
                                Intent intent = MainActivity.mainToRegistrationFactory(getApplicationContext());
                                startActivity(intent);
                            }
                        }
                    });
                }

            }
        });


        //instantiating an interface of onclick listener for returning to login view
        binding.registrationReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.mainToRegistrationFactory(getApplicationContext());
                startActivity(intent);
            }
        });

    }

    //intent factory
    public static Intent registrationToMainFactory(Context context) {
        return new Intent(context, registrationActivity.class);
    }


}