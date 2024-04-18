/**
 * Luis Hernandez, Guillermo Zendejas
 * April 6, 2024
 * GameActivity.java, this class describes the game round activity, the focus of our application
 */

package com.example.lipt;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lipt.Database.PlayerRepository;
import com.example.lipt.Database.Pokemon;
import com.example.lipt.Utils.PokemonInfo;
import com.example.lipt.databinding.ActivityGameBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GameActivity extends AppCompatActivity {
    private static final String CURRENT_USERNAME = "Active User";
    private static final int CURRENT_USER_ID = 0;
    private int current_id;
    private MediaPlayer mediaPlayer;
    private PlayerRepository player_repo;
    List<Pokemon> questionList = new ArrayList<>();
    List<Pokemon> gameList = new ArrayList<>();
    ImageView pokemon1, pokemon2, pokemon3, pokemon4;
    TextView pokemon1name, pokemon2name, pokemon3name, pokemon4name, questionNumText;
    Button adminButton;
    private int current_question = 1;
    private int solution = 0;
    private String solution_name;
    int final_score = 0, start_index = 0, end_index = 4;
    private boolean isAdmin = false;
    private final CountDownLatch latch = new CountDownLatch(1);
    Executor executor = Executors.newSingleThreadExecutor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.lipt.databinding.ActivityGameBinding binding = ActivityGameBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        current_id = getIntent().getIntExtra(CURRENT_USERNAME, 0);

        //determining whether logged in player is admin, and if not then hides the dev hack button
        adminCheck(current_id);
        try {
            latch.await();
            adminButton = findViewById(R.id.devHackButton);
            if(!isAdmin) {
                adminButton.setVisibility(View.GONE);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //setting image and text view values
        pokemon1 = findViewById(R.id.pokemon1Image);
        pokemon2 = findViewById(R.id.pokemon2Image);
        pokemon3 = findViewById(R.id.pokemon3Image);
        pokemon4 = findViewById(R.id.pokemon4Image);
        questionNumText = findViewById(R.id.gameQuestionNumberText);

        pokemon1name = findViewById(R.id.pokemon1NameText);
        pokemon2name = findViewById(R.id.pokemon2NameText);
        pokemon3name = findViewById(R.id.pokemon3NameText);
        pokemon4name = findViewById(R.id.pokemon4NameText);

        //instantiating 4 interfaces of onClickListener for pokemon images
        pokemon1.setOnClickListener(v -> handleImageClick(0));

        pokemon2.setOnClickListener(v -> handleImageClick(1));

        pokemon3.setOnClickListener(v -> handleImageClick(2));

        pokemon4.setOnClickListener(v -> handleImageClick(3));

        //instantiating an interface of onClickListener for playing the sound button
        binding.gameSoundButton.setOnClickListener(v -> mediaPlayer.start());

        //instantiating an interface of onCLickListener for dev hack button
        binding.devHackButton.setOnClickListener(v -> Toast.makeText(GameActivity.this, "Answer: " + solution_name, Toast.LENGTH_SHORT).show());

        //instantiating an interface of onClickListener for returning to menu button
        binding.exitGameButton.setOnClickListener(v -> {
            Intent intent = MenuActivity.menuFactory(getApplicationContext(), current_id);
            startActivity(intent);
        });

        //grabbing full pokemon list
        List<Pokemon> pokemonList = new ArrayList<>(PokemonInfo.full_pokemon_list);
        Log.d(MainActivity.TAG, "first pokemonlist = " + pokemonList.size());

        //populating game list of pokemon with 40 unique entries
        for (int i = 0; i < 40; i++) {
            drawRandomPokemon(pokemonList, gameList);
        }
        Log.d(MainActivity.TAG, "second gamelist = " + gameList.size());

        newQuestion();


    } //end on create()


    //this method handles the onclick for images in gameplay
    private void handleImageClick(int imageNum) {
        if(imageNum == solution) {
            final_score++;
        }
        current_question++;
        newQuestion();
    }

    //this method loads new images and the sound button cry for a new question
    private void newQuestion() {
        //game round is over, loading result screen
        if(current_question > 10) {
            //updating player stats
            updatePlayer(current_id);

            //shifting to game result activity
            Intent intent = GameResultActivity.gameResultFactory(getApplicationContext(), current_id, final_score);
            startActivity((intent));
        }

        else {
            //moving 4 pokemon from the game list into the question list for play
            if (questionList.isEmpty()) {
                questionList = gameList.subList(start_index, end_index);
            } else {
                for (int j = 0; j < 4; j++) {
                    questionList.set(j, gameList.get(start_index + j));
                }
            }
            start_index += 4;
            end_index += 4;

            //setting solution index and appropriate sound
            Random random = new Random();
            solution = random.nextInt(4);
            solution_name = questionList.get(solution).getName();
            setSound(questionList.get(solution).getSoundResourceId());
            //setting question text
            questionNumText.setText(String.valueOf(current_question) + "/10");

            //setting the view to display the four pokemon chosen
            pokemon1name.setText(questionList.get(0).getName());
            pokemon1.setImageResource(questionList.get(0).getImageResourceId());
            pokemon2name.setText(questionList.get(1).getName());
            pokemon2.setImageResource(questionList.get(1).getImageResourceId());
            pokemon3name.setText(questionList.get(2).getName());
            pokemon3.setImageResource(questionList.get(2).getImageResourceId());
            pokemon4name.setText(questionList.get(3).getName());
            pokemon4.setImageResource(questionList.get(3).getImageResourceId());

        }

    } //end onCreate()

    //for releasing the media player, prevention of memory leaks
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
            Log.d(MainActivity.TAG, "pokemon list size for drawing: " + pokemons.size());
            drawn_pokemon = pokemons.get(random.nextInt(pokemons.size()));
        } while(gameList.contains(drawn_pokemon));
        gameList.add(drawn_pokemon);

    }

    /**
     * this method sets the sound button's source to the correct resource upon loading a new question
     * @param soundResourceId
     */
    private void setSound(int soundResourceId) {
        if(mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, soundResourceId);
    }

    /**
     * this method updates the player's stats upon the game round ending
     * @param playerId the ID of player whose stats will be updated
     */
    private void updatePlayer(final int playerId) {
        executor.execute(() -> {
            player_repo = new PlayerRepository((Application) getApplicationContext());
            player_repo.increasePlayerPoints(playerId, final_score);
            player_repo.increasePlayerRoundsPlayed(playerId);

            if(final_score > 6) {
                player_repo.levelUpPlayer(playerId);
                Log.d(MainActivity.TAG, "player increased: " + playerId);
                Log.d(MainActivity.TAG, "points: " + final_score);
            }
        });
    }

    /**
     * this method determines whether the currently logged in player is an admin or not
     * then assigns the boolean to class field isAdmin
     * @param playerId the ID of currently logged in player
     */
    private void adminCheck(final int playerId) {
        executor.execute(() -> {
            player_repo = new PlayerRepository((Application) getApplicationContext());
            isAdmin = player_repo.getPlayerById(playerId).isAdmin();
            latch.countDown();
        });
    }
}