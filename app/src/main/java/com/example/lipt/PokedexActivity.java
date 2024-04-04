/**
 * Luis Hernandez, Guillermo Zendejas
 * April 3, 2024
 * PokedexActivity.java, this describes our application's Pokedex activity
 */

package com.example.lipt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.lipt.Database.PokeRepository;
import com.example.lipt.databinding.ActivityPokedexBinding;

public class PokedexActivity extends AppCompatActivity {

    private ActivityPokedexBinding binding;

    private static final String CURRENT_USERNAME = "Active User";
    private static final int CURRENT_USER_ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPokedexBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //retrieving and saving currently logged in player ID
        int current_id = getIntent().getIntExtra(CURRENT_USERNAME, 0);
        Toast.makeText(PokedexActivity.this, "RECORD ID: " + current_id, Toast.LENGTH_SHORT).show();

        //TODO: establish pokedex repo

        //TODO: implement scrollable view for pokedex

        //instantiating an interface of onClickListener for return to menu button
        binding.exitPokedexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MenuActivity.mainMenuFactory(getApplicationContext(), current_id);
                startActivity(intent);
            }
        });
    }

    //intent factory
    public static Intent pokedexFactory(Context context, int user_id) {
        Intent intent = new Intent(context, PokedexActivity.class);
        intent.putExtra(CURRENT_USERNAME, user_id);
        return intent;
    }
}