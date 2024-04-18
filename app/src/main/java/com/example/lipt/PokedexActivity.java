/**
 * Luis Hernandez, Guillermo Zendejas
 * April 3, 2024
 * PokedexActivity.java, this describes our application's Pokedex activity
 */

package com.example.lipt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.example.lipt.Database.Pokemon;
import com.example.lipt.Database.PokemonRepository;
import com.example.lipt.databinding.ActivityPokedexBinding;

import java.util.List;

public class PokedexActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {
    private ActivityPokedexBinding binding;
    private static final String CURRENT_USERNAME = "Active User";
    private static final int CURRENT_USER_ID = 0;
    private MediaPlayer mediaPlayer;
    private RecyclerView recyclerView;
    private PokemonAdapter adapter;
    private PokemonRepository pokedex_repo;
    private LiveData<List<Pokemon>> allPokemon;
    private GestureDetector gestureDetector;
    private int scrollEdgeLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPokedexBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //retrieving and saving currently logged in player ID
        int current_id = getIntent().getIntExtra(CURRENT_USERNAME, 0);

        //establishing repo, grabbing list of pokemon
        pokedex_repo = new PokemonRepository((Application) getApplicationContext());
        allPokemon = pokedex_repo.getAllPokemon();

        recyclerView = findViewById(R.id.pokedexRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(this);

        //configuring scrollbar mechanic
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent event1, MotionEvent event2, float distX, float distY) {
                int scrollDist = (int) (distY / recyclerView.getHeight() * recyclerView.computeVerticalScrollRange());
                recyclerView.scrollBy(0, scrollDist);
                return true;
            }
        });
        scrollEdgeLimit = getResources().getDimensionPixelSize(R.dimen.scroll_edge_limit);

        //observing pokemon data for recyclerview display
        allPokemon.observe(this, new Observer<List<Pokemon>>() {
            @Override
            public void onChanged(List<Pokemon> pokemons) {
                Log.d(MainActivity.TAG, "Pokemon no.1 sound ID: " + getResources().getIdentifier("sound1", "raw", getPackageName()));
                Log.d(MainActivity.TAG, "Pokemon no. 4 sound ID:" + getResources().getIdentifier("sound4", "raw", getPackageName()));
                adapter = new PokemonAdapter(PokedexActivity.this, pokemons);
                recyclerView.setAdapter(adapter);
            }
        });

        //instantiating an interface of onClickListener for return to menu button
        binding.exitPokedexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MenuActivity.menuFactory(getApplicationContext(), current_id);
                startActivity(intent);
            }
        });
    }

    //overriding on destroy to ensure media player is released and memory leaks are prevented
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer when the activity is destroyed to avoid resource leaks
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    //intent factory
    public static Intent pokedexFactory(Context context, int user_id) {
        Intent intent = new Intent(context, PokedexActivity.class);
        intent.putExtra(CURRENT_USERNAME, user_id);
        return intent;
    }

    //these three overridden methods support our recyclerview scrolling bar function
    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView r_v, @NonNull MotionEvent event) {
        if(isEdgeTouch(event)) {
            gestureDetector.onTouchEvent(event);
            return false;
        }
        return false;
    }
    @Override
    public void onTouchEvent(@NonNull RecyclerView r_v, @NonNull MotionEvent event) {}
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean blockIntercept) {}

    /**
     * this method determines whether a touch is near the edge of recyclerview where scrollbar mechanic is
     * @param e_ the touch event by player
     * @return a boolean indicating whether touch is near edge or not
     */
    private boolean isEdgeTouch(MotionEvent e_) {
        boolean result = false;
        int size = scrollEdgeLimit;
        int start = 0;
        int end = recyclerView.getWidth();
        int touchX = (int) e_.getX();
        result = touchX < start + size || touchX > end - size;
        return result;
    }

}