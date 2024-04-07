/**
 * Luis Hernandez, Guillermo Zendejas
 * April 6, 2024
 * GameActivity.java, this class describes the game round activity, the focus of our application
 */

package com.example.lipt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.lipt.Database.Player;
import com.example.lipt.Database.PlayerRepository;
import com.example.lipt.Database.Pokemon;
import com.example.lipt.Database.PokemonRepository;
import com.example.lipt.Database.Prize;
import com.example.lipt.Database.PrizeRepository;
import com.example.lipt.Utils.PokemonInfo;
import com.example.lipt.databinding.ActivityGameBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class GameActivity extends AppCompatActivity {

    private ActivityGameBinding binding;
    private static final String CURRENT_USERNAME = "Active User";
    private static final int CURRENT_USER_ID = 0;
    private MediaPlayer mediaPlayer;
    private PokemonRepository pokemon_repo;
    private LiveData<List<Pokemon>> allPokemon;

    boolean question_answered = false;

    //countdown latch for waiting until an image is clicked
    CountDownLatch latch = new CountDownLatch(1);
    private int image_choice;
    int final_score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        int current_id = getIntent().getIntExtra(CURRENT_USERNAME, 0);
        Toast.makeText(GameActivity.this, "GAME ID: " + current_id, Toast.LENGTH_SHORT).show();

        //grabbing full pokemon list
        pokemon_repo = new PokemonRepository((Application) getApplicationContext());
        allPokemon = pokemon_repo.getAllPokemon();
        List<Pokemon> pokemonList = new ArrayList<>(PokemonInfo.full_pokemon_list);
        Log.d(MainActivity.TAG, "first pokemonlist = " + pokemonList.size());

        /*

        allPokemon.observe(this, pokemon_List -> {
                    if(pokemon_List != null) {

                        Log.d(MainActivity.TAG, "initial observer copy list size: " + pokemon_List.size());
                        for(Pokemon pokemon : pokemonList) {
                            Pokemon new_pokemon = new Pokemon(pokemon.getPokedexNumber(),
                                    pokemon.getName(),
                                    pokemon.getImageResourceId(),
                                    pokemon.getSoundResourceId());
                            pokemonList.add(pokemon);

                        }
                        Log.d(MainActivity.TAG, "mid observer copy list size: " + pokemonList.size());
                    }
                });
        Log.d(MainActivity.TAG, "post observer copy list size: " + pokemonList.size());


         */


        /*

        binding.pokemon1Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick(0);
            }
        });

        binding.pokemon2Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick(1);
            }
        });

        binding.pokemon3Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick(2);
            }
        });

        binding.pokemon4Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick(3);
            }
        });
        allPokemon.observe(this, new Observer<List<Pokemon>>() {
            @Override
            public void onChanged(List<Pokemon> pokemons) {
                for(Pokemon pokemon : pokemons) {
                    Pokemon new_pokemon = new Pokemon(pokemon.getPokedexNumber(),
                            pokemon.getName(),
                            pokemon.getImageResourceId(),
                            pokemon.getSoundResourceId());
                    pokemonList.add(new_pokemon);
                }
                Toast.makeText(GameActivity.this, "Pokemon list: " + String.valueOf(pokemonList.size()), Toast.LENGTH_SHORT).show();

                //populating game list of pokemon with 40 unique entries
                for(int i = 0; i < 40; i++) {
                    drawRandomPokemon(pokemonList, gameList);
                }

                //actual game loop, 10 questions = 10 iterations
                for(int i = 0; i < 10; i++) {

                    //moving 4 pokemon from the game list into the round list for play
                    List<Pokemon> questionList = gameList.subList(0, 4);
                    gameList.removeAll(questionList);

                    //setting the view to display the four pokemon chosen
                    binding.pokemon1NameText.setText(questionList.get(0).getName());
                    binding.pokemon1Image.setImageResource(questionList.get(0).getImageResourceId());
                    binding.pokemon2NameText.setText(questionList.get(1).getName());
                    binding.pokemon2Image.setImageResource(questionList.get(1).getImageResourceId());
                    binding.pokemon3NameText.setText(questionList.get(2).getName());
                    binding.pokemon3Image.setImageResource(questionList.get(2).getImageResourceId());
                    binding.pokemon4NameText.setText(questionList.get(3).getName());
                    binding.pokemon4Image.setImageResource(questionList.get(3).getImageResourceId());

                    Random random = new Random();
                    int solution = random.nextInt(4);
                    mediaPlayer = MediaPlayer.create(GameActivity.this, questionList.get(solution).getSoundResourceId());
                    mediaPlayer.start();
                    question_answered = false;


                    try {
                        latch.await();
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(image_choice == solution) {
                        final_score++;
                    }

                }


            } //on changed
        }); //end observer call

        */

        //populating game list of pokemon with 40 unique entries
        List<Pokemon> gameList = new ArrayList<>();
        for(int i = 0; i < 40; i++) {
            drawRandomPokemon(pokemonList, gameList);
        }

        Log.d(MainActivity.TAG, "second gamelist = " + gameList.size());

        //actual game loop, 10 questions = 10 iterations
        //for(int i = 0; i < 10; i++) {

            //moving 4 pokemon from the game list into the round list for play
            List<Pokemon> questionList = gameList.subList(0, 4);
        Log.d(MainActivity.TAG, "third pokemonlist = " + questionList.size());
        Log.d(MainActivity.TAG, "third pokemonlistelement = " + questionList.get(0).getName());
            //gameList.removeAll(questionList);

                    //setting the view to display the four pokemon chosen
                    binding.pokemon1NameText.setText(questionList.get(0).getName());
                    binding.pokemon1Image.setImageResource(questionList.get(0).getImageResourceId());
                    binding.pokemon2NameText.setText(questionList.get(1).getName());
                    binding.pokemon2Image.setImageResource(questionList.get(1).getImageResourceId());
                    binding.pokemon3NameText.setText(questionList.get(2).getName());
                    binding.pokemon3Image.setImageResource(questionList.get(2).getImageResourceId());
                    binding.pokemon4NameText.setText(questionList.get(3).getName());
                    binding.pokemon4Image.setImageResource(questionList.get(3).getImageResourceId());

            Random random = new Random();
            int solution = random.nextInt(4);
            mediaPlayer = MediaPlayer.create(GameActivity.this, questionList.get(solution).getSoundResourceId());
            mediaPlayer.start();
            question_answered = false;

            try {
                latch.await();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }

            if(image_choice == solution) {
                final_score++;
            }

/*
            if(i == 9) {
                Intent intent = GameResultActivity.gameResultFactory(getApplicationContext(), current_id, final_score);
                startActivity(intent);
            }

 */
        //}





        //instantiating an interface of onClickListener for returning to menu button
        binding.exitGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MenuActivity.mainMenuFactory(getApplicationContext(), current_id);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    //game factory
    public static Intent gameFactory(Context context, int user_id) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(CURRENT_USERNAME, user_id);
        return intent;
    }

    /**
     * This method draws a unique, random pokemon from the full pokemon list
     * @param pokemons the original, full list of 493 pokemon
     * @param gameList the resulting game list used for one round of gameplay
     */
    public static void drawRandomPokemon(List<Pokemon> pokemons, List<Pokemon> gameList) {
        Random random = new Random();
        Pokemon drawn_pokemon;
        do {
            drawn_pokemon = pokemons.get(random.nextInt(pokemons.size()));
        } while(gameList.contains(drawn_pokemon));
        gameList.add(drawn_pokemon);

    }

    private void handleImageClick(int imageIndex) {
        image_choice = imageIndex;
        latch.countDown();
    }

}