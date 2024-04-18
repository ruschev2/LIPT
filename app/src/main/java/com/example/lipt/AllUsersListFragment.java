/**
 * Guillermo Zendejas, Luis Hernandez
 * April 15, 2024
 * Fragment that shows a list of all users.
 */

package com.example.lipt;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.lipt.Database.Player;
import java.util.ArrayList;


/**
 * Shows a list of all users through a recycler view.
 */
public class AllUsersListFragment extends Fragment implements  PlayerAdapter.ItemClickListener {
    private PlayerViewModel playerViewModel;
    private int loggedInId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playerViewModel = new ViewModelProvider(this).get(PlayerViewModel.class);
        loggedInId = getActivity().getIntent().getIntExtra(AdminActivity.ADMIN_ACTIVITY_USER_ID,0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_users_list, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.all_users_recyclerView);
        PlayerAdapter adapter = new PlayerAdapter(new ArrayList<>(), this, loggedInId);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        playerViewModel.getAllPlayers().observe(getViewLifecycleOwner(), allPlayers -> {
            adapter.setAllPlayersList(allPlayers);
        });

        return rootView;
    }

    /**
     * Handles behavior when Delete button is clicked on a user RecyclerView item.
     * @param player player to be deleted.
     */
    @Override
    public void onDeleteButtonClick(Player player) {
        playerViewModel.deletePlayerById(player.getUserID());
    }

    /**
     * Handles behavior when Info button is clicked on a user RecyclerView item.
     * @param player User who's info will be shown.
     */
    @Override
    public void onInfoButtonClick(Player player) {
        int loggedInId = getActivity().getIntent().getIntExtra(AdminActivity.ADMIN_ACTIVITY_USER_ID, 0);
        Intent intent = PlayerInfoActivity.playerInfoActivityIntentFactory(getActivity(), loggedInId ,player.getUserID());
        startActivity(intent);
    }
}