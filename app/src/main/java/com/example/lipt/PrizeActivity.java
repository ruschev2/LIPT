/**
 * Luis Hernandez, Guillermo Zendejas
 * April 4, 2024
 * PrizeActivity.java, this class describes the prize collection activity in our application
 */

package com.example.lipt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.example.lipt.Database.Player;
import com.example.lipt.Database.PlayerPrizeCrossRefRepository;
import com.example.lipt.Database.PlayerRepository;
import com.example.lipt.Database.Prize;
import com.example.lipt.Database.PrizeRepository;
import com.example.lipt.Utils.PokemonInfo;
import com.example.lipt.databinding.ActivityPrizeBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PrizeActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    private static final String CURRENT_USERNAME = "Active User";
    private static final int CURRENT_USER_ID = 0;
    private int current_id;
    private PlayerRepository player_repo;
    private PrizeRepository prize_repo;
    private PlayerPrizeCrossRefRepository playerprize_repo;
    private LiveData<List<Prize>> allPrizes;
    private List<Integer> earned_prize_ids = new ArrayList<>();
    private Player current_player;
    private RecyclerView recyclerView;
    private PrizeAdapter adapter;
    private ActivityPrizeBinding binding;
    Executor executor = Executors.newSingleThreadExecutor();
    private final CountDownLatch latch = new CountDownLatch(1);
    private List<String> unearnedPrizeNames;
    private boolean allPrizesEarned = false;
    private GestureDetector gestureDetector;
    private int scrollEdgeLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrizeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //retrieving and saving currently logged in Player ID
        current_id = getIntent().getIntExtra(CURRENT_USERNAME, 0);

        //grabbing player prize refs
        grabData(current_id);
        Log.d(MainActivity.TAG, "prize id: " + current_id);

        //generating recyclerView
        recyclerView = findViewById(R.id.prizeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(this);

        //configuring scrollbar mechanic
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent event1, @NonNull MotionEvent event2, float distX, float distY) {
                int scrollDist = (int) (distY / recyclerView.getHeight() * recyclerView.computeVerticalScrollRange());
                recyclerView.scrollBy(0, scrollDist);
                return true;
            }
        });
        scrollEdgeLimit = getResources().getDimensionPixelSize(R.dimen.scroll_edge_limit);

        //grabbing data first then launching observer call
        try {
            latch.await();
            allPrizes.observe(this, new Observer<List<Prize>>() {
                @Override
                public void onChanged(List<Prize> prizes) {
                    //generating new list for actually earned prizes
                    List<Prize> earnedPrizes = new ArrayList<>();
                    for (Prize prize : prizes) {
                        if (earned_prize_ids.contains(prize.getPrizeID())) {
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //instantiating an interface of onClickListener for viewing unearned prizes
        binding.seeUnearnedPrizesButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(PrizeActivity.this);
            builder.setTitle("List of Unearned Prizes");

            if(allPrizesEarned) {
                builder.setMessage("Congratulations, you have earned all prizes!");
            }
            else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < unearnedPrizeNames.size(); i++) {
                    sb.append(unearnedPrizeNames.get(i));
                    if (i < unearnedPrizeNames.size() - 1) {
                        sb.append(", ");
                    }
                }
                builder.setMessage(sb.toString());
            }

            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        });


        //instantiating an interface of onClickListener for resetting prizes
        binding.resetPrizesButton.setOnClickListener(v -> {
            if (earned_prize_ids.isEmpty()) {
                Toast.makeText(PrizeActivity.this, "No prizes to delete", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(PrizeActivity.this);
                builder.setTitle("Warning");
                builder.setMessage("Are you sure you wish to delete all your earned prizes?");

                builder.setPositiveButton("Yes", (dialog, which) -> {
                    deleteData(current_id);
                    Toast.makeText(PrizeActivity.this, "Prizes removed.", Toast.LENGTH_SHORT).show();
                    Intent intent = PrizeActivity.prizeFactory(getApplicationContext(), current_id);
                    startActivity(intent);
                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //instantiating an interface of onClickListener for return home button
        binding.prizeExitButton.setOnClickListener(v -> {
            Intent intent = MenuActivity.menuFactory(getApplicationContext(), current_id);
            startActivity(intent);
        });

    }

    //intent factory
    public static Intent prizeFactory(Context context, int user_id) {
        Intent intent = new Intent(context, PrizeActivity.class);
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


    /**
     * this method initializes the player and prize repos
     * @param playerId the ID of the currently logged in player.
     */
    private void grabData(final int playerId) {
        executor.execute(() -> {
            //establishing repos, grabbing full lists of players, prizes, and playerprizerefs
            playerprize_repo = new PlayerPrizeCrossRefRepository((Application) getApplicationContext());
            earned_prize_ids = playerprize_repo.getPlayerPrizeIdsForPlayer(playerId);
            player_repo = new PlayerRepository((Application) getApplicationContext());
            prize_repo = new PrizeRepository((Application) getApplicationContext());
            allPrizes = prize_repo.getAllPrizes();

            //grabbing current player
            current_player = player_repo.getPlayerById(current_id);
            Log.d(MainActivity.TAG, "executor prize id: " + current_player.getUserID());
            latch.countDown();

            List<Prize> unearnedPrizes;
            unearnedPrizes = prize_repo.getPrizeList();
            unearnedPrizes.removeIf(prize -> earned_prize_ids.contains(prize.getPrizeID()));

            allPrizesEarned = unearnedPrizes.isEmpty();
            unearnedPrizeNames = new ArrayList<>();
            for(Prize prize : unearnedPrizes) {
                unearnedPrizeNames.add(prize.getName());
            }
        });
    }

    /**
     * this method is for deleting a player's prizes, upon button click
     * it then refreshes the data
     * @param playerId the ID of the currently logged in player
     */
    private void deleteData(final int playerId) {
        executor.execute(() -> {
            playerprize_repo.removePlayerPrizes(playerId);
            earned_prize_ids = new ArrayList<>();
            unearnedPrizeNames = new ArrayList<>();
            for(int i = 1; i < 21; i++) {
                unearnedPrizeNames.add(PokemonInfo.getPrizeName(i));
            }
            allPrizesEarned = false;
        });
    }

}